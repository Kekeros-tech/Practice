package com.company;

enum SortOperator {
    sortByPrioritiesByHeirs,
    sortByPrioritiesByDuration,
    sortByEarlyAndLateStartDates;
}

enum SequenceOfOperations {
    successively,
    together;
}

enum UseAdvancedSorting {
    doNotUse,
    use;
}

public class ControlParameters {

    SortOperator sortOperator;
    SequenceOfOperations sequenceOfOperations;
    UseAdvancedSorting useAdvancedSorting;

    ControlParameters(int numberOfSortOperator, int numberOfSequenceOfOperation, int numberOfAdvancedSorting) {
        switch (numberOfSortOperator) {
            case 0:
                sortOperator = SortOperator.sortByPrioritiesByHeirs;
                break;
            case 1:
                sortOperator = SortOperator.sortByPrioritiesByDuration;
                break;
            case 2:
                sortOperator = SortOperator.sortByEarlyAndLateStartDates;
                break;
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