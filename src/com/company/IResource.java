package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface IResource {
    //Задать через правила работы станка 5-2 2-2 и тд
    void fillScheduleUsingPreviousData(LocalDateTime requiredDate);
    void addSchedule(WorkingHours currentWorkingHours);
    LocalDateTime takeWhichCanNotBeInterrupted(Operation operation);
    LocalDateTime takeWhichCanBeInterrupted(Operation operation);
    boolean isTactDateWorkingTime(LocalDateTime tactTime, Operation operation);
    LocalDateTime getStartDateAfterReleaseDate(LocalDateTime tactTime, Operation operation);
    boolean takeResWhichCanNotBeInterrupted(Operation operation);
    boolean takeResWhichCanBeInterrupted(Operation operation);
    ArrayList<WorkingHours> getSchedule();
    LocalDateTime takeReverseWhichCanBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate, LocalDateTime maxStartTime);
    LocalDateTime tackReverseWhichCanNotBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate, LocalDateTime maxStartTime);

    LocalDateTime getReleaseTime();
    void setReleaseTime(LocalDateTime releaseTime);
    void clean();
}
