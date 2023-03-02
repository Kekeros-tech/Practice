package com.company.results_of_algos;

import com.company.recourse.IStructuralUnitOfResource;

import java.time.Duration;

public class ResultOfRecourseBooking {
    private Duration durationOfBooking;
    private IStructuralUnitOfResource unitOfResource;

    public ResultOfRecourseBooking(Duration durationOfBooking, IStructuralUnitOfResource unitOfResource){
        this.durationOfBooking = durationOfBooking;
        this.unitOfResource = unitOfResource;
    }

    public Duration getDurationOfBooking() { return durationOfBooking; }
    public IStructuralUnitOfResource getUnitOfResource() { return unitOfResource; }
}
