package com.company;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Operation {
    private StringBuffer nameOfOperation;
    protected Group resourceGroup;
    protected Series serialAffiliation;
    protected ArrayList<Operation> previousOperations;
    protected ArrayList<Operation> followingOperations;
    protected Duration durationOfExecution;
    protected OperatingMode currentOperatingMode;

    private IResource cNumberOfAssignedRecourse;
    private WorkingHours cWorkingInterval;
    protected LocalDateTime tactTime;

    Operation(Group resourceGroup, Series serialAffiliation, Collection<Operation> previousOperations, Collection<Operation> followingOperations,
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

    Operation() {
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
        //cEarlierStartTime = currentOperation.cEarlierStartTime;
        //cLateStartTime = currentOperation.cLateStartTime;
    }

    protected void setPriority() {
        return;
    }

    public Group getResourceGroup() { return resourceGroup; }
    public Series getSerialAffiliation() { return serialAffiliation; }
    public ArrayList<Operation> getPreviousOperations() { return previousOperations; }
    public ArrayList<Operation> getFollowingOperations() { return followingOperations; }
    public Duration getDurationOfExecution() { return durationOfExecution; }
    public OperatingMode getOperatingMode() { return currentOperatingMode; }
    public IResource getCNumberOfAssignedRecourse() { return cNumberOfAssignedRecourse; }
    public WorkingHours getCWorkingInterval() { return cWorkingInterval; }
    public LocalDateTime getCLateStartTime() { return null; }
    public LocalDateTime getCEarlierStartTime() { return null; }
    public LocalDateTime getTactTime() { return tactTime; }

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
        //sb.append(cLateStartTime);
        sb.append(", рабочий интервал=").append(cWorkingInterval);
        return sb.toString();
    }


    //То что является условием не завершения операции и что предыдущие отработали
    public boolean isСanBePlacedInFront() {
        if(operationNotScheduled() && allPreviousAssigned()) return true;
        return false;
    }


    //Задать время такта
    public void setTactTime() {
        if (tactTime != null) return;
        if (previousOperations.isEmpty()) tactTime = serialAffiliation.getArrivalTime();
        else setTactTimeByEndTimeOfPrevious();
    }

    public boolean operationNotScheduled() {
        if(cNumberOfAssignedRecourse == null) {
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

    public void setCNumberOfAssignedRecourse(Recourse cNumberOfAssignedRecourse) {
        this.cNumberOfAssignedRecourse = cNumberOfAssignedRecourse;
    }

    public void setCWorkingInterval(WorkingHours cWorkingInterval) {
        this.cWorkingInterval = cWorkingInterval;
    }

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
            if (previousOperations.get(i).operationNotScheduled()) {
                return false;
            }
        }
        return true;
    }

    //Reverse
    public boolean allFollowingAssignedReverse() {
        return false;
    }

    public boolean allPreviousAssignedReverse() {
        return false;
    }

    //Reverse
    public LocalDateTime getEarliestStartTime() {
        return null;
    }

    public void setNewTactTime() {
        LocalDateTime startDate = LocalDateTime.MAX;
        for(IResource tactRecourse: resourceGroup.getRecoursesInTheGroup()) {
            LocalDateTime futureTactTime = tactRecourse.getStartDateAfterReleaseDate(tactTime, this);
            if(futureTactTime.isBefore(startDate)) {
                startDate = futureTactTime;
            }
        }
        tactTime = startDate;
    }

    //В операции с преоритетом не переопределяется
    public ArrayList<IResource> getResourcesToBorrow () {
        ArrayList<IResource> recoursesToBorrow = new ArrayList<>();

        for(IResource candidateForAddition: resourceGroup.getRecoursesInTheGroup()) {

            switch (currentOperatingMode) {
                case canNotBeInterrupted: {
                    if(candidateForAddition.takeWhichCanNotBeInterrupted(this) != null){
                        recoursesToBorrow.add(candidateForAddition);
                    }
                    break;
                }
                case canBeInterrupted: {
                    if(candidateForAddition.takeWhichCanBeInterrupted(this) != null) {
                        recoursesToBorrow.add(candidateForAddition);
                    }
                    break;
                }
            }
        }
        return recoursesToBorrow;
    }

    //Перенесено
    public void installOperation() {
        IResource currentRecourse = null;
        switch (currentOperatingMode) {
            case canNotBeInterrupted: {
                for(IResource recourse: resourceGroup.getRecoursesInTheGroup()) {
                    if(recourse.takeResWhichCanNotBeInterrupted(this) == true) {
                        currentRecourse = recourse;
                        break;
                    }
                }
            }
            case canBeInterrupted: {
                for(IResource recourse: resourceGroup.getRecoursesInTheGroup()) {
                    if(recourse.takeResWhichCanBeInterrupted(this) == true) {
                        currentRecourse = recourse;
                        break;
                    }
                }
            }
        }
        if(currentRecourse != null) {
            cNumberOfAssignedRecourse = currentRecourse;
            cWorkingInterval = new WorkingHours(tactTime, cNumberOfAssignedRecourse.getReleaseTime());
            serialAffiliation.setСNumberOfAssignedOperations(serialAffiliation.getСNumberOfAssignedOperations() + 1);
            //cEarlierStartTime = tactTime;
        }
    }

    //Перенес в операцию с преоритетом
    public void installOperationForSpecificResource(IResource currentRecourse) {

        switch (currentOperatingMode) {
            case canNotBeInterrupted: {
                currentRecourse.setReleaseTime(currentRecourse.takeWhichCanNotBeInterrupted(this));
                break;
            }
            case canBeInterrupted: {
                currentRecourse.setReleaseTime(currentRecourse.takeWhichCanBeInterrupted(this));
                break;
            }
        }

        cNumberOfAssignedRecourse = currentRecourse;
        cWorkingInterval = new WorkingHours(tactTime, cNumberOfAssignedRecourse.getReleaseTime());
        serialAffiliation.setСNumberOfAssignedOperations(serialAffiliation.getСNumberOfAssignedOperations() + 1);
        //cEarlierStartTime = tactTime;
    }

    public void setTactTimeByEndTimeOfPrevious() {
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
    }
}
