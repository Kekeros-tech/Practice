package com.company;

import java.time.Duration;

enum TypeOfWorkByDay {
    fiveByTwo,
    twoByTwo;
}

public class Schedule {
    private TypeOfWorkByDay typeOfWork;
    private String startTime;
    private String endTime;
    private String[] startTimeOfBreaks;
    private Duration[] durationOfBreaks;

    Schedule(TypeOfWorkByDay typeOfWork, String startTime, String endTime) {
        this.typeOfWork = typeOfWork;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    Schedule(TypeOfWorkByDay typeOfWorkByDay, String startTime, String endTime, String[] startTimeOfBreaks, Duration[] durationOfBreaks) {
        this(typeOfWorkByDay, startTime, endTime);
        String[] newStartTimeOfBreaks = new String[durationOfBreaks.length];
        for(int i = 0, k = 0; i < durationOfBreaks.length; i++){
            if(durationOfBreaks[i].isZero()){
                newStartTimeOfBreaks[i] = endTime;
            }
            else {
                newStartTimeOfBreaks[i] = startTimeOfBreaks[k];
                k++;
            }
        }
        this.startTimeOfBreaks = newStartTimeOfBreaks;
        this.durationOfBreaks = durationOfBreaks;
    }

    public TypeOfWorkByDay getTypeOfWork() {
        return typeOfWork;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String[] getStartTimeOfBreaks() {
        return startTimeOfBreaks;
    }

    public Duration[] getDurationOfBreaks() {
        return durationOfBreaks;
    }
}
