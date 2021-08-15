package com.company;

import java.time.Duration;
import java.util.Comparator;

class WHComparatorBasedOnDuration implements Comparator<WorkingHours> {

    public int compare(WorkingHours a, WorkingHours b){
        Duration first = Duration.between(a.getStartTime(),a.getEndTime());
        Duration second = Duration.between(b.getStartTime(),b.getEndTime());
        return -first.compareTo(second);
    }
}