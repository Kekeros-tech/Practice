package com.company;

public class Operation {
    //private recources_in_use: *Group(Ссылка на группу)
    private Series[] serial_affiliation;
    private Operation[] previous_operations;
    private Operation[] following_operations;
    private int duration_of_execution;
    private boolean operating_mode;

    public Operation(Series[] _serial_affiliation, Operation[] _previous_operations, Operation[] _following_operations,
                     int _duration_of_execution, boolean _operating_mode) {
        serial_affiliation = _serial_affiliation;
        previous_operations = _previous_operations;
        following_operations = _following_operations;
        duration_of_execution = _duration_of_execution;
        operating_mode = _operating_mode;
    }

    public Operation(Series[] i_serial_affiliation, Operation[] i_previous_operations,
                     Operation[] i_following_operations, int i_duration_of_execution) {
        this(i_serial_affiliation, i_previous_operations, i_following_operations, i_duration_of_execution, false);
    }
}
