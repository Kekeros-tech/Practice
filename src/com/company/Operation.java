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

    private Recourse cNumberOfAssignedRecourse;
    private WorkingHours cWorkingInterval;

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

    public Recourse getCNumberOfAssignedRecourse() { return cNumberOfAssignedRecourse; }

    public WorkingHours getCWorkingInterval() { return cWorkingInterval; }



    public void setResourceGroup(Group resourceGroup) { this.resourceGroup = resourceGroup; }

    public void setSerialAffiliation(Series serialAffiliation) { this.serialAffiliation = serialAffiliation; }

    public void setPreviousOperations(Collection<Operation> previousOperations) { this.previousOperations = new ArrayList<>(previousOperations); }

    public void setFollowingOperations(Collection<Operation> followingOperations) { this.followingOperations = new ArrayList<>(followingOperations); }

    public void setDurationOfExecution(Duration durationOfExecution) { this.durationOfExecution = durationOfExecution; }

    public  void setOperatingMode(int currentOperatingMode) {
        this.currentOperatingMode = OperatingMode.modeSelection(currentOperatingMode);
    }

    public void setCNumberOfAssignedRecourse(Recourse cNumberOfAssignedRecourse) {
        this.cNumberOfAssignedRecourse = cNumberOfAssignedRecourse;
    }

    public void setCWorkingInterval(WorkingHours cWorkingInterval) {
        this.cWorkingInterval = cWorkingInterval;
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
    }


    //5
    public boolean allPreviousAssigned() {
        for (int i = 0; i < this.previousOperations.size(); i++) {
            if(this.previousOperations.get(i).cNumberOfAssignedRecourse == null) {
                return false;
            }
        }
        return true;
    }

    //5
    public Duration getSumOfDurationOfPreviousOperations() {
        Duration resultDuration = Duration.ZERO;
        for (Operation currentOperation: previousOperations) {
            resultDuration = resultDuration.plus(currentOperation.durationOfExecution);
        }
        return resultDuration;
    }


    public LocalDateTime installOperation(LocalDateTime tackDate) {
        LocalDateTime startDate = LocalDateTime.MAX;
        for (Recourse tackRecourse: resourceGroup.getRecoursesInTheGroup()) {
            if(currentOperatingMode == OperatingMode.canBeInterrupted) {
                cNumberOfAssignedRecourse = tackRecourse.takeWhichCanBeInterrupted(durationOfExecution, tackDate);
            }
            else
            {
                cNumberOfAssignedRecourse = tackRecourse.tackWhichCanNotBeInterrupted(durationOfExecution, tackDate);
            }
            if(cNumberOfAssignedRecourse != null) {
                cWorkingInterval = new WorkingHours(tackDate, cNumberOfAssignedRecourse.getReleaseTime());
                serialAffiliation.setСNumberOfAssignedOperations(serialAffiliation.getСNumberOfAssignedOperations() + 1);
                return cWorkingInterval.getEndTime();
            }
            else
            {
                if(tackRecourse.getStartDateAfterReleaseDate(tackDate).isBefore(startDate)){
                    startDate = tackRecourse.getStartDateAfterReleaseDate(tackDate);
                }
            }
        }
        return startDate;
    }

    //5
    public Recourse hasRecourseForThisDate(LocalDateTime currentDate) {
        for (Recourse currentRecourse: resourceGroup.getRecoursesInTheGroup()) {
            for (int i = 0; i < currentRecourse.getSchedule().size(); i++) {
                WorkingHours currentWorkingInterval = currentRecourse.getSchedule().get(i);
                if(currentWorkingInterval.getStartTime().isAfter(currentDate)) {
                    break;
                }
                if (currentWorkingInterval.isWorkingTime(currentDate) && currentRecourse.isFree(currentDate)) {
                    if (currentOperatingMode == OperatingMode.canNotBeInterrupted) {
                        if (this.enoughTime(currentDate, currentWorkingInterval.getEndTime())){
                            return currentRecourse;
                        }
                    }
                    else
                    {
                        return currentRecourse;
                    }
                }
            }
        }
        return null;
    }

    public boolean enoughTime(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime,endTime);
        if(duration.toNanos() >= durationOfExecution.toNanos()) {
            return true;
        }
        return false;
    }

}
