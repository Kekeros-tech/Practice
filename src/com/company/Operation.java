package com.company;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

public class Operation {
    private Group resourceGroup; //когда операция назначается, мы должны группу уменьшать до 1 элемента, чтобы понимать где сейчас обрабатывается операция и сколько ещё времени ей требуется
    private Series serialAffiliation;
    private ArrayList<Operation> previousOperations;
    private ArrayList<Operation> followingOperations;
    private Duration durationOfExecution;
    private OperatingMode currentOperatingMode;

    Operation(Group resourceGroup,Series serialAffiliation, Collection<Operation> previousOperations, Collection<Operation> followingOperations,
                     Duration durationOfExecution, int currentOperatingMode) {
        this.resourceGroup = resourceGroup;
        this.serialAffiliation = serialAffiliation;
        this.previousOperations = new ArrayList<>(previousOperations);
        this.followingOperations = new ArrayList<>(followingOperations);
        this.durationOfExecution = durationOfExecution;
        this.currentOperatingMode = OperatingMode.modeSelection(currentOperatingMode);
    }

    Operation(Group resourceGroup,Series serialAffiliation, ArrayList<Operation> previousOperations,
                     ArrayList<Operation> followingOperations, Duration durationOfExecution) {
        this(resourceGroup, serialAffiliation, previousOperations, followingOperations, durationOfExecution, 0);
    }

    Operation(){
        this.previousOperations = new ArrayList<>();
        this.followingOperations = new ArrayList<>();
    }

    public Group getResourceGroup() { return resourceGroup; }

    public Series getSerialAffiliation() { return serialAffiliation; }

    public ArrayList<Operation> getPreviousOperations() { return previousOperations; }

    public ArrayList<Operation> getFollowingOperations() { return followingOperations; }

    public Duration getDurationOfExecution() { return durationOfExecution; }

    public OperatingMode getOperatingMode() { return currentOperatingMode; }


    public void setResourceGroup(Group resourceGroup) { this.resourceGroup = resourceGroup; }

    public void setSerialAffiliation(Series serialAffiliation) { this.serialAffiliation = serialAffiliation; }

    public void setPreviousOperations(Collection<Operation> previousOperations) { this.previousOperations = new ArrayList<>(previousOperations); }

    public void setFollowingOperations(Collection<Operation> followingOperations) { this.followingOperations = new ArrayList<>(followingOperations); }

    public void setDurationOfExecution(Duration durationOfExecution) { this.durationOfExecution = durationOfExecution; }

    public  void setOperatingMode(int currentOperatingMode) {
        this.currentOperatingMode = OperatingMode.modeSelection(currentOperatingMode);
    }


    public void addPreviousOperation(Operation previousOperation) { //один и тот же код
        if(this.previousOperations == null) {
            this.previousOperations = new ArrayList<>();
        }
        this.previousOperations.add(previousOperation);
        if(previousOperation.followingOperations == null){
            previousOperation.followingOperations = new ArrayList<>();
        }
        previousOperation.followingOperations.add(this);
    }

    public void addFollowingOperation(Operation followingOperation) {
        if(this.followingOperations == null) {
            this.followingOperations = new ArrayList<>();
        }
        this.followingOperations.add(followingOperation);
        if(followingOperation.previousOperations == null){
            followingOperation.previousOperations = new ArrayList<>();
        }
        followingOperation.previousOperations.add(this);
        //followingOperation.addPreviousOperation(this);
    }

    public void scheduleAnOperation(int number, int numberOfWorkingInterval){
        Recourse currentRecourse = resourceGroup.get(number);
        currentRecourse.takeRecourse(durationOfExecution, numberOfWorkingInterval);
        resourceGroup.setRecoursesInTheGroup(resourceGroup.get(number));
        serialAffiliation.removePreviousOperations(this);
    }
}
