package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

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
    public boolean installOperation(Operation operation, IResource currentResource) {
        Duration resultDuration = currentResource.putOperationOnResource(operation);
        if(resultDuration != null && (resultDuration.isNegative() || resultDuration.isZero())) {
            currentResource.setReleaseTime(operation.tactTime.plusNanos(durationOfExecution.toNanos()));
            operation.addCNumberOfAssignedRecourse(currentResource);
            operation.addCWorkingInterval(new WorkingHours(operation.tactTime, operation.tactTime.plusNanos(durationOfExecution.toNanos())));
            operation.serialAffiliation.setСNumberOfAssignedOperations(operation.serialAffiliation.getСNumberOfAssignedOperations() + 1);
            durationOfExecution = resultDuration;
            return true;
        }
        return false;
    }

    @Override
    public boolean reverseInstallOperation(Operation operation, IResource currentResource) {
        Duration resultDuration = currentResource.putReverseOperationOnResource(operation);
        if(resultDuration != null && (resultDuration.isNegative() || resultDuration.isZero())) {
            operation.addCNumberOfAssignedRecourse(currentResource);
            operation.addCWorkingInterval(new WorkingHours(operation.tactTime.minusNanos(durationOfExecution.toNanos()), operation.tactTime));
            operation.serialAffiliation.setСNumberOfAssignedOperations(operation.serialAffiliation.getСNumberOfAssignedOperations() + 1);
            durationOfExecution = resultDuration;
            return true;
        }
        return false;
    }

    @Override
    public void setNewTactTime(Operation operation) {
        LocalDateTime startDate = LocalDateTime.MAX;
        for(IResource tactRecourse: operation.resourceGroup.getRecoursesInTheGroup()) {
            LocalDateTime futureTactTime = tactRecourse.getStartDateAfterReleaseDate(operation.tactTime, operation);
            if(futureTactTime.isBefore(startDate)) {
                startDate = futureTactTime;
            }
        }
        operation.tactTime = startDate;
    }

    @Override
    public void setNewReverseTactTime(OperationWithPriorityNew operation) {
        LocalDateTime startTime = LocalDateTime.MIN;
        for(IResource tactRecourse: operation.getResourceGroup().getRecoursesInTheGroup()) {
            LocalDateTime futureTactTime = tactRecourse.getReverseStartDateAfterTactTime(operation.getTactTime(), operation);
            if(futureTactTime.isAfter(startTime)) {
                startTime = futureTactTime;
            }
        }
        operation.installTactTime(startTime);
    }

    @Override
    public boolean isResourcesCanToBorrow(Operation operation, IResource currentResource) {
        Duration resultDuration = currentResource.putOperationOnResource(operation);
        if(resultDuration != null && (resultDuration.isZero() || resultDuration.isNegative())){
            return true;
        }
        return false;
    }


}
