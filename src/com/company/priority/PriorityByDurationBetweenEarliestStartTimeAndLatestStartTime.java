package com.company.priority;

import com.company.operation.IOperation;
import com.company.operation.O_Priority;

import java.time.Duration;

public class PriorityByDurationBetweenEarliestStartTimeAndLatestStartTime implements IPriority {
    Duration priority;

    public PriorityByDurationBetweenEarliestStartTimeAndLatestStartTime()
    {
        priority = Duration.ZERO;
    }

    @Override
    public void setPriority(IOperation operation)
    {
        if(((O_Priority) operation).getCEarlierStartTime() != null
                && ((O_Priority) operation).getCLateStartTime() != null) {
            priority = Duration.between(((O_Priority) operation).getCEarlierStartTime(),
                                        ((O_Priority) operation).getCLateStartTime());
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
