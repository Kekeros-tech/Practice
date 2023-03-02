package com.company.operation;

import com.company.*;
import com.company.recourse.IResource;
import com.company.recourse.IStructuralUnitOfResource;
import com.company.results_of_algos.ResultOfOperationSetting;
import com.company.results_of_algos.ResultOfRecourseBooking;

import java.time.Duration;
import java.time.LocalDateTime;

public class MO_CanNotBeInterrupted implements IOperationMode {
    Duration durationOfExecution;

    public MO_CanNotBeInterrupted(Duration durationOfExecution) {
        this.durationOfExecution = durationOfExecution;
    }

    public MO_CanNotBeInterrupted() {
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
            if(resultDuration != null && (resultDuration.isNegative() || resultDuration.isZero())) {
                ResultOfOperationSetting resultOfOperationSetting = new ResultOfOperationSetting(
                    resultOfRecourseBooking.getUnitOfResource(),
                    new WorkingHours(operation.getTactTime(), operation.getTactTime().plusNanos(durationOfExecution.toNanos()))
                );
                durationOfExecution = resultDuration;
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
            if (resultDuration != null && (resultDuration.isNegative() || resultDuration.isZero())) {
                ResultOfOperationSetting resultOfOperationSetting = new ResultOfOperationSetting(
                        resultOfRecourseBooking.getUnitOfResource(),
                        new WorkingHours(operation.getTactTime().minusNanos(durationOfExecution.toNanos()), operation.getTactTime())
                );
                durationOfExecution = resultDuration;
                return resultOfOperationSetting;
            }
        }
        return null;
    }

    @Override
    public void setNewTactTime(IOperation operation) {
        LocalDateTime startDate = LocalDateTime.MAX;
        for(IResource tactRecourse: operation.getResourceGroup().getRecoursesInTheGroup()) {
            LocalDateTime futureTactTime = tactRecourse.getStartDateAfterReleaseDate(operation.getTactTime(), operation);
            if(futureTactTime.isBefore(startDate)) {
                startDate = futureTactTime;
            }
        }
        operation.setTactTime(startDate);
    }

    @Override
    public void setNewReverseTactTime(IOperation operation) {
        LocalDateTime startTime = LocalDateTime.MIN;
        for(IResource tactRecourse: operation.getResourceGroup().getRecoursesInTheGroup()) {
            LocalDateTime futureTactTime = tactRecourse.getReverseStartDateAfterTactTime(operation.getTactTime(), operation);
            if(futureTactTime.isAfter(startTime)) {
                startTime = futureTactTime;
            }
        }
        operation.setTactTime(startTime);
    }

    @Override
    public IStructuralUnitOfResource isResourcesCanToBorrow(O_Basic OBasic, IResource currentResource) {
        ResultOfRecourseBooking resultOfRecourseBooking = currentResource.putOperationOnResource(OBasic);
        if(resultOfRecourseBooking != null) {
            Duration resultDuration = resultOfRecourseBooking.getDurationOfBooking();
            if(resultDuration != null && (resultDuration.isZero() || resultDuration.isNegative())){
                return resultOfRecourseBooking.getUnitOfResource();
            }
        }
        return null;
    }

    @Override
    public IOperationMode clone() {
        return new MO_CanNotBeInterrupted(this.durationOfExecution);
    }
}
