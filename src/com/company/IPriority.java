package com.company;

public interface IPriority {
    void setPriority(IOperation operationToSetPriority);
    long getPriority();
    void clean();
}

enum PriorityType {
    priorityByDurationBetweenEarliestStartTimeAndLatestStartTime,
    priorityByLastStartTime,
    priorityByHeirs,
    priorityByDuration;
}




