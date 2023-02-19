package com.company;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class OperationWithPriorityNew implements IOperation {
    private IOperation underlyingOperation;
    protected IPriority priority;
    private LocalDateTime CEarliestStartTime;
    private LocalDateTime CLatestStartTime;


    public LocalDateTime getCLateStartTime() { return CLatestStartTime; }
    public LocalDateTime getCEarlierStartTime() { return  CEarliestStartTime; }
    public long getPriority() { return priority.getPriority(); }
    public Group getResourceGroup() { return underlyingOperation.getResourceGroup(); }
    public ArrayList<WorkingHours> getCWorkingInterval() { return underlyingOperation.getCWorkingInterval(); }

    @Override
    public int getNumberOfOperationMode() { return underlyingOperation.getNumberOfOperationMode(); }

    public OperationWithPriorityNew() {
        underlyingOperation = new Operation();
    }

    public OperationWithPriorityNew(IOperation operation) {
        underlyingOperation = operation;
    }

    public OperationWithPriorityNew(Series serialAffiliation,
                                    Group resourceGroup,
                                    Duration durationOfExecution,
                                    int currentOperationMode){
        underlyingOperation = new Operation(serialAffiliation, resourceGroup, durationOfExecution, currentOperationMode);
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
        final StringBuffer sb = new StringBuffer("Операция с приоритетом - ");
        sb.append(underlyingOperation.toString());
        return sb.toString();
    }

    public boolean underlyingOperationIsChain() {
        if(underlyingOperation.getClass().getSimpleName() == "O_OperationWithQuantityChain") {
            return true;
        }
        return false;
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
    public void setTactTime(LocalDateTime tactTime) {
        underlyingOperation.setTactTime(tactTime);
    }

    @Override
    public Series getSerialAffiliation() {
        return underlyingOperation.getSerialAffiliation();
    }

    @Override
    public IOperationMode getCurrentOperatingMode() {
        return underlyingOperation.getCurrentOperatingMode();
    }

    @Override
    public Collection<Operation> getOperationsAtCore() {
        return underlyingOperation.getOperationsAtCore();
    }

    @Override
    public Duration getInitDurationOfExecution() {
        return underlyingOperation.getInitDurationOfExecution();
    }

    @Override
    public LocalDateTime getEarliestTimeOfWorkingInterval() {
        return underlyingOperation.getEarliestTimeOfWorkingInterval();
    }

    @Override
    public boolean allFollowingAssigned() {
        return underlyingOperation.allFollowingAssigned();
    }

    @Override
    public ArrayList<IOperation> getFollowingOperations() {
        return underlyingOperation.getFollowingOperations();
    }

    @Override
    public ArrayList<IOperation> getPreviousOperations() {
        return underlyingOperation.getPreviousOperations();
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

/*    @Override
    public void addCNumberOfAssignedRecourse(IStructuralUnitOfResource cNumberOfAssignedRecourse) {
        underlyingOperation.addCNumberOfAssignedRecourse(cNumberOfAssignedRecourse);
    }

    @Override
    public void addCWorkingInterval(WorkingHours cWorkingInterval) {
        underlyingOperation.addCWorkingInterval(cWorkingInterval);
    }*/

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
        CEarliestStartTime = underlyingOperation.getEarliestTimeOfWorkingInterval();
    }

    @Override
    public void installOperationForSpecificResource(IResource currentRecourse) {
        underlyingOperation.installOperationForSpecificResource(currentRecourse);
        CEarliestStartTime = underlyingOperation.getEarliestTimeOfWorkingInterval();
    }

    @Override
    public void installOperationForSpecificResource(IResource currentRecourse, int numberOfOperations) {
        underlyingOperation.installOperationForSpecificResource(currentRecourse, numberOfOperations);
        CEarliestStartTime = underlyingOperation.getEarliestTimeOfWorkingInterval();
    }

    @Override
    public boolean isCanBePlacedInReverseFront() {
        return underlyingOperation.isCanBePlacedInReverseFront();
    }

    public void setReverseTactTime() {
        if(underlyingOperation.getTactTime() != null) return;
        if(underlyingOperation.getFollowingOperations().isEmpty()) underlyingOperation.setTactTime(underlyingOperation.getSerialAffiliation().getDeadlineForCompletion());
        else setTactTimeByEndTimeOfFollowing();
    }

    public void setTactTimeByEndTimeOfFollowing() {
        underlyingOperation.setTactTime(LocalDateTime.MAX);
        for(int i = 0; i < underlyingOperation.getFollowingOperations().size(); i ++) {
            if (underlyingOperation.getFollowingOperations().get(i).operationNotScheduled()) {
                underlyingOperation.setTactTime(null);
                return;
            } else {
                for (WorkingHours currentWH : underlyingOperation.getFollowingOperations().get(i).getCWorkingInterval()) {
                    if(currentWH.getStartTime().isBefore(underlyingOperation.getTactTime())){
                        underlyingOperation.setTactTime(currentWH.getStartTime());
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
    public void installReverseOperation() {
        underlyingOperation.installReverseOperation();
        CLatestStartTime = underlyingOperation.getEarliestTimeOfWorkingInterval();
    }

    @Override
    public void setTactTimeByEndTimeOfPrevious() {
        underlyingOperation.setTactTimeByEndTimeOfPrevious();
    }

    @Override
    public void setNewTactTime() {
        underlyingOperation.setNewTactTime();
    }

    @Override
    public ArrayList<IStructuralUnitOfResource> getResourcesToBorrow() {
        return underlyingOperation.getResourcesToBorrow();
    }

    @Override
    public int getCountOfOperations() {
        return underlyingOperation.getCountOfOperations();
    }

    public void setNewReverseTactTime() {
        underlyingOperation.setNewReverseTactTime();
    }
}
