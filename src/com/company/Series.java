package com.company;
import java.util.ArrayList;

public class Series {
    private ArrayList<Operation> operationsToCreate;
    private int deadlineForCompletion;//опять же тут проблема с датой, как это реализовать
    private int arrivalTime;


    public Series(ArrayList<Operation> operationsToCreate, int deadlineForCompletion, int arrivalTime)
    {
        this.operationsToCreate = operationsToCreate;
        this.deadlineForCompletion = deadlineForCompletion;
        this.arrivalTime = arrivalTime;
    }

    public Series(ArrayList<Operation> operationsToCreate, int deadlineForCompletion)
    {
        this(operationsToCreate,deadlineForCompletion,0);
    }

    public ArrayList<Operation> getOperationsToCreate() { return operationsToCreate; }

    public int getDeadlineForCompletion() { return deadlineForCompletion; }

    public int getArrivalTime() { return arrivalTime; }

    public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }

    public void setOperationsToCreate(ArrayList<Operation> operationsToCreate) { this.operationsToCreate = operationsToCreate; }

    public void setDeadlineForCompletion(int deadlineForCompletion) { this.deadlineForCompletion = deadlineForCompletion; }
}
