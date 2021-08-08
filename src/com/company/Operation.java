package com.company;
import java.util.ArrayList;

public class Operation {
    private Group resourcesInUse;
    private Series serialAffiliation;
    private ArrayList<Operation> previousOperations;
    private ArrayList<Operation> followingOperations;
    private WorkingHours durationOfExecution; // сколько рабочих часов уйдет на выполнение операции (срок выполнения операции)
    private OperatingMode currentOperatingMode;

    Operation(Group resourcesInUse,Series serialAffiliation, ArrayList<Operation> previousOperations, ArrayList<Operation> followingOperations,
                     WorkingHours durationOfExecution, int currentOperatingMode) {
        this.resourcesInUse = resourcesInUse;
        this.serialAffiliation = serialAffiliation;
        this.previousOperations = previousOperations;
        this.followingOperations = followingOperations;
        this.durationOfExecution = durationOfExecution;
        this.currentOperatingMode = OperatingMode.modeSelection(currentOperatingMode);
    }

    Operation(Group resourcesInUse,Series serialAffiliation, ArrayList<Operation> previousOperations,
                     ArrayList<Operation> followingOperations, WorkingHours durationOfExecution) {
        this(resourcesInUse, serialAffiliation, previousOperations, followingOperations, durationOfExecution, 0);
    }

    Operation(){

    }

    public Group getResourcesInUse() { return resourcesInUse; }

    public Series getSerialAffiliation() { return serialAffiliation; }

    public ArrayList<Operation> getPreviousOperations() { return previousOperations; }

    public ArrayList<Operation> getFollowingOperations() { return followingOperations; }

    public WorkingHours getDurationOfExecution() { return durationOfExecution; }

    public OperatingMode getOperatingMode() { return currentOperatingMode; }

    public void setResourcesInUse(Group resourcesInUse) { this.resourcesInUse = resourcesInUse; }

    public void setSerialAffiliation(Series serialAffiliation) { this.serialAffiliation = serialAffiliation; }

    public void setPreviousOperations(ArrayList<Operation> previousOperations) { this.previousOperations = previousOperations; }

    public void setFollowingOperations(ArrayList<Operation> followingOperations) { this.followingOperations = followingOperations; }

    public void setDurationOfExecution(WorkingHours durationOfExecution) { this.durationOfExecution = durationOfExecution; }

    public  void setOperatingMode(int currentOperatingMode) {
        this.currentOperatingMode = OperatingMode.modeSelection(currentOperatingMode);
    }

    public void addPreviousOperation(Operation previousOperation) {
        this.previousOperations.add(previousOperation);
    }

    public void addFollowingOperation(Operation followingOperation) {
        this.followingOperations.add(followingOperation);
    }
}
