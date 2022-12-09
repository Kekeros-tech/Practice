package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface IResource {
    //Задать через правила работы станка 5-2 2-2 и тд
    void fillScheduleUsingPreviousData(LocalDateTime requiredDate);
    void addSchedule(WorkingHours currentWorkingHours);
    Duration putOperationOnResource(Operation operation);
    Duration putReverseOperationOnResource(Operation operation);
    LocalDateTime getStartDateAfterReleaseDate(LocalDateTime tactTime, Operation operation);
    LocalDateTime getReverseStartDateAfterTactTime(LocalDateTime tackTime, OperationWithPriorityNew operation);
    void setReleaseTime(LocalDateTime releaseTime);
    void clean();
}
