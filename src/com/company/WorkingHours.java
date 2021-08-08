package com.company;

public class WorkingHours {
    private int hours;
    private int minute;

    WorkingHours(int hours, int minute){
        this.hours = hours;
        this.minute = minute;
    }

    WorkingHours(int hours){
        this(hours, 0);
    }

    WorkingHours(){
        this(0);
    }

    public WorkingHours convertDateToWorkingHours(int day,int hours,int minute) { // разобраться с этим
        WorkingHours workingHours = new WorkingHours(day*24+hours,minute);
        return workingHours;
    }

    public int getHours() {
        return hours;
    }

    public int getMinute() {
        return minute;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}