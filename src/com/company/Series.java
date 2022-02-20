package com.company;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Series {
    private StringBuffer nameOfSeries;
    private ArrayList<Operation> operationsToCreate;
    private LocalDateTime deadlineForCompletion;
    private LocalDateTime arrivalTime;

    private int cNumberOfAssignedOperations;


    Series(Collection<Operation> operationsToCreate, LocalDateTime deadlineForCompletion, LocalDateTime arrivalTime)
    {
        nameOfSeries = generateRandomHexString(8);
        this.operationsToCreate = new ArrayList<>(operationsToCreate);
        this.deadlineForCompletion = deadlineForCompletion;
        this.arrivalTime = arrivalTime;
    }

    Series(Collection<Operation> operationsToCreate, String deadlineForCompletion, String arrivalTime)
    {
        this(operationsToCreate, LocalDateTime.parse(deadlineForCompletion, WorkingHours.formatter), LocalDateTime.parse(arrivalTime, WorkingHours.formatter));
    }

    Series(String nameOfOperation, Collection<Operation> operationsToCreate, String deadlineForCompletion, String arrivalTime)
    {
        this(operationsToCreate, LocalDateTime.parse(deadlineForCompletion, WorkingHours.formatter), LocalDateTime.parse(arrivalTime, WorkingHours.formatter));
        this.nameOfSeries = new StringBuffer(nameOfOperation);
    }

    public StringBuffer getNameOfSeries() { return nameOfSeries; }

    public ArrayList<Operation> getOperationsToCreate() { return operationsToCreate; }

    public LocalDateTime getDeadlineForCompletion() { return deadlineForCompletion; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }

    public int getСNumberOfAssignedOperations() { return cNumberOfAssignedOperations; }



    public void setOperationsToCreate(Collection<Operation> operationsToCreate) { this.operationsToCreate = new ArrayList<>(operationsToCreate); }

    public void setDeadlineForCompletion(LocalDateTime deadlineForCompletion) { this.deadlineForCompletion = deadlineForCompletion; }

    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public void setСNumberOfAssignedOperations(int cNumberOfAssignedOperations) { this.cNumberOfAssignedOperations = cNumberOfAssignedOperations; }



    public void addOperationToCreate(Operation operation) { operationsToCreate.add(operation); }

    public void addOperationCollectionToCreate(Collection<Operation> operationsToCreate) { this.operationsToCreate.addAll(operationsToCreate); }

    public void removePreviousOperations(Operation requiredOperation){
        for (Operation operation: operationsToCreate) {
            if(operation.getPreviousOperations().contains(requiredOperation)){
                operation.getPreviousOperations().remove(requiredOperation);
            }
        }
    }

    public boolean allOperationsAssigned() {
        if(cNumberOfAssignedOperations == operationsToCreate.size()) {
            return true;
        }
        return false;
    }

    public void clean() {
        for(Operation currentOperation: operationsToCreate){
            currentOperation.clean();
        }
        cNumberOfAssignedOperations = 0;
    }

    public void fullClean() {
        for(Operation currentOperation: operationsToCreate) {
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
