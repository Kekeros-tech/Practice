package com.company;

public class Series {
    private Operation[] operationsToCreate;
    private int deadlineForCompletion;
    private int arrivalTime;


    public Series(Operation[] operationsToCreate, int deadlineForCompletion, int arrivalTime)
    {
        this.operationsToCreate = operationsToCreate;
        this.deadlineForCompletion = deadlineForCompletion;
        this.arrivalTime = arrivalTime;
    }

    public Series(Operation[] operationsToCreate, int deadlineForCompletion)
    {
        this(operationsToCreate,deadlineForCompletion,0);
    }

    public Operation[] getOperationsToCreate() { return operationsToCreate; }

    public int getDeadlineForCompletion() { return deadlineForCompletion; }

    public int getArrivalTime() { return arrivalTime; }

    public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }

    public void setOperationsToCreate(Operation[] operationsToCreate) { this.operationsToCreate = operationsToCreate; }

    public void setDeadlineForCompletion(int deadlineForCompletion) { this.deadlineForCompletion = deadlineForCompletion; }
}
