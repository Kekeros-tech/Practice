package com.company.priority;

import com.company.operation.IOperation;
import com.company.operation.O_Priority;

import java.time.Duration;

public class PriorityByLastStartTime implements IPriority {
    Duration priority;

    public PriorityByLastStartTime() {
        priority = Duration.ZERO;
    }

    @Override
    public void setPriority(IOperation operation) {
        if(((O_Priority) operation).getCLateStartTime() != null && operation.getTactTime() != null) {
            priority = Duration.between(operation.getTactTime(), ((O_Priority) operation).getCLateStartTime());
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
