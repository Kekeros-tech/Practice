package com.company;

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

        Collection<IOperation> operationsToInstall = mergeOperations();

        while(!isAllOperationsInstall(operationsToInstall)) {

            ArrayList<IOperation> frontOfWork = choiceFrontOfWork(operationsToInstall);

            installOperations(frontOfWork);
        }
    }

    protected Collection<IOperation> mergeOperations() {
        ArrayList<IOperation> operationsToInstall = new ArrayList<>();
        for(Series series: seriesForWork) {
            operationsToInstall.addAll(series.getOperationsToCreate());
        }
        return operationsToInstall;
    }

    protected ArrayList<IOperation> choiceFrontOfWork(Collection<IOperation> operationsToCreate) {
        Collection<IOperation> frontOfWorkByPrevious = choiceCollectionOfOperationsWhichСanBePlacedInFront(operationsToCreate);

        LocalDateTime minTime = findMinTactTime(frontOfWorkByPrevious);

        ArrayList<IOperation> frontOfWork = new ArrayList<>();

        for (IOperation currentOperation: frontOfWorkByPrevious) {
            if(!currentOperation.getTactTime().isAfter(minTime)) {
                frontOfWork.add(currentOperation);
            }
        }

        return frontOfWork;
    }

    protected Collection<IOperation> choiceCollectionOfOperationsWhichСanBePlacedInFront(Collection<IOperation> operationCollection){
        Collection<IOperation> frontOfWork = new ArrayList<>();

        for(IOperation operation: operationCollection) {
            if(operation.isСanBePlacedInFront()) {
                operation.setTactTime();
                frontOfWork.add(operation);
            }
        }

        return frontOfWork;
    }

    protected void installOperations(Collection<IOperation> operationsToInstall) {
        installOperationsAndReturnFutureDate(operationsToInstall);
    }

    protected LocalDateTime findMinTactTime(Collection<IOperation> operationsToSelect){
        LocalDateTime minTime = LocalDateTime.MAX;
        for(IOperation operation: operationsToSelect) {
            LocalDateTime tactTimeOfCurrentOperation = operation.getTactTime();
            if(tactTimeOfCurrentOperation != null && tactTimeOfCurrentOperation.isBefore(minTime)){
                minTime = tactTimeOfCurrentOperation;
            }
        }
        return minTime;
    }

    protected void installOperationsAndReturnFutureDate(Collection<IOperation> frontOfWork) {

        for(IOperation operation: frontOfWork) {
            operation.installOperation();
        }

        for(Series series: seriesForWork) {
            for(IOperation operation: series.getOperationsToCreate()){
                if(operation.operationNotScheduled() && operation.getTactTime() != null) {
                    operation.setNewTactTime();
                }
            }
        }
    }

    protected boolean isAllOperationsInstall(Collection<IOperation> operationsToCreate){
        for(IOperation currentOperation: operationsToCreate){
            if(currentOperation.operationNotScheduled()){
                return false;
            }
        }
        return true;
    }
}
