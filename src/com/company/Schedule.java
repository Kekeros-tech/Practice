package com.company;// может быть использовать Map, чтобы осуществить привязку к дням недели

public class Schedule {
    WorkingHours[] workTime;

    Schedule(WorkingHours[] workTime){
        this.workTime = workTime;
    }

    Schedule(){
        this.workTime = new WorkingHours[7];
    }
}
