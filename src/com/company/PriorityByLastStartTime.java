package com.company;

import java.time.Duration;

public class PriorityByLastStartTime implements IPriority{
    Duration priority;

    public PriorityByLastStartTime() {
        priority = Duration.ZERO;
    }

    @Override
    public void setPriority(Operation operation) {
        if(operation.getCLateStartTime() != null && operation.getTactTime() != null){
            priority = Duration.between(operation.getTactTime(), operation.getCLateStartTime());
        }
    }

    @Override
    public long getPriority() {
        return -priority.toMinutes();
    }

    @Override
    public void clean() {
        priority = Duration.ZERO;
    }
}
