package com.company;

import java.time.Duration;

public class PriorityByDurationBetweenEarliestStartTimeAndLatestStartTime implements IPriority{
    Duration priority;

    public PriorityByDurationBetweenEarliestStartTimeAndLatestStartTime()
    {
        priority = Duration.ZERO;
    }

    @Override
    public void setPriority(Operation operation)
    {
        if(operation.getCEarlierStartTime() != null && operation.getCLateStartTime() != null){
            priority = Duration.between(operation.getCEarlierStartTime(), operation.getCLateStartTime());
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
