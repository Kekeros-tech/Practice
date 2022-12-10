package com.company;

import java.time.Duration;

public class ResultOfRecourseBooking {
    private Duration durationOfBooking;
    private IStructuralUnitOfResource unitOfResource;

    ResultOfRecourseBooking(Duration durationOfBooking, IStructuralUnitOfResource unitOfResource){
        this.durationOfBooking = durationOfBooking;
        this.unitOfResource = unitOfResource;
    }

    public Duration getDurationOfBooking() { return durationOfBooking; }
    public IStructuralUnitOfResource getUnitOfResource() { return unitOfResource; }
}
