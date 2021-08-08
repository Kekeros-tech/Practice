package com.company;
import java.util.ArrayList;

public class Series {
    private ArrayList<Operation> operationsToCreate;
    private Date deadlineForCompletion;
    private Date arrivalTime;


    public Series(ArrayList<Operation> operationsToCreate, Date deadlineForCompletion, Date arrivalTime)
    {
        this.operationsToCreate = operationsToCreate;
        this.deadlineForCompletion = deadlineForCompletion;
        this.arrivalTime = arrivalTime;
    }

    public ArrayList<Operation> getOperationsToCreate() { return operationsToCreate; }

    public Date getDeadlineForCompletion() { return deadlineForCompletion; }

    public Date getArrivalTime() { return arrivalTime; }

    public void setArrivalTime(Date arrivalTime) { this.arrivalTime = arrivalTime; }

    public void setOperationsToCreate(ArrayList<Operation> operationsToCreate) { this.operationsToCreate = operationsToCreate; }

    public void setDeadlineForCompletion(Date deadlineForCompletion) { this.deadlineForCompletion = deadlineForCompletion; }
}
