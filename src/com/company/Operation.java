package com.company;

public class Operation {
    private Group recourcesInUse;
    private Series[] serialAffiliation;
    private Operation[] previousOperations;
    private Operation[] followingOperations;
    private int durationOfExecution;
    private boolean operatingMode;

    public Operation(Group recourcesInUse,Series[] serialAffiliation, Operation[] previousOperations, Operation[] followingOperations,
                     int durationOfExecution, boolean operatingMode) {
        this.recourcesInUse = recourcesInUse;
        this.serialAffiliation = serialAffiliation;
        this.previousOperations = previousOperations;
        this.followingOperations = followingOperations;
        this.durationOfExecution = durationOfExecution;
        this.operatingMode = operatingMode;
    }

    public Operation(Group recourcesInUse,Series[] serialAffiliation, Operation[] previousOperations,
                     Operation[] followingOperations, int durationOfExecution) {
        this(recourcesInUse, serialAffiliation, previousOperations, followingOperations, durationOfExecution,false);
    }

    public Group getRecourcesInUse() { return recourcesInUse; }

    public Series[] getSerialAffiliation() { return serialAffiliation; }

    public Operation[] getPreviousOperations() { return previousOperations; }

    public Operation[] getFollowingOperations() { return followingOperations; }

    public int getDurationOfExecution() { return durationOfExecution; }

    public boolean getOperatingMode() { return operatingMode; }

    public void setRecourcesInUse(Group recourcesInUse) { this.recourcesInUse = recourcesInUse; }

    public void setSerialAffiliation(Series[] serialAffiliation) { this.serialAffiliation = serialAffiliation; }

    public void setPreviousOperations(Operation[] previousOperations) { this.previousOperations = previousOperations; }

    public void setFollowingOperations(Operation[] followingOperations) { this.followingOperations = followingOperations; }

    public void setDurationOfExecution(int durationOfExecution) { this.durationOfExecution = durationOfExecution; }

    public  void setOperatingMode(boolean operatingMode) { this.operatingMode = operatingMode; }
}
