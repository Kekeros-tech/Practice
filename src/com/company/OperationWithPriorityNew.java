package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class OperationWithPriorityNew extends Operation {
    protected IPriority priority;

    public OperationWithPriorityNew(Group resourceGroup, Series serialAffiliation, Collection<Operation> previousOperations, Collection<Operation> followingOperations,
              Duration durationOfExecution, int currentOperatingMode, IPriority priority) {
        super(resourceGroup, serialAffiliation, previousOperations, followingOperations, durationOfExecution, currentOperatingMode);
        this.priority = priority;
    }

    public OperationWithPriorityNew(Group resourceGroup, Series serialAffiliation, Collection<Operation> previousOperations, Collection<Operation> followingOperations,
                                    Duration durationOfExecution, int currentOperatingMode, PriorityType priority) {
        super(resourceGroup, serialAffiliation, previousOperations, followingOperations, durationOfExecution, currentOperatingMode);
        switch (priority) {
            case priorityByHeirs:
                this.priority = new PriorityByHeirs();
                break;
            case priorityByDuration:
                this.priority = new PriorityByDuration();
                break;
        }
    }

    public OperationWithPriorityNew() {
        super();
    }

    public void installPriority(PriorityType priorityType) {
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

    public OperationWithPriorityNew(PriorityType priorityType) {
        super();
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

    public OperationWithPriorityNew(Operation operation, PriorityType priorityType){
        super(operation);
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


    public void setPriority() {
        priority.setPriority(this);
    }

    public long getPriority() {
        return priority.getPriority();
    }

    @Override
    public void fullClean() {
        super.fullClean();
        priority.clean();
    }
}
