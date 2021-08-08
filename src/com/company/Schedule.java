package com.company;

public class Schedule {
    private WorkingHours[] workTime;

    Schedule(WorkingHours[] workTime){
        this.workTime = workTime;
    }

    Schedule(){
        this.workTime = new WorkingHours[7];
    }

    public void setWorkTime(WorkingHours[] workTime){
        this.workTime = workTime;
    }

    public void setWorkTimeOfCurrentDayOfWeek(short dayOfWeek, WorkingHours workingHours){
        workTime[dayOfWeek] = workingHours;
    }

    public WorkingHours[] getWorkTime() {
        return workTime;
    }

    public WorkingHours getWorkTimeOfCurrentDayOfWeek(short value) {
        return workTime[value];
    }
}
