package com.company;

import java.time.LocalDateTime;

public class ResultOfCurrentCellLoad {
    private int numberOfDetails;
    private WorkingHours workingTime;

    ResultOfCurrentCellLoad(int numberOfDetails, WorkingHours workingTime) {
        this.numberOfDetails = numberOfDetails;
        this.workingTime = workingTime;
    }
    public int getNumberOfDetails() { return numberOfDetails; }

    public WorkingHours getReleaseTime() { return workingTime; }
}
