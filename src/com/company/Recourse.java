package com.company;

import java.time.LocalDateTime;

public class Recourse {
    private Schedule schedule;
    private LocalDateTime releaseDate;

    Recourse(Schedule schedule, LocalDateTime releaseDate)
    {
        this.schedule = schedule;
        this.releaseDate = releaseDate;
    }

    Recourse(Schedule schedule)
    {
        this.schedule = schedule;
        this.releaseDate = LocalDateTime.now();
    }

    public Schedule getSchedule() { return this.schedule; }

    public LocalDateTime getReleaseTime() { return this.releaseDate; }

    public void setSchedule(Schedule schedule) { this.schedule = schedule; }

    public void setReleaseTime(LocalDateTime releaseDate) { this.releaseDate = releaseDate; }

    /*public void takeToRecourse(Operation operation) {
        releaseDate.setDate(releaseDate.getDate().plusHours(operation.getDurationOfExecution().getHours()));
        releaseDate.setDate(releaseDate.getDate().plusMinutes(operation.getDurationOfExecution().getMinute()));
    }*/
}
