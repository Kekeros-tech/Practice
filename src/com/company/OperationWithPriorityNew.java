package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class OperationWithPriorityNew extends Operation {
    protected IPriority priority;
    private LocalDateTime CEarliestStartTime;
    private LocalDateTime CLatestStartTime;


    public LocalDateTime getCLateStartTime() { return CLatestStartTime; }
    public LocalDateTime getCEarlierStartTime() { return  CEarliestStartTime; }
    public long getPriority() {
        return priority.getPriority();
    }


    public OperationWithPriorityNew(Group                 resourceGroup,
                                    Series                serialAffiliation,
                                    Collection<Operation> previousOperations,
                                    Collection<Operation> followingOperations,
                                    Duration              durationOfExecution,
                                    int                   currentOperatingMode,
                                    IPriority             priority) {
        super(resourceGroup, serialAffiliation, previousOperations, followingOperations, durationOfExecution, currentOperatingMode);
        this.priority = priority;
    }

    public OperationWithPriorityNew(Group                 resourceGroup,
                                    Series                serialAffiliation,
                                    Collection<Operation> previousOperations,
                                    Collection<Operation> followingOperations,
                                    Duration              durationOfExecution,
                                    int                   currentOperatingMode,
                                    PriorityType          priority) {
        super(resourceGroup, serialAffiliation, previousOperations, followingOperations, durationOfExecution, currentOperatingMode);
        choosePriority(priority);
    }

    public OperationWithPriorityNew() {
        super();
    }

    public void installPriority(PriorityType priorityType) { choosePriority(priorityType); }
    public void setPriority() {
        priority.setPriority(this);
    }

    public OperationWithPriorityNew(PriorityType priorityType) {
        super();
        choosePriority(priorityType);
    }

    public OperationWithPriorityNew(Operation operation, PriorityType priorityType){
        super(operation);
        choosePriority(priorityType);
    }

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
    public boolean allPreviousAssignedReverse() {
        System.out.println(this.getClass().getSimpleName());
        System.out.println();
        for(int i = 0; i < previousOperations.size(); i++) {
            if(previousOperations.get(i).getCLateStartTime() != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean allFollowingAssignedReverse() {
        for(int i = 0; i < followingOperations.size(); i++){
            if(followingOperations.get(i).getCLateStartTime() == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void fullClean() {
        super.fullClean();
        priority.clean();
        CEarliestStartTime = null;
        CLatestStartTime = null;
    }

    @Override
    public void installOperation() {
        super.installOperation();
        CEarliestStartTime = super.tactTime;
    }

    @Override
    public void installOperationForSpecificResource(IResource currentRecourse) {
        super.installOperationForSpecificResource(currentRecourse);
        CEarliestStartTime = super.tactTime;
    }

    @Override
    public void getLatestEndTimeOfFollowing() {
        if(tactTime != null) {
            return;
        }

        else if (followingOperations.isEmpty()) {
            tactTime = serialAffiliation.getDeadlineForCompletion();
        }
        else {
            tactTime = LocalDateTime.MAX;
            for (int i = 0; i < followingOperations.size(); i++) {
                if (followingOperations.get(i).getCLateStartTime().isBefore(tactTime)) {
                    tactTime = followingOperations.get(i).getCLateStartTime();
                }
            }
        }
    }

    @Override
    public LocalDateTime installReverseOperation() {
        LocalDateTime startDate = LocalDateTime.MIN;

        for (IResource tactRecourse: resourceGroup.getRecoursesInTheGroup()) {
            LocalDateTime newTime = null;
            if(currentOperatingMode == OperatingMode.canBeInterrupted) {
                newTime = tactRecourse.takeReverseWhichCanBeInterrupted(durationOfExecution, tactTime, this.getEarliestStartTime());
            }
            else
            {
                newTime = tactRecourse.tackReverseWhichCanNotBeInterrupted(durationOfExecution, tactTime, this.getEarliestStartTime());
            }

            if ( newTime != null && newTime.isAfter(startDate) ) {
                startDate = newTime;
            }
        }
        if( startDate != LocalDateTime.MIN ) {
            CLatestStartTime = startDate;
            serialAffiliation.setСNumberOfAssignedOperations(serialAffiliation.getСNumberOfAssignedOperations() + 1);
            return startDate;
        }

        LocalDateTime localmax = LocalDateTime.MIN;
        for(IResource currentRecourse: resourceGroup.getRecoursesInTheGroup())
        {
            for(int i = currentRecourse.getSchedule().size() - 1; i > 0; i--) {
                if (currentRecourse.getSchedule().get(i).getStartTime().isAfter(this.getEarliestStartTime()) && currentRecourse.getSchedule().get(i).getEndTime().isBefore(tactTime)) {
                    if(currentRecourse.getSchedule().get(i).getEndTime().isAfter(localmax)) {
                        localmax = currentRecourse.getSchedule().get(i).getEndTime();
                        break;
                    }
                }
            }
        }
        tactTime = localmax;
        return null;
    }

    public LocalDateTime getEarliestStartTime() {
        LocalDateTime maxTime = LocalDateTime.MIN;

        for(int i = 0; i < previousOperations.size(); i++ ) {
            if(previousOperations.get(i).getCEarlierStartTime().isAfter(maxTime))
            {
                maxTime = previousOperations.get(i).getCEarlierStartTime();
            }
        }
        if(maxTime == LocalDateTime.MIN) {
            return this.CEarliestStartTime;
        }
        return maxTime;
    }
}
