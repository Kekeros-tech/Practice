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
    public boolean installOperation(Operation operation, IResource currentResource) {
        //for(IResource currentResource: operation.resourceGroup.getRecoursesInTheGroup()) {
            Duration resultDuration = currentResource.putOperationOnResource(operation);
            if(resultDuration != null) {
                Duration gupDuration = durationOfExecution.minus(resultDuration);
                currentResource.setReleaseTime(operation.tactTime.plusNanos(durationOfExecution.toNanos()));
                if(resultDuration.isZero() || resultDuration.isNegative()) {
                    gupDuration = durationOfExecution;
                    operation.serialAffiliation.setСNumberOfAssignedOperations(operation.serialAffiliation.getСNumberOfAssignedOperations() + 1);
                }
                durationOfExecution = resultDuration;
                operation.addCWorkingInterval(new WorkingHours(operation.tactTime, operation.tactTime.plusNanos(gupDuration.toNanos())));
                operation.addCNumberOfAssignedRecourse(currentResource);
                return true;
            }
            return false;
        //}
    }

    @Override
    public boolean reverseInstallOperation(Operation operation, IResource currentResource) {
        Duration resultDuration = currentResource.putReverseOperationOnResource(operation);
        if(resultDuration != null) {
            Duration gupDuration = durationOfExecution.minus(resultDuration);
            if(resultDuration.isZero() || resultDuration.isNegative()) {
                gupDuration = durationOfExecution;
                operation.serialAffiliation.setСNumberOfAssignedOperations(operation.serialAffiliation.getСNumberOfAssignedOperations() + 1);
            }
            durationOfExecution = resultDuration;
            operation.addCWorkingInterval(new WorkingHours(operation.tactTime.minusNanos(gupDuration.toNanos()), operation.tactTime));
            operation.addCNumberOfAssignedRecourse(currentResource);
            return true;
        }
        return false;
    }

    @Override
    public void setNewTactTime(Operation operation) {
        // Это чтобы не мог устрановиться раньше отработанных частей
        LocalDateTime endOfPreviousParts = LocalDateTime.MIN;
        for(WorkingHours currentWH: operation.getCWorkingInterval()) {
            LocalDateTime currentEndTime = currentWH.getEndTime();
            if(currentEndTime.isAfter(endOfPreviousParts)) {
                endOfPreviousParts = currentEndTime;
            }
        }

        LocalDateTime startDate = LocalDateTime.MAX;
        for(IResource tactRecourse: operation.resourceGroup.getRecoursesInTheGroup()) {
            LocalDateTime futureTactTime = tactRecourse.getStartDateAfterReleaseDate(endOfPreviousParts, operation);
            if(futureTactTime.isBefore(startDate)) {
                startDate = futureTactTime;
            }
        }

        operation.tactTime = startDate;
    }

    @Override
    public void setNewReverseTactTime(OperationWithPriorityNew operation) {
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
        operation.installTactTime(startTime);
    }

    @Override
    public boolean isResourcesCanToBorrow(Operation operation, IResource currentResource) {
        Duration resultDuration = currentResource.putOperationOnResource(operation);
        if(resultDuration != null) {
            return true;
        }
        return false;
    }
}
