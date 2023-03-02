package com.company.arrangement_algo;

import com.company.*;
import com.company.comparator.OExtendedComparator;
import com.company.comparator.OPriorityComparator;
import com.company.control_param.ControlParameters;
import com.company.control_param.SequenceOfOperations;
import com.company.control_param.UseAdvancedSorting;
import com.company.operation.IOperation;
import com.company.operation.O_Priority;
import com.company.priority.PriorityType;
import com.company.results_of_algos.ResultOfMaximumFlowSolution;

import java.time.LocalDateTime;
import java.util.*;

public class Algo_WithCP extends Algo_Basic {
    public ControlParameters controlParameters;

    public Algo_WithCP(Collection<Series> seriesForWork, ControlParameters controlParameters) {
        super(seriesForWork);
        this.controlParameters = controlParameters;
    }

    public Algo_WithCP() {
        super();
        this.controlParameters = null;
    }

    public void setSortMethodToOperations() {
        for(Series series: seriesForWork) {
            for(IOperation operation: series.getOperationsToCreate()) {
                ((O_Priority) operation).installPriority(controlParameters.sortOperator);
            }
        }
    }

    public Collection<Series> prepareOperationsForInstallation() {
        setSortMethodToOperations();

        ArrayList<Series> workingSeries = new ArrayList<>(seriesForWork);

        installOperationsForwardAndReverse();

        if(controlParameters.sequenceOfOperations == SequenceOfOperations.together) {

            Collection<IOperation> operationToInstall = mergeOperations();

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
            Collection<IOperation> frontOfWork = choiceReverseFrontOfWork(currentSeries.getOperationsToCreate());

            installReverseOperationsAndReturnFutureDate(frontOfWork);
        }
    }

    protected Collection<IOperation> choiceReverseCollectionOfOperationsWhichСanBePlacedInFront(Collection<IOperation> operationCollection) {
        ArrayList<IOperation> frontOfWork = new ArrayList<>();

        for(IOperation operation: operationCollection) {
            if(operation.isCanBePlacedInReverseFront()) {
                ((O_Priority) operation).setReverseTactTime();
                frontOfWork.add(operation);
            }
        }
        return frontOfWork;
    }

    private LocalDateTime findMaxTactTime(Collection<IOperation> operations) {
        LocalDateTime maxTime = LocalDateTime.MIN;
        for(IOperation currentOperation: operations) {
            if(currentOperation.getTactTime().isAfter(maxTime)){
                maxTime = currentOperation.getTactTime();
            }
        }
        return maxTime;
    }

    protected ArrayList<IOperation> choiceReverseFrontOfWork(ArrayList<IOperation> operationsToCreate) {
        Collection<IOperation> frontOfWorkByFollowing = choiceReverseCollectionOfOperationsWhichСanBePlacedInFront(operationsToCreate);

        LocalDateTime minTime = findMaxTactTime(frontOfWorkByFollowing);

        ArrayList<IOperation> frontOfWork = new ArrayList<>();

        for (IOperation currentOperation: frontOfWorkByFollowing) {
            if(currentOperation.getTactTime().isEqual(minTime)) {
                frontOfWork.add(currentOperation);
            }
        }

        return frontOfWork;
    }

    protected void installReverseOperationsAndReturnFutureDate (Collection<IOperation> frontOfWork) {

        for(IOperation operation: frontOfWork) {

            if(controlParameters.sortOperator == PriorityType.priorityByDuration || controlParameters.sortOperator == PriorityType.priorityByHeirs){
                ((O_Priority) operation).setPriority();
            }

            operation.installReverseOperation();
        }

        for(IOperation operation: frontOfWork) {
            if(operation.operationNotScheduled()){
                ((O_Priority) operation).setNewReverseTactTime();
            }
        }
    }

    protected void installOperationsUntilDeadline(Series currentSeries) {

        while (!currentSeries.allOperationsAssigned()) {

            ArrayList <IOperation> frontOfWork = choiceFrontOfWork(currentSeries.getOperationsToCreate());

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
    protected void installOperations(Collection<IOperation> operationsToInstall) {

        while (!isAllOperationsInstall(operationsToInstall)) {

            ArrayList<IOperation> frontOfWork = choiceFrontOfWork(operationsToInstall);

            sortFrontOfWorkByControlParameters(frontOfWork);

            advancedSorting(frontOfWork, controlParameters.useAdvancedSorting);

            installOperationsAndReturnFutureDate(frontOfWork);
        }
    }

    @Override
    protected void installOperationsAndReturnFutureDate(Collection<IOperation> frontOfWork) {

        ArrayList<ResultOfMaximumFlowSolution> installationSequence = MaximumFlowSolution.solveMaximumFlowProblem(frontOfWork);

        if(installationSequence != null) {
            for(ResultOfMaximumFlowSolution currentResult: installationSequence) {
                currentResult.getOperationForInstalling().installOperationForSpecificResource(currentResult.getResourceForInstalling(),
                        currentResult.getAmountForInstalling());
            }
        }

        for(IOperation operation: frontOfWork) {
            if(operation.operationNotScheduled()) {
                operation.setNewTactTime();
            }
        }
    }

    protected void sortFrontOfWorkByControlParameters(ArrayList<IOperation> frontOfWork) {
        for(IOperation operation: frontOfWork) {
            ((O_Priority) operation).setPriority();
        }

        Comparator<IOperation> sorter = new OPriorityComparator();
        frontOfWork.sort(sorter);
    }

    protected void advancedSorting(ArrayList<IOperation> frontOfWork, UseAdvancedSorting sorting) {
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
