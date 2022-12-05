package com.company;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/*public class OperationsArrangementAlgorithmWithCP extends OperationsArrangementAlgorithm {
    ControlParameters controlParameters;

    public OperationsArrangementAlgorithmWithCP(Collection<Series> seriesForWork, ControlParameters controlParameters) {
        super(seriesForWork);
        this.controlParameters = controlParameters;
    }

    public OperationsArrangementAlgorithmWithCP(){
        super();
        this.controlParameters = null;
    }

    @Override
    public void takeSeriesToWork() {

        installOperationsForwardAndReverse();

        if(controlParameters.sequenceOfOperations == SequenceOfOperations.together) {

            Collection<Operation> operationToInstall = mergeOperations();

            Series newCompleteSeries = new Series(operationToInstall, findEarliestArrivalTime(seriesForWork), findLatestDeadlineTime(seriesForWork));
            seriesForWork.clear();
            seriesForWork.add(newCompleteSeries);
        }

        for(Series series: seriesForWork) {
            installOperations(series.getOperationsToCreate());
        }
    }

    @Override
    protected void installOperations(Collection<Operation> operationsToInstall) {

        while (!isAllOperationsInstall(operationsToInstall)) {

            ArrayList <Operation> frontOfWork = choiceFrontOfWork(operationsToInstall);

            sortFrontOfWorkByControlParameters(frontOfWork, controlParameters.sortOperator);

            advancedSorting(frontOfWork, controlParameters.useAdvancedSorting);

            installOperationsAndReturnFutureDate(frontOfWork);
        }
    }

    protected void sortFrontOfWorkByControlParameters(ArrayList<Operation> frontOfWork, SortOperator sortOperator){
        Comparator<Operation> sorter = new OComparatorBasedOnLateStartTime();
        switch (sortOperator) {
            case sortByPrioritiesByHeirs:
                sorter = new OComparatorBasedOnPrioritiesByHeirs();
                break;
            case sortByPrioritiesByDuration:
                sorter = new OComparatorBasedOnPrioritiesByDuration();
                break;
            case sortByEarlyAndLateStartDates:
                break;
        }
        frontOfWork.sort(sorter);
    }

    protected void advancedSorting(ArrayList<Operation> frontOfWork, UseAdvancedSorting sorting) {
        switch (sorting) {
            case doNotUse:
                break;
            case use:
                OExtendedComparator sorter = new OExtendedComparator();
                frontOfWork.sort(sorter);
                break;
        }
    }

    public static LocalDateTime findEarliestArrivalTime(Collection<Series> seriesForWork){
        LocalDateTime earliestTime = LocalDateTime.MAX;
        for(Series series: seriesForWork){
            if(series.getArrivalTime().isBefore(earliestTime)){
                earliestTime = series.getArrivalTime();
            }
        }
        return earliestTime;
    }

    public static LocalDateTime findLatestDeadlineTime(Collection<Series> seriesForWork) {
        LocalDateTime latestTime = LocalDateTime.MAX;
        for(Series series: seriesForWork){
            if(series.getArrivalTime().isAfter(latestTime)){
                latestTime = series.getArrivalTime();
            }
        }
        return latestTime;
    }
}*/
