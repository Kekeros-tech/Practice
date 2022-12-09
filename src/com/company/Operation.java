package com.company;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Operation implements IOperation{
    private StringBuffer nameOfOperation;
    protected Group resourceGroup;
    protected Series serialAffiliation;
    private Duration durationOfExecution;
    protected ArrayList<IOperation> previousOperations;
    protected ArrayList<IOperation> followingOperations;
    protected IOperationMode currentOperatingMode;

    private ArrayList<IResource> cNumberOfAssignedRecourse;
    private ArrayList<WorkingHours> cWorkingInterval;
    protected LocalDateTime tactTime;

    Operation() {
        nameOfOperation = Series.generateRandomHexString(8);
        this.previousOperations = new ArrayList<>();
        this.followingOperations = new ArrayList<>();
        this.cNumberOfAssignedRecourse = new ArrayList<>();
        this.cWorkingInterval = new ArrayList<>();
    }

    protected void setPriority() {
        return;
    }

    public Group getResourceGroup() { return resourceGroup; }
    public Series getSerialAffiliation() { return serialAffiliation; }
    public ArrayList<IOperation> getPreviousOperations() { return previousOperations; }
    public ArrayList<IOperation> getFollowingOperations() { return followingOperations; }
    public Duration getInitDurationOfExecution() { return durationOfExecution; }
    public Duration getDurationOfExecution() { return currentOperatingMode.getDurationOfExecution(); }
    public IOperationMode getOperationMode() { return currentOperatingMode; }
    public Collection<IResource> getCNumberOfAssignedRecourse() { return cNumberOfAssignedRecourse; }
    public ArrayList<WorkingHours> getCWorkingInterval() { return cWorkingInterval; }
    public LocalDateTime getCLateStartTime() { return null; }
    public LocalDateTime getCEarlierStartTime() { return null; }
    public LocalDateTime getTactTime() { return tactTime; }

    @Override
    public int getNumberOfOperationMode() {
        switch (currentOperatingMode.getClass().getSimpleName()) {
            case("MO_CanNotBeInterrupted"):
                return 1;
            case("MO_CanBeInterrupted"):
                return 0;
        }
        return 0;
    }

    //Можно будет удалить потом
    public boolean isCanBePlacedInReverseFront() { return false; }

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

    @Override
    public void setPreviousOperation(ArrayList<IOperation> previousOperation) {
        this.previousOperations = previousOperation;
    }

    //Задать время такта
    public void setTactTime() {
        if (tactTime != null) return;
        if (previousOperations.isEmpty()) tactTime = serialAffiliation.getArrivalTime();
        else setTactTimeByEndTimeOfPrevious();
    }

    public boolean operationNotScheduled() {
        return currentOperatingMode.operationNotScheduled();
    }

    public void setResourceGroup(Group resourceGroup) { this.resourceGroup = resourceGroup; }

    public void setSerialAffiliation(Series serialAffiliation) { this.serialAffiliation = serialAffiliation; }

    public void setPreviousOperations(Collection<Operation> previousOperations) { this.previousOperations = new ArrayList<>(previousOperations); }

    public void setFollowingOperations(Collection<Operation> followingOperations) { this.followingOperations = new ArrayList<>(followingOperations); }

    public void setDurationOfExecution(Duration durationOfExecution) { this.durationOfExecution = durationOfExecution; }
    //public void setDurationOfExecution(Duration durationOfExecution) { currentOperatingMode.setDurationOfExecution(durationOfExecution); }

    public void setOperatingMode(int currentOperatingMode) {
        switch (currentOperatingMode) {
            case 0:
                this.currentOperatingMode = new MO_CanNotBeInterrupted();
                this.currentOperatingMode.setDurationOfExecution(durationOfExecution);
                break;
            case 1:
                this.currentOperatingMode = new MO_CanBeInterrupted();
                this.currentOperatingMode.setDurationOfExecution(durationOfExecution);
                break;
        }
    }

    public void addCNumberOfAssignedRecourse(IResource cNumberOfAssignedRecourse) {
        this.cNumberOfAssignedRecourse.add(cNumberOfAssignedRecourse);
    }

    public void addCWorkingInterval(WorkingHours cWorkingInterval) {
        this.cWorkingInterval.add(cWorkingInterval);
    }

    public void setNameOfOperation(String nameOfOperation) {
        this.nameOfOperation = new StringBuffer(nameOfOperation);
    }

    public void addFollowingOperation(IOperation followingOperation) {
        if(this.followingOperations == null) {
            this.followingOperations = new ArrayList<>();
        }
        this.followingOperations.add(followingOperation);
        if(followingOperation.getPreviousOperations() == null){
            followingOperation.setPreviousOperation(new ArrayList<IOperation>());
        }
        followingOperation.getPreviousOperations().add(this);
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

    public boolean allFollowingAssigned() {
        for (int i = 0; i < followingOperations.size(); i++) {
            if (followingOperations.get(i).operationNotScheduled()) {
                return false;
            }
        }
        return true;
    }

    public void setNewTactTime() {
        currentOperatingMode.setNewTactTime(this);
    }

    //В операции с преоритетом не переопределяется
    public ArrayList<IResource> getResourcesToBorrow () {
        ArrayList<IResource> recoursesToBorrow = new ArrayList<>();
        for(IResource recourse: resourceGroup.getRecoursesInTheGroup()) {
            if(currentOperatingMode.isResourcesCanToBorrow(this, recourse)) {
                recoursesToBorrow.add(recourse);
            }
        }

        return recoursesToBorrow;
    }

    //Перенесено
    public void installOperation() {
        boolean operationSuccessfullyInstalled = false;
        for (IResource resource: resourceGroup.getRecoursesInTheGroup())
        {
            operationSuccessfullyInstalled = currentOperatingMode.installOperation(this, resource);
            if(operationSuccessfullyInstalled)
            {

                break;
            }
        }
    }

    //Перенес в операцию с преоритетом
    public void installOperationForSpecificResource(IResource currentRecourse) {
        currentOperatingMode.installOperation(this, currentRecourse);
    }

    public void setTactTimeByEndTimeOfPrevious() {
        tactTime = LocalDateTime.MIN;
        for(int i = 0; i < previousOperations.size(); i++) {
            if(previousOperations.get(i).operationNotScheduled()) {
                tactTime = null;
                return;
            }
            else {
                for(WorkingHours currentWH: previousOperations.get(i).getCWorkingInterval()) {
                    if(currentWH.getEndTime().isAfter(tactTime)) {
                        tactTime = currentWH.getEndTime();
                    }
                }
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
            //cNumberOfAssignedRecourse.clean();
            for(IResource resource: cNumberOfAssignedRecourse) {
                resource.clean();
            }
        }
        cNumberOfAssignedRecourse = new ArrayList<>();
        cWorkingInterval = new ArrayList<>();
        tactTime = null;
        currentOperatingMode.setDurationOfExecution(durationOfExecution);
    }

    public void fullClean() {
        this.clean();
    }
}
