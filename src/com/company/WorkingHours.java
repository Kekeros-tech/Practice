package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkingHours {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    WorkingHours(LocalDateTime startTime, LocalDateTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    WorkingHours(String startTime, String endTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.startTime = LocalDateTime.parse(startTime,formatter);
        this.endTime = LocalDateTime.parse(endTime,formatter);
    }

    WorkingHours(String endTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.parse(endTime, formatter);
    }

    WorkingHours() {
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now();
    }

    public LocalDateTime getStartTime() { return startTime; }

    public LocalDateTime getEndTime() { return endTime; }

    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return startTime.format(formatter)+"--->"+ endTime.format(formatter);
    }

    public Duration toDuration() {
        Duration result = Duration.between(startTime, endTime);
        return result;
    }

    public int getDayOfStartDate(){
        return this.startTime.getDayOfMonth();
    }

    public boolean isWorkingTime(LocalDateTime currentDate) {
        if(!startTime.isAfter(currentDate) && !endTime.isBefore(currentDate)) {
            return true;
        }
        return false;
    }

    public boolean isReverseTime(LocalDateTime currentTime) {
        return false;
    }

}