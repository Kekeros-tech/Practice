package com.company;

import java.time.LocalDateTime;

public class WorkingHours {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    WorkingHours(LocalDateTime startTime, LocalDateTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    WorkingHours() {
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now();
    }

    /*public boolean haveSameDayOfStartTime(WorkingHours another){
        System.out.println(this.startTime.getDayOfMonth() == another.startTime.getDayOfMonth());
        return (this.startTime.getDayOfMonth() == another.startTime.getDayOfMonth());
    }*/

    public LocalDateTime getStartTime() { return startTime; }

    public LocalDateTime getEndTime() { return endTime; }

    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}