package com.company.control_param;

import com.company.priority.PriorityType;

public class ControlParameters {

    public PriorityType sortOperator;
    public SequenceOfOperations sequenceOfOperations;
    public UseAdvancedSorting useAdvancedSorting;

    public ControlParameters(int numberOfSortOperator, int numberOfSequenceOfOperation, int numberOfAdvancedSorting) {
        switch (numberOfSortOperator) {
            case 0:
                sortOperator = PriorityType.priorityByHeirs;
                break;
            case 1:
                sortOperator = PriorityType.priorityByDuration;
                break;
            case 2:
                sortOperator = PriorityType.priorityByLastStartTime;
                break;
            case 3:
                sortOperator = PriorityType.priorityByDurationBetweenEarliestStartTimeAndLatestStartTime;
        }

        switch (numberOfSequenceOfOperation) {
            case 0:
                sequenceOfOperations = SequenceOfOperations.successively;
                break;
            case 1:
                sequenceOfOperations = SequenceOfOperations.together;
                break;
        }

        switch (numberOfAdvancedSorting) {
            case 0:
                useAdvancedSorting = UseAdvancedSorting.doNotUse;
                break;
            case 1:
                useAdvancedSorting = UseAdvancedSorting.use;
                break;
        }
    }
}