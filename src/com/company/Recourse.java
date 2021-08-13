package com.company;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Recourse {
    private Collection<WorkingHours> schedule;
    private LocalDateTime releaseDate;

    Recourse(Collection<WorkingHours> schedule, LocalDateTime releaseDate)
    {
        this.schedule = new ArrayList<>(schedule);
        this.releaseDate = releaseDate;
    }

    Recourse(Collection<WorkingHours> schedule)
    {
        this.schedule = new ArrayList<>(schedule);
        this.releaseDate = LocalDateTime.now();
    }


    public Collection<WorkingHours> getSchedule() { return this.schedule; }

    public LocalDateTime getReleaseTime() { return this.releaseDate; }


    public void setSchedule(Collection<WorkingHours> schedule) { this.schedule = new ArrayList<>(schedule); }

    public void setReleaseTime(LocalDateTime releaseDate) { this.releaseDate = releaseDate; }

}
