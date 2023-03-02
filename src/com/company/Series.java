package com.company;
import com.company.operation.IOperation;
import com.company.operation.O_Basic;

import java.time.LocalDateTime;
import java.util.*;

public class Series {
    private StringBuffer nameOfSeries;
    private ArrayList<IOperation> operationsToCreate;
    private LocalDateTime deadlineForCompletion;
    private LocalDateTime arrivalTime;

    private int cNumberOfAssignedOperations;


    public Series(Collection<IOperation> operationsToCreate, LocalDateTime deadlineForCompletion, LocalDateTime arrivalTime)
    {
        nameOfSeries = generateRandomHexString(8);
        this.operationsToCreate = new ArrayList<>(operationsToCreate);
        this.deadlineForCompletion = deadlineForCompletion;
        this.arrivalTime = arrivalTime;
    }

    Series(Collection<IOperation> operationsToCreate, String deadlineForCompletion, String arrivalTime)
    {
        this(operationsToCreate, LocalDateTime.parse(deadlineForCompletion, WorkingHours.formatter), LocalDateTime.parse(arrivalTime, WorkingHours.formatter));
    }

    Series(String deadlineForCompletion, String arrivalTime) {
        this.deadlineForCompletion = LocalDateTime.parse(deadlineForCompletion, WorkingHours.formatter);
        this.arrivalTime = LocalDateTime.parse(arrivalTime, WorkingHours.formatter);
    }

    Series(String nameOfOperation, Collection<IOperation> operationsToCreate, String deadlineForCompletion, String arrivalTime)
    {
        this(operationsToCreate, LocalDateTime.parse(deadlineForCompletion, WorkingHours.formatter), LocalDateTime.parse(arrivalTime, WorkingHours.formatter));
        this.nameOfSeries = new StringBuffer(nameOfOperation);
    }

    public StringBuffer getNameOfSeries() { return nameOfSeries; }

    public ArrayList<IOperation> getOperationsToCreate() { return operationsToCreate; }

    public LocalDateTime getDeadlineForCompletion() { return deadlineForCompletion; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }

    public int getСNumberOfAssignedOperations() { return cNumberOfAssignedOperations; }



    public void setOperationsToCreate(Collection<IOperation> operationsToCreate) { this.operationsToCreate = new ArrayList<>(operationsToCreate); }

    public void setDeadlineForCompletion(LocalDateTime deadlineForCompletion) { this.deadlineForCompletion = deadlineForCompletion; }

    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public void setСNumberOfAssignedOperations(int cNumberOfAssignedOperations) { this.cNumberOfAssignedOperations = cNumberOfAssignedOperations; }



    public void addOperationToCreate(O_Basic OBasic) { operationsToCreate.add(OBasic); }

    public void addOperationCollectionToCreate(Collection<O_Basic> operationsToCreate) { this.operationsToCreate.addAll(operationsToCreate); }

    public boolean allOperationsAssigned() {
        for(IOperation operation: operationsToCreate) {
            if(operation.operationNotScheduled()) {
                return false;
            }
        }
        return true;
        /*if(cNumberOfAssignedOperations == operationsToCreate.size()) {
            return true;
        }
        return false;*/
    }

    public void clean() {
        for(IOperation currentOperation: operationsToCreate){
            currentOperation.clean();
        }
        cNumberOfAssignedOperations = 0;
    }

    public void fullClean() {
        for(IOperation currentOperation: operationsToCreate) {
            currentOperation.fullClean();
        }
        cNumberOfAssignedOperations = 0;
    }

    public static StringBuffer generateRandomHexString(int length){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < length){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.delete(length, sb.length());
    }

}
