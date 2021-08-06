package com.company;
import java.util.ArrayList;

public class Operation {
    private Group recourcesInUse;
    private Series serialAffiliation;
    private ArrayList<Operation> previousOperations;
    private ArrayList<Operation> followingOperations;
    private int durationOfExecution;
    private OperatingMode currentOperatingMode;

    public Operation(Group recourcesInUse,Series serialAffiliation, ArrayList<Operation> previousOperations, ArrayList<Operation> followingOperations,
                     int durationOfExecution, int currentOperatingMode) {
        this.recourcesInUse = recourcesInUse;
        this.serialAffiliation = serialAffiliation;
        this.previousOperations = previousOperations;
        this.followingOperations = followingOperations;
        this.durationOfExecution = durationOfExecution;
        this.currentOperatingMode = OperatingMode.modeSelection(currentOperatingMode);
    }

    public Operation(Group recourcesInUse,Series serialAffiliation, ArrayList<Operation> previousOperations,
                     ArrayList<Operation> followingOperations, int durationOfExecution) {
        this(recourcesInUse, serialAffiliation, previousOperations, followingOperations, durationOfExecution, 0);
    }

    public Group getRecourcesInUse() { return recourcesInUse; }

    public Series getSerialAffiliation() { return serialAffiliation; }

    public ArrayList<Operation> getPreviousOperations() { return previousOperations; }

    public ArrayList<Operation> getFollowingOperations() { return followingOperations; }

    public int getDurationOfExecution() { return durationOfExecution; }

    public OperatingMode getOperatingMode() { return currentOperatingMode; }

    public void setRecourcesInUse(Group recourcesInUse) { this.recourcesInUse = recourcesInUse; }

    public void setSerialAffiliation(Series serialAffiliation) { this.serialAffiliation = serialAffiliation; }

    public void setPreviousOperations(ArrayList<Operation> previousOperations) { this.previousOperations = previousOperations; }

    public void setFollowingOperations(ArrayList<Operation> followingOperations) { this.followingOperations = followingOperations; }

    public void setDurationOfExecution(int durationOfExecution) { this.durationOfExecution = durationOfExecution; }

    public  void setOperatingMode(int currentOperatingMode) {
        this.currentOperatingMode = OperatingMode.modeSelection(currentOperatingMode);
    }
}
