package com.company;

public class Operation {
    //private recources_in_use: *Group(Ссылка на группу)
    private Series[] serial_affiliation;
    private Operation[] previous_operations;
    private Operation[] following_operations;
    private int duration_of_execution;
    private boolean operating_mode;

    public Operation(Series[] i_serial_affiliation, Operation[] i_previous_operations, Operation[] i_following_operations,
                     int i_duration_of_execution, boolean i_operating_mode) {
        serial_affiliation = i_serial_affiliation;
        previous_operations = i_previous_operations;
        following_operations = i_following_operations;
        duration_of_execution = i_duration_of_execution;
        operating_mode = i_operating_mode;
    }

    public Operation(Series[] i_serial_affiliation, Operation[] i_previous_operations,
                     Operation[] i_following_operations, int i_duration_of_execution) {
        this(i_serial_affiliation, i_previous_operations, i_following_operations, i_duration_of_execution, false);
    }
}
