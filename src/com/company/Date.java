package com.company;

import java.time.format.DateTimeFormatter;
import java.time.*;

public class Date {
    private LocalDateTime date;

    Date(int day,int month, int year,int hour, int minute) {
        this.date = LocalDateTime.of(year,month,day,hour,minute);
    }

    Date() {
        this.date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Duration getDurationBetweenToDates(Date deductibleDate) { // возвращает промежуток между двумя датами
        Duration duration = Duration.between(deductibleDate.date,this.date);
        System.out.println(duration.toMillis());
        return duration;
    }

    /*public WorkingHours getWorkingHoursBetweenToDates(Date deductibleDate) {
        Duration duration;
        if(this.date.isAfter(deductibleDate.date))
            duration = Duration.between(deductibleDate.date, this.date);
        else
            duration = Duration.between(this.date, deductibleDate.date);
        WorkingHours clock = new WorkingHours((int)duration.toMinutes() / 60,(int)duration.toMinutes() % 60);
        return clock;
    }*/

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.date.format(formatter);
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
