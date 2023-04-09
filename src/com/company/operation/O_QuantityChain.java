package com.company.operation;

import com.company.*;
import com.company.recourse.IResource;
import com.company.recourse.IStructuralUnitOfResource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class O_QuantityChain implements IOperation {
    StringBuffer nameOfOperation;
    ArrayList<O_Basic> operationsToCreate;

    public O_QuantityChain(O_Basic OBasicToFill, int numberOfOperations) {
        nameOfOperation = Series.generateRandomHexString(8);
        operationsToCreate = new ArrayList<>();
        //operationsToCreate.add(operationToFill);
        for(int i = 0; i < numberOfOperations; i++) {
            operationsToCreate.add(OBasicToFill.clone());
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Множественная операция ----- \n");
        for(O_Basic OBasic : operationsToCreate) {
            sb.append("|| ");
            sb.append(OBasic.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String formResultOfOperation() {
        final StringBuffer sb = new StringBuffer("");
        sb.append(nameOfOperation).append(";");
        for(O_Basic oper : operationsToCreate) {
            ArrayList<IStructuralUnitOfResource> resources = new ArrayList<>(oper.getCNumberOfAssignedRecourse());
            for(int i = 0; i < resources.size(); i ++) {
                sb.append(resources.get(i)).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(";");
        for(O_Basic oper : operationsToCreate) {
            ArrayList<WorkingHours> workingHours = new ArrayList<>(oper.getCWorkingInterval());
            for(int i = 0; i < workingHours.size(); i ++) {
                sb.append(workingHours.get(i)).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(";");
        return sb.toString();
    }

    @Override
    public boolean isСanBePlacedInFront() {
        for(O_Basic OBasic : operationsToCreate) {
            if(OBasic.operationNotScheduled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setTactTime() {
        for(O_Basic OBasic : operationsToCreate) {
            OBasic.setTactTime();
        }
    }

    @Override
    public void setTactTime(LocalDateTime tactTime) {
        for(O_Basic OBasic : operationsToCreate) {
            if(operationNotScheduled()) {
                OBasic.setTactTime(tactTime);
            }
        }
    }

    @Override
    public Series getSerialAffiliation() {
        return operationsToCreate.get(0).getSerialAffiliation();
    }

    @Override
    public IOperationMode getCurrentOperatingMode() {
        return operationsToCreate.get(0).getCurrentOperatingMode();
    }

    @Override
    public Collection<O_Basic> getOperationsAtCore() {
        return operationsToCreate;
    }

    @Override
    public Duration getInitDurationOfExecution() {
        return operationsToCreate.get(0).getInitDurationOfExecution();
    }

    @Override
    public LocalDateTime getEarliestTimeOfWorkingInterval() {
        LocalDateTime minTime = LocalDateTime.MAX;
        for(O_Basic OBasic : operationsToCreate) {
            LocalDateTime currentWorkingInterval = OBasic.getEarliestTimeOfWorkingInterval();
            if(currentWorkingInterval.isBefore(minTime)) {
                minTime = currentWorkingInterval;
            }
        }
        return minTime;
    }

    @Override
    public boolean allFollowingAssigned() {
        return operationsToCreate.get(0).allFollowingAssigned();
    }

    @Override
    public Group getResourceGroup() {
        return operationsToCreate.get(0).getResourceGroup();
    }

    @Override
    public void setNameOfOperation(String nameOfOperation) {
        this.nameOfOperation = new StringBuffer(nameOfOperation);
    }

    @Override
    public ArrayList<IOperation> getFollowingOperations() {
        return operationsToCreate.get(0).getFollowingOperations();
    }

    @Override
    public ArrayList<IOperation> getPreviousOperations() {
        return operationsToCreate.get(0).getPreviousOperations();
    }

    @Override
    public void setPreviousOperation(ArrayList<IOperation> previousOperation) {
        for(O_Basic OBasic : operationsToCreate) {
            OBasic.setPreviousOperation(previousOperation);
        }
    }

    @Override
    public int getNumberOfOperationMode() {
        return operationsToCreate.get(0).getNumberOfOperationMode();
    }


    //todo - сделать более удобное представление результата расстановки операций
    @Override
    public ArrayList<WorkingHours> getCWorkingInterval() {
        ArrayList<WorkingHours> result = new ArrayList<>();
        for(O_Basic OBasic : operationsToCreate) {
            result.addAll(OBasic.getCWorkingInterval());
        }
        return result;
    }

    @Override
    public LocalDateTime getTactTime() {
        LocalDateTime minTactTime = LocalDateTime.MAX;
        for(O_Basic OBasic : operationsToCreate) {
            if(OBasic.getTactTime() == null) return null;
            if(operationNotScheduled() && OBasic.getTactTime().isBefore(minTactTime)) {
                minTactTime = OBasic.getTactTime();
            }
        }
        return minTactTime;
    }

    //-------
    //Данная операция не использует durationOfExecution;
    @Override
    public Duration getDurationOfExecution() {
        return null;
    }

    @Override
    public boolean operationNotScheduled() {
        for(O_Basic OBasic : operationsToCreate) {
            if(OBasic.operationNotScheduled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setResourceGroup(Group resourceGroup) {
        for(O_Basic OBasic : operationsToCreate) {
            OBasic.setResourceGroup(resourceGroup);
        }
    }

    @Override
    public void setSerialAffiliation(Series serialAffiliation) {
        for(O_Basic OBasic : operationsToCreate) {
            OBasic.setSerialAffiliation(serialAffiliation);
        }
    }

    @Override
    public void setDurationOfExecution(Duration durationOfExecution) {
        for(O_Basic OBasic : operationsToCreate) {
            OBasic.setDurationOfExecution(durationOfExecution);
        }
    }

    @Override
    public void setOperatingMode(int currentOperatingMode) {
        for(O_Basic OBasic : operationsToCreate) {
            OBasic.setOperatingMode(currentOperatingMode);
        }
    }


    @Override
    public void addFollowingOperation(IOperation followingOperation) {
        for(O_Basic OBasic : operationsToCreate) {
            OBasic.addFollowingOperation(followingOperation);
        }
    }

    @Override
    public void setNewTactTime() {
        for(O_Basic OBasic : operationsToCreate) {
            if(operationNotScheduled()) {
                OBasic.setNewTactTime();
            }
        }
    }

    @Override
    public void setNewReverseTactTime() {
        for(O_Basic OBasic : operationsToCreate) {
            if(operationNotScheduled()) {
                OBasic.setNewReverseTactTime();
            }
        }
    }

    //--------
    //эта над этой частью надо подумать
    @Override
    public ArrayList<IStructuralUnitOfResource> getResourcesToBorrow() {
        HashSet<IStructuralUnitOfResource> resultOfWork = new HashSet<>();
        for(O_Basic OBasic : operationsToCreate) {
            if(OBasic.operationNotScheduled()) {
                resultOfWork.addAll(OBasic.getResourcesToBorrow());
            }
        }
        return new ArrayList<>(resultOfWork);
    }

    @Override
    public int getCountOfOperations() {
        LocalDateTime tactTime = getTactTime();
        int count = 0;
        for(O_Basic OBasic : operationsToCreate) {
            if(OBasic.operationNotScheduled() && OBasic.getTactTime().isEqual(tactTime)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void installOperation() {
        for(O_Basic OBasic : operationsToCreate) {
            if(OBasic.operationNotScheduled()) {
                OBasic.installOperation();
            }
        }
    }

    @Override
    public void installOperationForSpecificResource(IResource currentRecourse) {

    }

    @Override
    public void installOperationForSpecificResource(IResource currentRecourse, int numberOfOperations) {
        LocalDateTime currentTactTime = getTactTime();
        int countInstalling = 0;
        for(O_Basic OBasic : operationsToCreate) {
            if(OBasic.getTactTime().isEqual(currentTactTime) && OBasic.operationNotScheduled()) {
                OBasic.installOperationForSpecificResource(currentRecourse);
                countInstalling++;
            }
            if(countInstalling == numberOfOperations) {
                break;
            }
        }
    }

    @Override
    public void installReverseOperation() {
        for(O_Basic OBasic : operationsToCreate) {
            if(OBasic.operationNotScheduled()) {
                OBasic.installReverseOperation();
            }
        }
    }

    @Override
    public void setTactTimeByEndTimeOfPrevious() {
        for(O_Basic OBasic : operationsToCreate) {
            OBasic.setTactTimeByEndTimeOfPrevious();
        }
    }

    @Override
    public boolean isCanBePlacedInReverseFront() {
        for(O_Basic OBasic : operationsToCreate) {
            if(OBasic.isCanBePlacedInReverseFront()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clean() {
        for(O_Basic OBasic : operationsToCreate) {
            OBasic.clean();
        }
    }

    @Override
    public void fullClean() {
        for(O_Basic OBasic : operationsToCreate) {
            OBasic.fullClean();
        }
    }
}
