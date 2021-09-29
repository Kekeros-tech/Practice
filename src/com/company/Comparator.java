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

    class WHComparatorBasedOnDate implements Comparator<WorkingHours> {

        public int compare(WorkingHours a, WorkingHours b){
            if(a.getEndTime().isBefore(b.getStartTime())){
                return -1;
            }
            else{
                return 1;
            }
        }
    }

    class OComparatorBasedOnLateStartTime implements Comparator<Operation> {

        public int compare(Operation a, Operation b) {
            if(a.getCLateStartTime().isBefore(b.getCLateStartTime())) {
                return -1;
            }
            return 1;
        }
    }

    class OComparatorBasedOnWorkingInterval implements Comparator<Operation> {

        public int compare(Operation a, Operation b) {
            if(a.getCWorkingInterval().getStartTime().isBefore(b.getCWorkingInterval().getStartTime())) {
                return -1;
            }
            return 1;
        }
    }