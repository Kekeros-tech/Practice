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
            if(a.getCLateStartTime().isEqual(b.getCLateStartTime())) {
                return 0;
            }
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

    class OComparatorBasedOnPrioritiesByHeirs implements Comparator<Operation> {

        public int compare(Operation a, Operation b) {
            if(((OperationWithPrioritiesByHeirs) a).getPrioritiesByHeirs() == ((OperationWithPrioritiesByHeirs) b).getPrioritiesByHeirs()){
                return 0;
            }
            if(((OperationWithPrioritiesByHeirs) a).getPrioritiesByHeirs() > ((OperationWithPrioritiesByHeirs) b).getPrioritiesByHeirs())
                return -1;
            return 1;
        }
    }

    class OComparatorBasedOnPrioritiesByDuration implements Comparator<Operation> {

        public int compare(Operation a, Operation b) {
            if(((OperationWithPrioritiesByDuration) a).getPrioritiesByDuration().equals(((OperationWithPrioritiesByDuration) b).getPrioritiesByDuration())){
                return 0;
            }
            return -((OperationWithPrioritiesByDuration) a).getPrioritiesByDuration().compareTo(((OperationWithPrioritiesByDuration) b).getPrioritiesByDuration());
        }
    }

    class OExtendedComparator implements Comparator<Operation> {

        public int compare(Operation a, Operation b) {
            int resultOfSorter = Integer.MAX_VALUE;

            switch (a.getClass().getSimpleName()) {
                case "Operation":
                    OComparatorBasedOnLateStartTime sorter = new OComparatorBasedOnLateStartTime();
                    resultOfSorter = sorter.compare(a,b);
                    break;
                case "OperationWithPrioritiesByHeirs":
                    OComparatorBasedOnPrioritiesByHeirs sorter1 = new OComparatorBasedOnPrioritiesByHeirs();
                    resultOfSorter = sorter1.compare(a,b);
                    break;
                case  "OperationWithPrioritiesByDuration":
                    OComparatorBasedOnPrioritiesByDuration sorter2 = new OComparatorBasedOnPrioritiesByDuration();
                    resultOfSorter = sorter2.compare(a,b);
                    break;
            }

            if(resultOfSorter == 0) {
                if(a.getOperatingMode() == OperatingMode.canNotBeInterrupted && b.getOperatingMode() == OperatingMode.canBeInterrupted){
                    return -1;
                }
                if(a.getOperatingMode() == OperatingMode.canBeInterrupted && b.getOperatingMode() == OperatingMode.canNotBeInterrupted){
                    return 1;
                }
            }
            return 0;
        }
    }