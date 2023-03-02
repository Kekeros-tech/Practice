package com.company;

import java.time.Duration;

public class PriorityByDuration implements IPriority {
    private Duration priorityByDuration;

    public PriorityByDuration(){
        priorityByDuration = Duration.ZERO;
    }

    @Override
    public void setPriority(IOperation operationToSetPriority) {
        if(priorityByDuration != Duration.ZERO) {
            return;
        }
        priorityByDuration = operationToSetPriority.getInitDurationOfExecution();

        for(IOperation followingOperation: operationToSetPriority.getFollowingOperations()) {
            priorityByDuration = priorityByDuration.plusMinutes(((OperationWithPriorityNew) followingOperation).getPriority());
        }
    }

    @Override
    public long getPriority() {
        return priorityByDuration.toMinutes();
    }

    @Override
    public void clean() {
        priorityByDuration = Duration.ZERO;
    }
}