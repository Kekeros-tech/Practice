package com.company.recourse;

import com.company.results_of_algos.ResultOfRecourseBooking;
import com.company.WorkingHours;
import com.company.operation.IOperation;

import java.time.LocalDateTime;

public interface IResource {
    //Задать через правила работы станка 5-2 2-2 и тд
    void fillScheduleUsingPreviousData(LocalDateTime requiredDate);
    void addSchedule(WorkingHours currentWorkingHours);
    ResultOfRecourseBooking putOperationOnResource(IOperation operation);
    ResultOfRecourseBooking putReverseOperationOnResource(IOperation operation);
    LocalDateTime getStartDateAfterReleaseDate(LocalDateTime tactTime, IOperation operation);
    LocalDateTime getReverseStartDateAfterTactTime(LocalDateTime tackTime, IOperation operation);
    void clean();
}
