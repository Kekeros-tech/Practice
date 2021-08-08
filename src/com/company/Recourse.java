package com.company;

public class Recourse {
    private Schedule schedule;
    private Date releaseDate; // когда станок освободится, может тут тоже Date, а то так получается сколько станок ещё будет работать

    public Recourse(Schedule schedule, Date releaseDate)
    {
        this.schedule = schedule;
        this.releaseDate = releaseDate;
    }

    public Recourse(Schedule schedule)
    {
        this.schedule = schedule;
        this.releaseDate = new Date();
    }

    public Schedule getSchedule() { return this.schedule; }

    public Date getReleaseTime() { return this.releaseDate; }

    public void setSchedule(Schedule schedule) { this.schedule = schedule; }

    public void setReleaseTime(Date releaseDate) { this.releaseDate = releaseDate; }

    public void takeToRecourse(Operation operation) {
        releaseDate.setDate(releaseDate.getDate().plusHours(operation.getDurationOfExecution().getHours()));
        releaseDate.setDate(releaseDate.getDate().plusMinutes(operation.getDurationOfExecution().getMinute()));
    }
}
