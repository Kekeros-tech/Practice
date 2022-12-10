package com.company;

public class ResultOfOperationSetting {
    private IStructuralUnitOfResource resourceOfBooking;
    private WorkingHours workingInterval;

    ResultOfOperationSetting(IStructuralUnitOfResource resourceOfBooking, WorkingHours workingInterval) {
        this.resourceOfBooking = resourceOfBooking;
        this.workingInterval = workingInterval;
    }

    public IStructuralUnitOfResource getResourceOfBooking() { return resourceOfBooking; }
    public WorkingHours getWorkingInterval() { return workingInterval; }
}
