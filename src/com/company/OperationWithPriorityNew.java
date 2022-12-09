package com.company;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class OperationWithPriorityNew implements IOperation {
    private Operation underlyingOperation;
    protected IPriority priority;
    private LocalDateTime CEarliestStartTime;
    private LocalDateTime CLatestStartTime;


    public LocalDateTime getCLateStartTime() { return CLatestStartTime; }
    public LocalDateTime getCEarlierStartTime() { return  CEarliestStartTime; }
    public long getPriority() { return priority.getPriority(); }
    public Group getResourceGroup() { return underlyingOperation.getResourceGroup(); }
    public void installTactTime(LocalDateTime tactTime) { underlyingOperation.tactTime = tactTime; }
    public ArrayList<WorkingHours> getCWorkingInterval() { return underlyingOperation.getCWorkingInterval(); }

    @Override
    public int getNumberOfOperationMode() { return underlyingOperation.getNumberOfOperationMode(); }

    public OperationWithPriorityNew() {
        underlyingOperation = new Operation();
    }

    public void installPriority(PriorityType priorityType) { choosePriority(priorityType); }
    public void setPriority() { priority.setPriority(this); }
    @Override
    public void setNameOfOperation(String nameOfOperation) { underlyingOperation.setNameOfOperation(nameOfOperation); }

    private void choosePriority(PriorityType priorityType){
        switch (priorityType) {
            case priorityByHeirs:
                this.priority = new PriorityByHeirs();
                break;
            case priorityByDuration:
                this.priority = new PriorityByDuration();
                break;
            case priorityByLastStartTime:
                this.priority = new PriorityByLastStartTime();
                break;
            case priorityByDurationBetweenEarliestStartTimeAndLatestStartTime:
                this.priority = new PriorityByDurationBetweenEarliestStartTimeAndLatestStartTime();
                break;
        }
    }

    @Override
    public String toString() {
        return underlyingOperation.toString();
    }


    @Override
    public boolean isСanBePlacedInFront() {
        return underlyingOperation.isСanBePlacedInFront();
    }

    @Override
    public void setTactTime() {
        underlyingOperation.setTactTime();
    }

    @Override
    public ArrayList<IOperation> getFollowingOperations() {
        return underlyingOperation.followingOperations;
    }

    @Override
    public ArrayList<IOperation> getPreviousOperations() {
        return underlyingOperation.previousOperations;
    }

    @Override
    public void setPreviousOperation(ArrayList<IOperation> previousOperation) {
        underlyingOperation.setPreviousOperation(previousOperation);
    }

    @Override
    public LocalDateTime getTactTime() {
        return underlyingOperation.getTactTime();
    }

    @Override
    public Duration getDurationOfExecution() {
        return underlyingOperation.getDurationOfExecution();
    }

    @Override
    public boolean operationNotScheduled() {
        return underlyingOperation.operationNotScheduled();
    }

    @Override
    public void setResourceGroup(Group resourceGroup) {
        underlyingOperation.setResourceGroup(resourceGroup);
    }

    @Override
    public void setSerialAffiliation(Series serialAffiliation) {
        underlyingOperation.setSerialAffiliation(serialAffiliation);
    }

    @Override
    public void setDurationOfExecution(Duration durationOfExecution) {
        underlyingOperation.setDurationOfExecution(durationOfExecution);
    }

    @Override
    public void setOperatingMode(int currentOperatingMode) {
        underlyingOperation.setOperatingMode(currentOperatingMode);
    }

    @Override
    public void addCNumberOfAssignedRecourse(IResource cNumberOfAssignedRecourse) {
        underlyingOperation.addCNumberOfAssignedRecourse(cNumberOfAssignedRecourse);
    }

    @Override
    public void addCWorkingInterval(WorkingHours cWorkingInterval) {
        underlyingOperation.addCWorkingInterval(cWorkingInterval);
    }

    @Override
    public void addFollowingOperation(IOperation followingOperation) {
        underlyingOperation.addFollowingOperation(followingOperation);
    }

    @Override
    public void fullClean() {
        underlyingOperation.fullClean();
        priority.clean();
        CEarliestStartTime = null;
        CLatestStartTime = null;
    }

    @Override
    public void installOperation() {
        underlyingOperation.installOperation();
        CEarliestStartTime = underlyingOperation.tactTime;
    }

    @Override
    public void installOperationForSpecificResource(IResource currentRecourse) {
        underlyingOperation.installOperationForSpecificResource(currentRecourse);
        CEarliestStartTime = underlyingOperation.tactTime;
    }

    @Override
    public boolean isCanBePlacedInReverseFront() {
        if(operationNotScheduled() && underlyingOperation.allFollowingAssigned()){
            return true;
        }
        return false;
    }

    //Переписать, задание такта времени при работе назад
    @Override
    public void getLatestEndTimeOfFollowing() {
        if(underlyingOperation.tactTime != null) {
            return;
        }

        else if (underlyingOperation.followingOperations.isEmpty()) {
            underlyingOperation.tactTime = underlyingOperation.serialAffiliation.getDeadlineForCompletion();
        }
        else {
            underlyingOperation.tactTime = LocalDateTime.MAX;
            for (int i = 0; i < underlyingOperation.followingOperations.size(); i++) {
                if (underlyingOperation.followingOperations.get(i).getCLateStartTime().isBefore(underlyingOperation.tactTime)) {
                    underlyingOperation.tactTime = underlyingOperation.followingOperations.get(i).getCLateStartTime();
                }
            }
        }
    }

    public void setReverseTactTime() {
        if(underlyingOperation.getTactTime() != null) return;
        if(underlyingOperation.followingOperations.isEmpty()) underlyingOperation.tactTime = underlyingOperation.serialAffiliation.getDeadlineForCompletion();
        else setTactTimeByEndTimeOfFollowing();
    }

    public void setTactTimeByEndTimeOfFollowing() {
        underlyingOperation.tactTime = LocalDateTime.MAX;
        for(int i = 0; i < underlyingOperation.followingOperations.size(); i ++) {
            if (underlyingOperation.followingOperations.get(i).operationNotScheduled()) {
                underlyingOperation.tactTime = null;
                return;
            } else {
                for (WorkingHours currentWH : underlyingOperation.followingOperations.get(i).getCWorkingInterval()) {
                    if(currentWH.getStartTime().isBefore(underlyingOperation.tactTime)){
                        underlyingOperation.tactTime = currentWH.getStartTime();
                    }
                }
            }
        }
    }

    @Override
    public void clean() {
        underlyingOperation.clean();
    }

    @Override
    public LocalDateTime installReverseOperation() {
        for (IResource tactRecourse: underlyingOperation.resourceGroup.getRecoursesInTheGroup()) {
            Boolean operationSuccessfullyInstalled = underlyingOperation.currentOperatingMode.reverseInstallOperation(underlyingOperation, tactRecourse);
            if(operationSuccessfullyInstalled) {
                CLatestStartTime = underlyingOperation.tactTime.minusNanos(underlyingOperation.getInitDurationOfExecution().toNanos());
                break;
            }
        }
        return null;
    }

    @Override
    public void setTactTimeByEndTimeOfPrevious() {
        underlyingOperation.setTactTimeByEndTimeOfPrevious();
    }

    public LocalDateTime getEarliestStartTime() {
        LocalDateTime maxTime = LocalDateTime.MIN;

        for(int i = 0; i < underlyingOperation.previousOperations.size(); i++ ) {
            if(underlyingOperation.previousOperations.get(i).getCEarlierStartTime().isAfter(maxTime))
            {
                maxTime = underlyingOperation.previousOperations.get(i).getCEarlierStartTime();
            }
        }
        if(maxTime == LocalDateTime.MIN) {
            return this.CEarliestStartTime;
        }
        return maxTime;
    }

    @Override
    public void setNewTactTime() {
        underlyingOperation.currentOperatingMode.setNewTactTime(underlyingOperation);
    }

    @Override
    public ArrayList<IResource> getResourcesToBorrow() {
        return underlyingOperation.getResourcesToBorrow();
    }

    public void setNewReverseTactTime() {
        underlyingOperation.currentOperatingMode.setNewReverseTactTime(this);
    }
}
