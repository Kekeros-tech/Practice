package com.company;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Series {
    private Collection<Operation> operationsToCreate;
    private LocalDateTime deadlineForCompletion;
    private LocalDateTime arrivalTime;


    Series(Collection<Operation> operationsToCreate, LocalDateTime deadlineForCompletion, LocalDateTime arrivalTime)
    {
        this.operationsToCreate = new ArrayList<>(operationsToCreate);
        this.deadlineForCompletion = deadlineForCompletion;
        this.arrivalTime = arrivalTime;
    }


    public Collection<Operation> getOperationsToCreate() { return operationsToCreate; }

    public LocalDateTime getDeadlineForCompletion() { return deadlineForCompletion; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }


    public void setOperationsToCreate(Collection<Operation> operationsToCreate) { this.operationsToCreate = new ArrayList<>(operationsToCreate); }

    public void setDeadlineForCompletion(LocalDateTime deadlineForCompletion) { this.deadlineForCompletion = deadlineForCompletion; }

    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }


    public void addOperationToCreate(Operation operation) { operationsToCreate.add(operation); }

    public void addOperationCollectionToCreate(Collection<Operation> operationsToCreate) { this.operationsToCreate.addAll(operationsToCreate); }

}
