package com.company;

public class WorkingHours {
    byte hours;
    byte minute;

    WorkingHours(byte hours, byte minute){
        this.hours = hours;
        this.minute = minute;
    }

    WorkingHours(byte hours){
        this(hours, (byte)0);
    }

    WorkingHours(){
        this((byte) 0);
    }
}