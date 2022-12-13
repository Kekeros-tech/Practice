package com.company;

import java.time.Duration;

public class PriorityByLastStartTime implements IPriority{
    Duration priority;

    public PriorityByLastStartTime() {
        priority = Duration.ZERO;
    }

    @Override
    public void setPriority(IOperation operation) {
        if(((OperationWithPriorityNew) operation).getCLateStartTime() != null && operation.getTactTime() != null) {
            priority = Duration.between(operation.getTactTime(), ((OperationWithPriorityNew) operation).getCLateStartTime());
        }
    }

    @Override
    public long getPriority() {
        return priority.toMinutes();
    }

    @Override
    public void clean() {
        priority = Duration.ZERO;
    }
}
