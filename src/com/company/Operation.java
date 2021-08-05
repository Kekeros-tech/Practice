package com.company;

enum OperatingMode {
    canBeInterrupted,
    canNotBeInterrupted;
}

public class Operation {
    private Group recourcesInUse;
    private Series serialAffiliation; // к одной партии
    private Operation[] previousOperations;
    private Operation[] followingOperations;
    private int durationOfExecution;
    private OperatingMode currentOperatingMode;

    public Operation(Group recourcesInUse,Series serialAffiliation, Operation[] previousOperations, Operation[] followingOperations,
                     int durationOfExecution, int currentOperatingMode) {
        this.recourcesInUse = recourcesInUse;
        this.serialAffiliation = serialAffiliation;
        this.previousOperations = previousOperations;
        this.followingOperations = followingOperations;
        this.durationOfExecution = durationOfExecution;
        switch (currentOperatingMode)
        {
            case 0:
                this.currentOperatingMode = OperatingMode.canBeInterrupted;
                break;
            default:
                this.currentOperatingMode = OperatingMode.canNotBeInterrupted;

        }
    }

    public Operation(Group recourcesInUse,Series serialAffiliation, Operation[] previousOperations,
                     Operation[] followingOperations, int durationOfExecution) {
        this(recourcesInUse, serialAffiliation, previousOperations, followingOperations, durationOfExecution, 0);
    }

    public Group getRecourcesInUse() { return recourcesInUse; }

    public Series getSerialAffiliation() { return serialAffiliation; }

    public Operation[] getPreviousOperations() { return previousOperations; }

    public Operation[] getFollowingOperations() { return followingOperations; }

    public int getDurationOfExecution() { return durationOfExecution; }

    public OperatingMode getOperatingMode() { return currentOperatingMode; }

    public void setRecourcesInUse(Group recourcesInUse) { this.recourcesInUse = recourcesInUse; }

    public void setSerialAffiliation(Series serialAffiliation) { this.serialAffiliation = serialAffiliation; }

    public void setPreviousOperations(Operation[] previousOperations) { this.previousOperations = previousOperations; }

    public void setFollowingOperations(Operation[] followingOperations) { this.followingOperations = followingOperations; }

    public void setDurationOfExecution(int durationOfExecution) { this.durationOfExecution = durationOfExecution; }

    public  void setOperatingMode(int currentOperatingMode) {
        switch (currentOperatingMode) { // вынести в отдельную функцию, узнать куда на самом деле это стоит выносить?
            case 0:
                this.currentOperatingMode = OperatingMode.canBeInterrupted;
                break;
            default:
                this.currentOperatingMode = OperatingMode.canNotBeInterrupted;
        }
        //this.currentOperatingMode = currentOperatingMode;
    }
}
