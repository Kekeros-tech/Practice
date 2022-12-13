package com.company;

import java.time.Duration;

public class PriorityByDurationBetweenEarliestStartTimeAndLatestStartTime implements IPriority{
    Duration priority;

    public PriorityByDurationBetweenEarliestStartTimeAndLatestStartTime()
    {
        priority = Duration.ZERO;
    }

    @Override
    public void setPriority(IOperation operation)
    {
        if(((OperationWithPriorityNew) operation).getCEarlierStartTime() != null
                && ((OperationWithPriorityNew) operation).getCLateStartTime() != null) {
            priority = Duration.between(((OperationWithPriorityNew) operation).getCEarlierStartTime(),
                                        ((OperationWithPriorityNew) operation).getCLateStartTime());
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
