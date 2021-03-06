package com.company;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Series {
    private ArrayList<Operation> operationsToCreate;
    private LocalDateTime deadlineForCompletion;
    private LocalDateTime arrivalTime;

    private int cNumberOfAssignedOperations;


    Series(Collection<Operation> operationsToCreate, LocalDateTime deadlineForCompletion, LocalDateTime arrivalTime)
    {
        this.operationsToCreate = new ArrayList<>(operationsToCreate);
        this.deadlineForCompletion = deadlineForCompletion;
        this.arrivalTime = arrivalTime;
    }

    Series(Collection<Operation> operationsToCreate, String deadlineForCompletion, String arrivalTime)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.operationsToCreate = new ArrayList<>(operationsToCreate);
        this.deadlineForCompletion = LocalDateTime.parse(deadlineForCompletion,formatter);
        this.arrivalTime = LocalDateTime.parse(arrivalTime,formatter);
    }


    public ArrayList<Operation> getOperationsToCreate() { return operationsToCreate; }

    public LocalDateTime getDeadlineForCompletion() { return deadlineForCompletion; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }

    public int get–°NumberOfAssignedOperations() { return cNumberOfAssignedOperations; }



    public void setOperationsToCreate(Collection<Operation> operationsToCreate) { this.operationsToCreate = new ArrayList<>(operationsToCreate); }

    public void setDeadlineForCompletion(LocalDateTime deadlineForCompletion) { this.deadlineForCompletion = deadlineForCompletion; }

    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public void set–°NumberOfAssignedOperations(int cNumberOfAssignedOperations) { this.cNumberOfAssignedOperations = cNumberOfAssignedOperations; }



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

/*    public void nullifyPriorities() {
        for (Operation currentOperation: operationsToCreate) {
            for (Recourse recourse: currentOperation.getResourceGroup().getRecoursesInTheGroup()) {
                recourse.setCPriority(0);
            }
        }
    }*/

/*    public Recourse PrioritizeRecourse() {
        Recourse maxRecourse = new Recourse();
        for (Operation currentOperation: operationsToCreate) {
            if(currentOperation.getCNumberOfAssignedRecourse() == null && currentOperation.allPreviousAssigned()) {
                for (Recourse recourse: currentOperation.getResourceGroup().getRecoursesInTheGroup()) {
                    recourse.increasePriority();
                    if(recourse.getCPriority() > maxRecourse.getCPriority()){
                        maxRecourse = recourse;
                    }
                }
            }
        }
        return maxRecourse;
    }*/
}
