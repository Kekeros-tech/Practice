package com.company.operation;
import com.company.*;
import com.company.recourse.IResource;
import com.company.recourse.IStructuralUnitOfResource;
import com.company.results_of_algos.ResultOfOperationSetting;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class O_Basic implements IOperation {
    private StringBuffer nameOfOperation;
    protected Group resourceGroup;
    protected Series serialAffiliation;
    private Duration durationOfExecution;
    protected ArrayList<IOperation> previousOperations;
    protected ArrayList<IOperation> followingOperations;
    protected IOperationMode currentOperatingMode;

    private ArrayList<IStructuralUnitOfResource> cNumberOfAssignedRecourse;
    private ArrayList<WorkingHours> cWorkingInterval;
    protected LocalDateTime tactTime;

    O_Basic(O_Basic copyOBasic) {
        nameOfOperation = Series.generateRandomHexString(8);
        resourceGroup = copyOBasic.resourceGroup;
        serialAffiliation = copyOBasic.serialAffiliation;
        durationOfExecution = copyOBasic.durationOfExecution;
        previousOperations = copyOBasic.previousOperations;
        followingOperations = copyOBasic.followingOperations;
        currentOperatingMode = copyOBasic.currentOperatingMode.clone();
        cNumberOfAssignedRecourse = (ArrayList<IStructuralUnitOfResource>) copyOBasic.cNumberOfAssignedRecourse.clone();
        cWorkingInterval = (ArrayList<WorkingHours>) copyOBasic.cWorkingInterval.clone();
        tactTime = copyOBasic.getTactTime();
    }

    public O_Basic() {
        nameOfOperation = Series.generateRandomHexString(8);
        this.previousOperations = new ArrayList<>();
        this.followingOperations = new ArrayList<>();
        this.cNumberOfAssignedRecourse = new ArrayList<>();
        this.cWorkingInterval = new ArrayList<>();
    }

    O_Basic(Series serialAffiliation,
            Group resourceGroup,
            Duration durationOfExecution,
            int currentOperationMode)  {
        nameOfOperation = Series.generateRandomHexString(8);
        this.serialAffiliation = serialAffiliation;
        this.resourceGroup = resourceGroup;
        this.durationOfExecution = durationOfExecution;
        previousOperations = new ArrayList<>();
        followingOperations = new ArrayList<>();
        setOperatingMode(currentOperationMode);
        cNumberOfAssignedRecourse = new ArrayList<>();
        cWorkingInterval = new ArrayList<>();
    }

    protected void setPriority() {
        return;
    }

    public Group getResourceGroup() { return resourceGroup; }
    public Series getSerialAffiliation() { return serialAffiliation; }
    public ArrayList<IOperation> getPreviousOperations() { return previousOperations; }
    public ArrayList<IOperation> getFollowingOperations() { return followingOperations; }
    public IOperationMode getCurrentOperatingMode() { return currentOperatingMode; }

    @Override
    public Collection<O_Basic> getOperationsAtCore() {
        ArrayList<O_Basic> operationsAtCore = new ArrayList<>();
        operationsAtCore.add(this);
        return operationsAtCore;
    }

    public Duration getInitDurationOfExecution() { return durationOfExecution; }

    @Override
    public LocalDateTime getEarliestTimeOfWorkingInterval() {
        LocalDateTime minTime = LocalDateTime.MAX;
        for(WorkingHours currentWorkingInterval: cWorkingInterval) {
            if(currentWorkingInterval.getStartTime().isBefore(minTime)){
                minTime = currentWorkingInterval.getStartTime();
            }
        }
        return minTime;
    }

    public Duration getDurationOfExecution() { return currentOperatingMode.getDurationOfExecution(); }
    public IOperationMode getOperationMode() { return currentOperatingMode; }
    public Collection<IStructuralUnitOfResource> getCNumberOfAssignedRecourse() { return cNumberOfAssignedRecourse; }
    public ArrayList<WorkingHours> getCWorkingInterval() { return cWorkingInterval; }
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
    public boolean isCanBePlacedInReverseFront() {
        if(operationNotScheduled() && allFollowingAssigned()){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Операция - ");
        sb.append(nameOfOperation);
        //sb.append(" Группа ресурсов=").append(resourceGroup.getNameOfGroup());
        //sb.append(", код серии=").append(serialAffiliation.getNameOfSeries());
        sb.append(", назначенный ресурс=").append(cNumberOfAssignedRecourse);
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

    public void setTactTime(LocalDateTime tactTime) { this.tactTime = tactTime; }

    public boolean operationNotScheduled() {
        return currentOperatingMode.operationNotScheduled();
    }

    public void setResourceGroup(Group resourceGroup) { this.resourceGroup = resourceGroup; }

    public void setSerialAffiliation(Series serialAffiliation) { this.serialAffiliation = serialAffiliation; }

    public void setPreviousOperations(Collection<O_Basic> previousOBasics) { this.previousOperations = new ArrayList<>(previousOBasics); }

    public void setFollowingOperations(Collection<O_Basic> followingOBasics) { this.followingOperations = new ArrayList<>(followingOBasics); }

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

    public void addCNumberOfAssignedRecourse(IStructuralUnitOfResource cNumberOfAssignedRecourse) {
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

    public void addFollowingOperation(Collection<O_Basic> followingOBasic) {
        if(this.followingOperations == null) {
            this.followingOperations = new ArrayList<>();
        }
        this.followingOperations.addAll(followingOBasic);
        for (O_Basic currentOBasic : followingOBasic) {
            if(currentOBasic.previousOperations == null) {
                currentOBasic.previousOperations = new ArrayList<>();
            }
            currentOBasic.previousOperations.add(this);
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

    @Override
    public void setNewReverseTactTime() {
        currentOperatingMode.setNewReverseTactTime(this);
    }

    //В операции с преоритетом не переопределяется
    public ArrayList<IStructuralUnitOfResource> getResourcesToBorrow () {
        ArrayList<IStructuralUnitOfResource> recoursesToBorrow = new ArrayList<>();
        for(IResource recourse: resourceGroup.getRecoursesInTheGroup()) {
            IStructuralUnitOfResource result = currentOperatingMode.isResourcesCanToBorrow(this, recourse);
            if(result != null) {
                recoursesToBorrow.add(result);
            }
        }
        return recoursesToBorrow;
    }

    @Override
    public int getCountOfOperations() {
        return 1;
    }

    //Перенесено
    public void installOperation() {
        for (IResource resource: resourceGroup.getRecoursesInTheGroup())
        {
            ResultOfOperationSetting resultOfOperationSetting = currentOperatingMode.installOperation(this, resource);
            if(resultOfOperationSetting != null)
            {
                WorkingHours bufferOfWH = resultOfOperationSetting.getWorkingInterval();
                addCWorkingInterval(bufferOfWH);
                addCNumberOfAssignedRecourse(resultOfOperationSetting.getResourceOfBooking());
                resultOfOperationSetting.getResourceOfBooking().setReleaseTime(1, bufferOfWH);
                //resultOfOperationSetting.getResourceOfBooking().setReleaseTime(bufferOfWH.getEndTime());
                if(!operationNotScheduled()) {
                    serialAffiliation.setСNumberOfAssignedOperations(serialAffiliation.getСNumberOfAssignedOperations() + 1);
                }
                break;
            }
        }
    }

    //Перенес в операцию с преоритетом
    public void installOperationForSpecificResource(IResource currentRecourse) {
        ResultOfOperationSetting resultOfOperationSetting = currentOperatingMode.installOperation(this, currentRecourse);
        if(resultOfOperationSetting != null)
        {
            WorkingHours bufferOfWH = resultOfOperationSetting.getWorkingInterval();
            addCWorkingInterval(bufferOfWH);
            addCNumberOfAssignedRecourse(resultOfOperationSetting.getResourceOfBooking());
            resultOfOperationSetting.getResourceOfBooking().setReleaseTime(1, bufferOfWH);
            //resultOfOperationSetting.getResourceOfBooking().setReleaseTime(bufferOfWH.getEndTime());
            if(!operationNotScheduled()) {
                serialAffiliation.setСNumberOfAssignedOperations(serialAffiliation.getСNumberOfAssignedOperations() + 1);
            }
            //System.out.println(this);
        }
    }

    @Override
    public void installOperationForSpecificResource(IResource currentRecourse, int numberOfOperations) {
        installOperationForSpecificResource(currentRecourse);
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

    //Reverse
    public void installReverseOperation() {
        for (IResource tactRecourse: resourceGroup.getRecoursesInTheGroup()) {
            ResultOfOperationSetting resultOfOperationSetting = currentOperatingMode.reverseInstallOperation(this, tactRecourse);
            if(resultOfOperationSetting != null) {
                WorkingHours bufferOfWH = resultOfOperationSetting.getWorkingInterval();
                addCWorkingInterval(bufferOfWH);
                addCNumberOfAssignedRecourse(resultOfOperationSetting.getResourceOfBooking());
                if(!operationNotScheduled()) {
                    serialAffiliation.setСNumberOfAssignedOperations(serialAffiliation.getСNumberOfAssignedOperations() + 1);
                }
                //underlyingOperation.serialAffiliation.setСNumberOfAssignedOperations(underlyingOperation.serialAffiliation.getСNumberOfAssignedOperations() + 1);
                break;
            }
        }
    }

    public void clean() {
        if(cNumberOfAssignedRecourse != null) {
            //cNumberOfAssignedRecourse.clean();
            for(IStructuralUnitOfResource resource: cNumberOfAssignedRecourse) {
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

    @Override
    public O_Basic clone() {
        return new O_Basic(this);
    }
}
