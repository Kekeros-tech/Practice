package com.company;

public class Series {
    private Operation[] operations_to_create;
    private int deadline_for_completion;
    private int arrival_time;

    public Series(Operation[] operations, int deadline, int arrival)
    {
        operations_to_create = operations;
        arrival_time = arrival;
        deadline_for_completion = deadline;
    }

    public Series(Operation[] operations, int deadline)
    {
        this(operations,deadline,0);
    }
}
