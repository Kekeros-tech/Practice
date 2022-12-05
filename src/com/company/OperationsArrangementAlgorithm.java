package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
//------------------
// Базовый алгоритм расстановки операций, без учета преоритетов операций и без просмотра будущих операций
//------------------
public class OperationsArrangementAlgorithm {
    Collection<Series> seriesForWork;

    public OperationsArrangementAlgorithm(Collection<Series> seriesForWork) {
        this.seriesForWork = seriesForWork;
    }

    public OperationsArrangementAlgorithm() {
        this.seriesForWork = null;
    }

    public void takeSeriesToWork() {

        Collection<Operation> operationsToInstall = mergeOperations();

        while(!isAllOperationsInstall(operationsToInstall)) {

            ArrayList<Operation> frontOfWork = choiceFrontOfWork(operationsToInstall);

            installOperations(frontOfWork);
        }
    }

    protected Collection<Operation> mergeOperations() {
        ArrayList<Operation> operationsToInstall = new ArrayList<>();
        for(Series series: seriesForWork) {
            operationsToInstall.addAll(series.getOperationsToCreate());
        }
        return operationsToInstall;
    }

    protected ArrayList<Operation> choiceFrontOfWork(Collection<Operation> operationsToCreate) {
        Collection<Operation> frontOfWorkByPrevious = choiceCollectionOfOperationsWhichСanBePlacedInFront(operationsToCreate);

        LocalDateTime minTime = findMinTactTime(frontOfWorkByPrevious);

        ArrayList<Operation> frontOfWork = new ArrayList<>();

        for (Operation currentOperation: frontOfWorkByPrevious) {
            if(!currentOperation.getTactTime().isAfter(minTime)) {
                frontOfWork.add(currentOperation);
            }
        }

        return frontOfWork;
    }

    protected Collection<Operation> choiceCollectionOfOperationsWhichСanBePlacedInFront(Collection<Operation> operationCollection){
        Collection<Operation> frontOfWork = new ArrayList<>();

        for(Operation operation: operationCollection) {
            if(operation.getCNumberOfAssignedRecourse() == null && operation.allPreviousAssigned()){
                operation.getLatestEndTimeOfPrevious();
                frontOfWork.add(operation);
            }
        }

        return frontOfWork;
    }

    protected void installOperations(Collection<Operation> operationsToInstall) {
        installOperationsAndReturnFutureDate(operationsToInstall);
    }

    protected LocalDateTime findMinTactTime(Collection<Operation> operationsToSelect){
        LocalDateTime minTime = LocalDateTime.MAX;
        for(Operation operation: operationsToSelect) {
            LocalDateTime tactTimeOfCurrentOperation = operation.getTactTime();
            if(tactTimeOfCurrentOperation != null && tactTimeOfCurrentOperation.isBefore(minTime)){
                minTime = tactTimeOfCurrentOperation;
            }
        }
        return minTime;
    }

    protected void installOperationsAndReturnFutureDate(Collection<Operation> frontOfWork) {

        for(Operation operation: frontOfWork) {
            operation.installOperation();
        }

        for(Series series: seriesForWork) {
            for(Operation operation: series.getOperationsToCreate()){
                if(operation.operationNotScheduled() && operation.tactTime != null) {
                    operation.setNewTactTime();
                }
            }
        }
    }

    protected boolean isAllOperationsInstall(Collection<Operation> operationsToCreate){
        for(Operation currentOperation: operationsToCreate){
            if(currentOperation.operationNotScheduled()){
                return false;
            }
        }
        return true;
    }
}
