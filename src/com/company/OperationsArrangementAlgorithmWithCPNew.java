package com.company;

import java.time.LocalDateTime;
import java.util.*;

public class OperationsArrangementAlgorithmWithCPNew extends  OperationsArrangementAlgorithm{
    ControlParameters controlParameters;

    public OperationsArrangementAlgorithmWithCPNew(Collection<Series> seriesForWork, ControlParameters controlParameters) {
        super(seriesForWork);
        this.controlParameters = controlParameters;
    }

    public OperationsArrangementAlgorithmWithCPNew() {
        super();
        this.controlParameters = null;
    }

    public void setSortMethodToOperations() {
        for(Series series: seriesForWork) {
            for(Operation operation: series.getOperationsToCreate()) {
                ((OperationWithPriorityNew) operation).installPriority(controlParameters.sortOperator);
            }
        }
    }

    public Collection<Series> prepareOperationsForInstallation() {
        setSortMethodToOperations();

        ArrayList<Series> workingSeries = new ArrayList<>(seriesForWork);

        installOperationsForwardAndReverse();

        if(controlParameters.sequenceOfOperations == SequenceOfOperations.together) {

            Collection<Operation> operationToInstall = mergeOperations();

            Series newCompleteSeries = new Series(operationToInstall, findEarliestArrivalTime(seriesForWork), findLatestDeadlineTime(seriesForWork));
            workingSeries.clear();
            workingSeries.add(newCompleteSeries);
        }

        return workingSeries;
    }

    protected void installOperationsForwardAndReverse() {
        for(Series series: seriesForWork) {
            installOperationsUntilDeadline(series);
            series.clean();

            installReverseOperationsUntilDeadline(series);
            series.clean();
        }
    }

    protected void installReverseOperationsUntilDeadline(Series currentSeries){

        while (!currentSeries.allOperationsAssigned()) {
            Collection<Operation> frontOfWork = choiceReverseFrontOfWork(currentSeries.getOperationsToCreate());

            installReverseOperationsAndReturnFutureDate(frontOfWork);
        }
    }

    protected ArrayList<Operation> choiceReverseFrontOfWork(ArrayList<Operation> operationsToCreate) {
        ArrayList<Operation> frontOfWork = new ArrayList<>();

        for(Operation operation: operationsToCreate) {
            if(operation.getCLateStartTime() == null && operation.allPreviousAssignedReverse() && operation.allFollowingAssignedReverse()) {
                frontOfWork.add(operation);
            }
        }
        return frontOfWork;
    }

    protected void installReverseOperationsAndReturnFutureDate (Collection<Operation> frontOfWork) {
        for(Operation operation: frontOfWork) {
            operation.getLatestEndTimeOfFollowing();

            if(controlParameters.sortOperator == PriorityType.priorityByDuration || controlParameters.sortOperator == PriorityType.priorityByHeirs){
                operation.setPriority();
            }

            operation.installReverseOperation();
        }
    }

    protected void installOperationsUntilDeadline(Series currentSeries) {

        while (!currentSeries.allOperationsAssigned()) {

            ArrayList <Operation> frontOfWork = choiceFrontOfWork(currentSeries.getOperationsToCreate());

            installOperationsAndReturnFutureDate(frontOfWork);
        }
    }

    @Override
    public void takeSeriesToWork() {
        Collection<Series> workingListOfSeries = prepareOperationsForInstallation();

        for(Series series: workingListOfSeries) {
            installOperations(series.getOperationsToCreate());
        }
    }

    @Override
    protected void installOperations(Collection<Operation> operationsToInstall) {

        while (!isAllOperationsInstall(operationsToInstall)) {

            ArrayList<Operation> frontOfWork = choiceFrontOfWork(operationsToInstall);

            sortFrontOfWorkByControlParameters(frontOfWork);

            advancedSorting(frontOfWork, controlParameters.useAdvancedSorting);

            installOperationsAndReturnFutureDate(frontOfWork);
        }
    }

    @Override
    protected void installOperationsAndReturnFutureDate(Collection<Operation> frontOfWork) {

        HashMap<Operation, IResource> installationSequence = MaximumFlowSolution.solveMaximumFlowProblem(frontOfWork);

        if(installationSequence != null) {
            for(Map.Entry<Operation, IResource> entry : installationSequence.entrySet()){
                entry.getKey().installOperationForSpecificResource(entry.getValue());
            }
        }

        for(Operation operation: frontOfWork){
            if(operation.operationNotScheduled()){
                operation.setNewTactTime();
            }
        }
    }

    protected void sortFrontOfWorkByControlParameters(ArrayList<Operation> frontOfWork) {
        for(Operation operation: frontOfWork) {
            operation.setPriority();
        }

        Comparator<Operation> sorter = new OPriorityComparator();
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
}
