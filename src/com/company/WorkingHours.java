package com.company;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkingHours {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    WorkingHours(LocalDateTime startTime, LocalDateTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    WorkingHours(String startTime, String endTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.startTime = LocalDateTime.parse(startTime,formatter);
        this.endTime = LocalDateTime.parse(endTime,formatter);
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

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return startTime.format(formatter)+"--->"+ endTime.format(formatter);
    }


}