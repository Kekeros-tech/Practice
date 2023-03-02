package com.company.results_of_algos;

import com.company.WorkingHours;

import java.time.LocalDateTime;

public class ResultOfCurrentCellLoad {
    private int numberOfDetails;
    private WorkingHours workingTime;

    public ResultOfCurrentCellLoad(int numberOfDetails, WorkingHours workingTime) {
        this.numberOfDetails = numberOfDetails;
        this.workingTime = workingTime;
    }
    public int getNumberOfDetails() { return numberOfDetails; }

    public WorkingHours getReleaseTime() { return workingTime; }
}
