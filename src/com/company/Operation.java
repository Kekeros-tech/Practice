package com.company;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Operation {
    private StringBuffer nameOfOperation;
    private Group resourceGroup;
    private Series serialAffiliation;
    private ArrayList<Operation> previousOperations;
    private ArrayList<Operation> followingOperations;
    private Duration durationOfExecution;
    private OperatingMode currentOperatingMode;

    private Recourse cNumberOfAssignedRecourse;
    private WorkingHours cWorkingInterval;
    private LocalDateTime tactTime;
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
        nameOfOperation = Series.generateRandomHexString(8);
        this.previousOperations = new ArrayList<>();
        this.followingOperations = new ArrayList<>();
    }

    Operation(Operation currentOperation) {
        resourceGroup = currentOperation.resourceGroup;
        serialAffiliation = currentOperation.serialAffiliation;
        previousOperations = currentOperation.previousOperations;
        followingOperations = currentOperation.followingOperations;
        durationOfExecution = currentOperation.durationOfExecution;
        currentOperatingMode = currentOperation.currentOperatingMode;
        cNumberOfAssignedRecourse = currentOperation.cNumberOfAssignedRecourse;
        cWorkingInterval = currentOperation.cWorkingInterval;
        tactTime = currentOperation.tactTime;
        cEarlierStartTime = currentOperation.cEarlierStartTime;
        cLateStartTime = currentOperation.cLateStartTime;
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

    public LocalDateTime getTactTime() {
        return tactTime;
    }

    public void setCEarlierStartTime(String cEarlierStartTime) {
        this.cEarlierStartTime = LocalDateTime.parse(cEarlierStartTime, WorkingHours.formatter);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Операция - ");
        sb.append(nameOfOperation);
        //sb.append(" Группа ресурсов=").append(resourceGroup.getNameOfGroup());
        //sb.append(", код серии=").append(serialAffiliation.getNameOfSeries());
        //sb.append(", назначенный ресурс=").append(cNumberOfAssignedRecourse.getNameOfRecourse());
        //sb.append(cLateStartTime);
        sb.append(", рабочий интервал=").append(cWorkingInterval);
        return sb.toString();
    }

    public String print() {
        final StringBuffer sb = new StringBuffer("Операция - ");
        sb.append(nameOfOperation);
        //sb.append(" Группа ресурсов=").append(resourceGroup.getNameOfGroup());
        //sb.append(", код серии=").append(serialAffiliation.getNameOfSeries());
        //sb.append(", назначенный ресурс=").append(cNumberOfAssignedRecourse.getNameOfRecourse());
        sb.append(cLateStartTime);
        sb.append(", рабочий интервал=").append(cWorkingInterval);
        return sb.toString();
    }

    public void getLatestEndTimeOfPrevious() {
        if(tactTime != null) {
            return;
        }
        if (previousOperations.isEmpty()) {
            for(Recourse currentRecourse: resourceGroup.getRecoursesInTheGroup()) {
                if(currentRecourse.isTactDateWorkingTime(serialAffiliation.getArrivalTime())) {
                    tactTime = serialAffiliation.getArrivalTime();
                    break;
                }
                else {
                    tactTime = LocalDateTime.MAX;
                    LocalDateTime startDateAfterArrivalTime = currentRecourse.getStartDateAfterReleaseDate(serialAffiliation.getArrivalTime());

                    if(startDateAfterArrivalTime.isBefore(tactTime)) {
                        tactTime =  startDateAfterArrivalTime;
                    }
                }
            }
        }
        else {
            tactTime = LocalDateTime.MIN;
            for(int i = 0; i < previousOperations.size(); i++) {
                if(previousOperations.get(i).getCWorkingInterval() == null) {
                    tactTime = null;
                    return;
                }
                else if(previousOperations.get(i).getCWorkingInterval().getEndTime().isAfter(tactTime)) {
                    tactTime = previousOperations.get(i).getCWorkingInterval().getEndTime();
                }
            }
        }
    }

    public boolean operationNotScheduled(){
        if(cNumberOfAssignedRecourse == null && cWorkingInterval == null){
            return true;
        }
        else return false;
    }

    public void setResourceGroup(Group resourceGroup) { this.resourceGroup = resourceGroup; }

    public void setSerialAffiliation(Series serialAffiliation) { this.serialAffiliation = serialAffiliation; }

    public void setPreviousOperations(Collection<Operation> previousOperations) { this.previousOperations = new ArrayList<>(previousOperations); }

    public void setFollowingOperations(Collection<Operation> followingOperations) { this.followingOperations = new ArrayList<>(followingOperations); }

    public void setDurationOfExecution(Duration durationOfExecution) { this.durationOfExecution = durationOfExecution; }

    public  void setOperatingMode(int currentOperatingMode) {
        this.currentOperatingMode = OperatingMode.modeSelection(currentOperatingMode);
    }

    public void setTactTime(String tactTime) {
        this.tactTime = LocalDateTime.parse(tactTime, WorkingHours.formatter);
    }

    public void setCNumberOfAssignedRecourse(Recourse cNumberOfAssignedRecourse) {
        this.cNumberOfAssignedRecourse = cNumberOfAssignedRecourse;
    }

    public void setCWorkingInterval(WorkingHours cWorkingInterval) {
        this.cWorkingInterval = cWorkingInterval;
    }

    public void setCLateStartTime(LocalDateTime cLateStartTime) { this.cLateStartTime = cLateStartTime; }

    public void setNameOfOperation(String nameOfOperation) {
        this.nameOfOperation = new StringBuffer(nameOfOperation);
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
        if(maxTime == LocalDateTime.MIN) {
            return this.cEarlierStartTime;
        }
        return maxTime;
    }

    public void setNewTactTime() {
        LocalDateTime startDate = LocalDateTime.MAX;
        for(Recourse tactRecourse: resourceGroup.getRecoursesInTheGroup()) {
            if(tactRecourse.getStartDateAfterReleaseDate(tactTime).isBefore(startDate)) {
                startDate = tactRecourse.getStartDateAfterReleaseDate(tactTime);
            }
        }
        tactTime = startDate;
    }


    public ArrayList<Recourse> getResourcesToBorrow () {
        ArrayList<Recourse> recoursesToBorrow = new ArrayList<>();

        for(Recourse candidateForAddition: resourceGroup.getRecoursesInTheGroup()) {

            switch (currentOperatingMode) {
                case canNotBeInterrupted: {
                    if(candidateForAddition.takeWhichCanNotBeInterrupted(durationOfExecution, tactTime) != null){
                        recoursesToBorrow.add(candidateForAddition);
                    }
                    break;
                }
                case canBeInterrupted: {
                    if(candidateForAddition.takeWhichCanBeInterrupted(durationOfExecution, tactTime) != null) {
                        recoursesToBorrow.add(candidateForAddition);
                    }
                    break;
                }
            }
        }
        return recoursesToBorrow;
    }

    public void installOperationForSpecificResource(Recourse currentRecourse) {

        switch (currentOperatingMode) {
            case canNotBeInterrupted: {
                currentRecourse.setReleaseTime(currentRecourse.takeWhichCanNotBeInterrupted(durationOfExecution, tactTime));
                break;
            }
            case canBeInterrupted: {
                currentRecourse.setReleaseTime(currentRecourse.takeWhichCanBeInterrupted(durationOfExecution, tactTime));
                break;
            }
        }

        cNumberOfAssignedRecourse = currentRecourse;
        cWorkingInterval = new WorkingHours(tactTime, cNumberOfAssignedRecourse.getReleaseTime());
        serialAffiliation.setСNumberOfAssignedOperations(serialAffiliation.getСNumberOfAssignedOperations() + 1);
        cEarlierStartTime = tactTime;
    }

   public void getLatestEndTimeOfFollowing() {
        if(tactTime != null) {
            return;
        }

        else if (followingOperations.isEmpty()) {
            tactTime = serialAffiliation.getDeadlineForCompletion();
        }
        else {
            tactTime = LocalDateTime.MAX;
            for (int i = 0; i < followingOperations.size(); i++) {
                if (followingOperations.get(i).getCLateStartTime().isBefore(tactTime)) {
                    tactTime = followingOperations.get(i).getCLateStartTime();
                }
            }
        }
    }

    //Reverse
    public LocalDateTime installReverseOperation() {

        LocalDateTime startDate = LocalDateTime.MIN;

        for (Recourse tactRecourse: resourceGroup.getRecoursesInTheGroup()) {
            LocalDateTime newTime = null;
            if(currentOperatingMode == OperatingMode.canBeInterrupted) {
                newTime = tactRecourse.takeReverseWhichCanBeInterrupted(durationOfExecution, tactTime, this.getEarliestStartTime());
            }
            else
            {
                newTime = tactRecourse.tackReverseWhichCanNotBeInterrupted(durationOfExecution, tactTime, this.getEarliestStartTime());
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

        LocalDateTime localmax = LocalDateTime.MIN;
        for(Recourse currentRecourse: resourceGroup.getRecoursesInTheGroup())
        {
            for(int i = currentRecourse.getSchedule().size() - 1; i > 0; i--) {
                if (currentRecourse.getSchedule().get(i).getStartTime().isAfter(this.getEarliestStartTime()) && currentRecourse.getSchedule().get(i).getEndTime().isBefore(tactTime)) {
                    if(currentRecourse.getSchedule().get(i).getEndTime().isAfter(localmax)) {
                        localmax = currentRecourse.getSchedule().get(i).getEndTime();
                        break;
                    }
                }
            }
        }
        tactTime = localmax;
        return null;
    }

    public void clean() {
        if(cNumberOfAssignedRecourse != null) {
            cNumberOfAssignedRecourse.clean();
        }
        cNumberOfAssignedRecourse = null;
        cWorkingInterval = null;
        tactTime = null;
    }

    public void fullClean() {
        this.clean();
        cEarlierStartTime = null;
        cLateStartTime = null;
    }
}
