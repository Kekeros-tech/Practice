package com.company;

import java.util.ArrayList;
import java.util.Collection;

public class Schedule {
    private ArrayList<WorkingHours> workTime;

    Schedule(Collection<WorkingHours> workTime){
        this.workTime = new ArrayList<>(workTime);
    }

    Schedule(){
        this.workTime = new ArrayList<WorkingHours>();
    }

    public void setWorkTime(Collection<WorkingHours> workTime){
        this.workTime = new ArrayList<>(workTime);
    }

    public void setWorkTimeOfCurrentDayOfWeek(short dayOfWeek, WorkingHours hours){
        workTime.set(dayOfWeek,hours);
    }

    public ArrayList<WorkingHours> getWorkTime() {
        return workTime;
    }

    public WorkingHours getWorkTimeOfCurrentDayOfWeek(short value) {
        return workTime.get(value);
    }
}
