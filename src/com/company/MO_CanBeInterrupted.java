package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

public class MO_CanBeInterrupted implements IOperationMode{
    Duration durationOfExecution;

    public MO_CanBeInterrupted(Duration durationOfExecution) {
        this.durationOfExecution = durationOfExecution;
    }

    public MO_CanBeInterrupted() {

    }

    @Override
    public void setDurationOfExecution(Duration durationOfExecution) {
        this.durationOfExecution = durationOfExecution;
    }

    @Override
    public Duration getDurationOfExecution() {
        return durationOfExecution;
    }

    @Override
    public boolean operationNotScheduled() {
        if((durationOfExecution.isNegative() || durationOfExecution.isZero())) {
            return false;
        }
        return true;
    }

    @Override
    public ResultOfOperationSetting installOperation(IOperation operation, IResource currentResource) {
        ResultOfRecourseBooking resultOfRecourseBooking = currentResource.putOperationOnResource(operation);
        if(resultOfRecourseBooking != null) {
            Duration resultDuration = resultOfRecourseBooking.getDurationOfBooking();
            if (resultDuration != null) {
                Duration gupDuration = durationOfExecution.minus(resultDuration);
                if (resultDuration.isZero() || resultDuration.isNegative()) {
                    gupDuration = durationOfExecution;
                }
                durationOfExecution = resultDuration;
                ResultOfOperationSetting resultOfOperationSetting = new ResultOfOperationSetting(
                        resultOfRecourseBooking.getUnitOfResource(),
                        new WorkingHours(operation.getTactTime(), operation.getTactTime().plusNanos(gupDuration.toNanos())));
                return resultOfOperationSetting;
            }
        }
        return null;
    }

    @Override
    public ResultOfOperationSetting reverseInstallOperation(IOperation operation, IResource currentResource) {
        ResultOfRecourseBooking resultOfRecourseBooking = currentResource.putReverseOperationOnResource(operation);
        if(resultOfRecourseBooking != null) {
            Duration resultDuration = resultOfRecourseBooking.getDurationOfBooking();
            if (resultDuration != null) {
                Duration gupDuration = durationOfExecution.minus(resultDuration);
                if (resultDuration.isZero() || resultDuration.isNegative()) {
                    gupDuration = durationOfExecution;
                }
                durationOfExecution = resultDuration;
                ResultOfOperationSetting resultOfOperationSetting = new ResultOfOperationSetting(
                        resultOfRecourseBooking.getUnitOfResource(),
                        new WorkingHours(operation.getTactTime().minusNanos(gupDuration.toNanos()), operation.getTactTime())
                );
                return resultOfOperationSetting;
            }
        }
        return null;
    }

    @Override
    public void setNewTactTime(IOperation operation) {
        // Это чтобы не мог устрановиться раньше отработанных частей
        LocalDateTime endOfPreviousParts = LocalDateTime.MIN;
        for(WorkingHours currentWH: operation.getCWorkingInterval()) {
            LocalDateTime currentEndTime = currentWH.getEndTime();
            if(currentEndTime.isAfter(endOfPreviousParts)) {
                endOfPreviousParts = currentEndTime;
            }
        }

        LocalDateTime startDate = LocalDateTime.MAX;
        for(IResource tactRecourse: operation.getResourceGroup().getRecoursesInTheGroup()) {
            LocalDateTime futureTactTime = tactRecourse.getStartDateAfterReleaseDate(endOfPreviousParts, operation);
            if(futureTactTime.isBefore(startDate)) {
                startDate = futureTactTime;
            }
        }

        operation.setTactTime(startDate);
    }

    @Override
    public void setNewReverseTactTime(IOperation operation) {
        LocalDateTime endOfPreviousParts = LocalDateTime.MAX;
        for(WorkingHours currentWH: operation.getCWorkingInterval()) {
            LocalDateTime currentStartTime = currentWH.getStartTime();
            if(currentStartTime.isBefore(endOfPreviousParts)) {
                endOfPreviousParts = currentStartTime;
            }
        }

        LocalDateTime startTime = LocalDateTime.MIN;
        for(IResource tactRecourse: operation.getResourceGroup().getRecoursesInTheGroup()) {
            LocalDateTime futureTactTime = tactRecourse.getReverseStartDateAfterTactTime(endOfPreviousParts, operation);
            if(futureTactTime.isAfter(startTime)) {
                startTime = futureTactTime;
            }
        }
        operation.setTactTime(startTime);
    }

    @Override
    public boolean isResourcesCanToBorrow(Operation operation, IResource currentResource) {
        ResultOfRecourseBooking resultOfRecourseBooking = currentResource.putOperationOnResource(operation);
        if(resultOfRecourseBooking != null) {
            Duration resultDuration = resultOfRecourseBooking.getDurationOfBooking();
            if (resultDuration != null) {
                return true;
            }
        }
        return false;
    }
}
