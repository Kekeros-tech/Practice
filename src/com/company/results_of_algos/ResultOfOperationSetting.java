package com.company.results_of_algos;

import com.company.WorkingHours;
import com.company.recourse.IStructuralUnitOfResource;

public class ResultOfOperationSetting {
    private IStructuralUnitOfResource resourceOfBooking;
    private WorkingHours workingInterval;

    public ResultOfOperationSetting(IStructuralUnitOfResource resourceOfBooking, WorkingHours workingInterval) {
        this.resourceOfBooking = resourceOfBooking;
        this.workingInterval = workingInterval;
    }

    public IStructuralUnitOfResource getResourceOfBooking() { return resourceOfBooking; }
    public WorkingHours getWorkingInterval() { return workingInterval; }
}
