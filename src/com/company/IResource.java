package com.company;

import java.time.LocalDateTime;

public interface IResource {
    //Задать через правила работы станка 5-2 2-2 и тд
    void fillScheduleUsingPreviousData(LocalDateTime requiredDate);
    LocalDateTime takeWhichCanNotBeInterrupted(Operation operation);
    LocalDateTime takeWhichCanBeInterrupted(Operation operation);
    boolean isTactDateWorkingTime(LocalDateTime tactTime, Operation operation);
    LocalDateTime getStartDateAfterReleaseDate(LocalDateTime tactTime, Operation operation);
    void clean();
}
