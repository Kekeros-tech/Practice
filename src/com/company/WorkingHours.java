package com.company;

import java.time.LocalDateTime;

public class WorkingHours {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    WorkingHours(LocalDateTime startTime, LocalDateTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    WorkingHours() {}

    public LocalDateTime getHours() { return startTime; }

    public LocalDateTime getMinute() { return endTime; }

    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}