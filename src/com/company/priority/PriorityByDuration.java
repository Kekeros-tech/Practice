package com.company.priority;

import com.company.operation.IOperation;
import com.company.operation.O_Priority;

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
            priorityByDuration = priorityByDuration.plusMinutes(((O_Priority) followingOperation).getPriority());
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
