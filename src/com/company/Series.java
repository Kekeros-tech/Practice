package com.company;

public class Series {
    private Operation[] operations_to_create;
    private int arrival_time;
    private int deadline_for_completion;

    public Series(Operation[] input_data, int entrance_deadline_for_completion, int entrance_arrival_time)
    {
        operations_to_create = input_data;
        arrival_time = entrance_arrival_time;
        deadline_for_completion = entrance_deadline_for_completion;
    }

    public Series(Operation[] input_data, int entrance_deadline_for_completion)
    {
        this(input_data,entrance_deadline_for_completion,0);
    }
}
