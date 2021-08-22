package com.company;
import java.time.Duration;
import java.time.LocalDateTime;
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

    public void addFollowingOperation(Collection<Operation> followingOperation) {
        if(this.followingOperations == null) {
            this.followingOperations = new ArrayList<>();
        }
        this.followingOperations.addAll(followingOperation);
        for (Operation currentOperation: followingOperation) {
            if(currentOperation.previousOperations == null) {
                currentOperation.previousOperations = new ArrayList<>();
            }
            currentOperation.previousOperations.add(this);
        }
        //if(followingOperation.previousOperations == null){
        //    followingOperation.previousOperations = new ArrayList<>();
        //}
        //followingOperation.previousOperations.add(this);
    }

    public Recourse scheduleAnOperation(int numberOfRecourse, int numberOfWorkingInterval){
        Recourse currentRecourse = resourceGroup.get(numberOfRecourse);
        //currentRecourse.takeRecourse(durationOfExecution, numberOfWorkingInterval);
        Duration duration = currentRecourse.takeRecourse(durationOfExecution, numberOfWorkingInterval);
        this.setDurationOfExecution(duration);
        return currentRecourse;
        //resourceGroup.setRecoursesInTheGroup(resourceGroup.get(numberOfRecourse));
        //serialAffiliation.removePreviousOperations(this);
    }

    //тестовый метод
    public void installAnOperation(int numberOfRecourse, int numberOfWorkingInterval, LocalDateTime currentDate) {
        if(this.getOperatingMode() == OperatingMode.canNotBeInterrupted){
            Recourse currentRecourse = resourceGroup.get(numberOfRecourse);
            Duration duration = currentRecourse.takeRecourse(durationOfExecution, numberOfWorkingInterval);
            this.setDurationOfExecution(duration);
            resourceGroup.setRecoursesInTheGroup(resourceGroup.get(numberOfRecourse));
        }
        else{
            while(!durationOfExecution.isZero() || this.hasRecourseForThisDate(currentDate)){
                for(Recourse recourse: resourceGroup.getRecoursesInTheGroup()){

                }
            }
        }
    }

    //метод определяет есть ли ресурс до времени currentDate, куда мы можем назначить операцию
    public boolean hasRecourseForThisDate(LocalDateTime currentDate){
        for(Recourse currentRecourse: resourceGroup.getRecoursesInTheGroup()) {
            LocalDateTime workingDate = currentRecourse.getSchedule().get(0).getStartTime();
            int iteration = 0;
            while(workingDate.isBefore(currentDate)) {
                if(this.enoughTime(currentRecourse.getSchedule().get(iteration).getStartTime(),currentRecourse.getSchedule().get(iteration).getEndTime())){
                    return true;
                }
                iteration++;
                workingDate = currentRecourse.getSchedule().get(iteration).getStartTime();
            }
        }
        return false;
    }

    public boolean enoughTime(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime,endTime);
        if(duration.toNanos() >= durationOfExecution.toNanos()) {
            return true;
        }
        return false;
    }
}
