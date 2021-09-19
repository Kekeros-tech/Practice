package com.company;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Operation {
    private Group resourceGroup;
    private Series serialAffiliation;
    private ArrayList<Operation> previousOperations;
    private ArrayList<Operation> followingOperations;
    private Duration durationOfExecution;
    private OperatingMode currentOperatingMode;

    private Recourse cNumberOfAssignedRecourse;
    private WorkingHours cWorkingInterval;
    private LocalDateTime cEarlierStartTime;
    private LocalDateTime cLateStartTime;

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

    public LocalDateTime getCLateStartTime() { return cLateStartTime; }

    public LocalDateTime getCEarlierStartTime() { return cEarlierStartTime; }

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

    public void setCLateStartTime(LocalDateTime cLateStartTime) { this.cLateStartTime = cLateStartTime; }



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
        for (int i = 0; i < previousOperations.size(); i++) {
            if (previousOperations.get(i).cNumberOfAssignedRecourse == null) {
                return false;
            }
        }
        return true;
    }

    //Reverse

    public boolean allFollowingAssignedReverse() {
        for(int i = 0; i < followingOperations.size(); i++){
            if(followingOperations.get(i).cLateStartTime == null) {
                return false;
            }
        }
        return true;
    }

    public boolean allPreviousAssignedReverse() {
        for(int i = 0; i < previousOperations.size(); i++) {
            if(previousOperations.get(i).cLateStartTime != null) {
                return false;
            }
        }
        return true;
    }

    //Reverse
    public LocalDateTime getEarliestStartTime() {
        LocalDateTime maxTime = LocalDateTime.MIN;

        for(int i = 0; i < previousOperations.size(); i++ ) {
            if(previousOperations.get(i).cEarlierStartTime.isAfter(maxTime))
            {
                maxTime = previousOperations.get(i).cEarlierStartTime;
            }
        }
        if(maxTime == LocalDateTime.MIN){
            return this.cEarlierStartTime;
        }
        return maxTime;
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

        for ( Recourse tactRecourse: resourceGroup.getRecoursesInTheGroup() ) {
            if( currentOperatingMode == OperatingMode.canBeInterrupted ) {
                cNumberOfAssignedRecourse = tactRecourse.takeWhichCanBeInterrupted(durationOfExecution, tackDate);
            }
            else
            {
                cNumberOfAssignedRecourse = tactRecourse.tackWhichCanNotBeInterrupted(durationOfExecution, tackDate);
            }
            if(cNumberOfAssignedRecourse != null) {
                cWorkingInterval = new WorkingHours(tackDate, cNumberOfAssignedRecourse.getReleaseTime());
                serialAffiliation.setСNumberOfAssignedOperations(serialAffiliation.getСNumberOfAssignedOperations() + 1);
                cEarlierStartTime = tackDate;
                return cWorkingInterval.getEndTime();
            }
            else
            {
                if(tactRecourse.getStartDateAfterReleaseDate(tackDate).isBefore(startDate)) {
                    startDate = tactRecourse.getStartDateAfterReleaseDate(tackDate);
                }
            }
        }
        return startDate;
    }


    //Reverse
    public LocalDateTime installReverseOperation(LocalDateTime tackDate) {

        LocalDateTime startDate = LocalDateTime.MIN;

        for (Recourse tactRecourse: resourceGroup.getRecoursesInTheGroup()) {
            Recourse flagRecourse;
            LocalDateTime newTime = null;
            if(currentOperatingMode == OperatingMode.canBeInterrupted) {
                newTime = tactRecourse.takeReverseWhichCanBeInterrupted(durationOfExecution, tackDate, this.getEarliestStartTime());
            }
            else
            {
                newTime = tactRecourse.tackReverseWhichCanNotBeInterrupted(durationOfExecution, tackDate, this.getEarliestStartTime());
            }

            if ( newTime != null && newTime.isAfter(startDate) ) {
                startDate = newTime;
            }
        }
        if( startDate != LocalDateTime.MIN ) {
            cLateStartTime = startDate;
            serialAffiliation.setСNumberOfAssignedOperations(serialAffiliation.getСNumberOfAssignedOperations() + 1);
            return startDate;
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

    public void clean() {
        cNumberOfAssignedRecourse.clean();
        cNumberOfAssignedRecourse = null;
        cWorkingInterval = null;
    }

}
