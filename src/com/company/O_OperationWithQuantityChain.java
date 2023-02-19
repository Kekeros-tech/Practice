package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class O_OperationWithQuantityChain implements IOperation {
    StringBuffer nameOfOperation;
    ArrayList<Operation> operationsToCreate;

    O_OperationWithQuantityChain(Operation operationToFill, int numberOfOperations) {
        nameOfOperation = Series.generateRandomHexString(8);
        operationsToCreate = new ArrayList<>();
        //operationsToCreate.add(operationToFill);
        for(int i = 0; i < numberOfOperations; i++) {
            operationsToCreate.add(operationToFill.clone());
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Множественная операция ----- \n");
        for(Operation operation: operationsToCreate) {
            sb.append("|| ");
            sb.append(operation.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean isСanBePlacedInFront() {
        for(Operation operation: operationsToCreate) {
            if(operation.operationNotScheduled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setTactTime() {
        for(Operation operation: operationsToCreate) {
            operation.setTactTime();
        }
    }

    @Override
    public void setTactTime(LocalDateTime tactTime) {
        for(Operation operation: operationsToCreate) {
            if(operationNotScheduled()) {
                operation.setTactTime(tactTime);
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
    public Collection<Operation> getOperationsAtCore() {
        return null;
    }

    @Override
    public Duration getInitDurationOfExecution() {
        return operationsToCreate.get(0).getInitDurationOfExecution();
    }

    @Override
    public LocalDateTime getEarliestTimeOfWorkingInterval() {
        LocalDateTime minTime = LocalDateTime.MAX;
        for(Operation operation: operationsToCreate) {
            LocalDateTime currentWorkingInterval = operation.getEarliestTimeOfWorkingInterval();
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
        for(Operation operation: operationsToCreate) {
            operation.setPreviousOperation(previousOperation);
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
        for(Operation operation: operationsToCreate) {
            result.addAll(operation.getCWorkingInterval());
        }
        return result;
    }

    @Override
    public LocalDateTime getTactTime() {
        LocalDateTime minTactTime = LocalDateTime.MAX;
        for(Operation operation: operationsToCreate) {
            if(operation.getTactTime() == null) return null;
            if(operationNotScheduled() && operation.getTactTime().isBefore(minTactTime)) {
                minTactTime = operation.getTactTime();
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
        for(Operation operation : operationsToCreate) {
            if(operation.operationNotScheduled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setResourceGroup(Group resourceGroup) {
        for(Operation operation: operationsToCreate) {
            operation.setResourceGroup(resourceGroup);
        }
    }

    @Override
    public void setSerialAffiliation(Series serialAffiliation) {
        for(Operation operation: operationsToCreate) {
            operation.setSerialAffiliation(serialAffiliation);
        }
    }

    @Override
    public void setDurationOfExecution(Duration durationOfExecution) {
        for(Operation operation: operationsToCreate) {
            operation.setDurationOfExecution(durationOfExecution);
        }
    }

    @Override
    public void setOperatingMode(int currentOperatingMode) {
        for(Operation operation: operationsToCreate) {
            operation.setOperatingMode(currentOperatingMode);
        }
    }


    @Override
    public void addFollowingOperation(IOperation followingOperation) {
        for(Operation operation: operationsToCreate) {
            operation.addFollowingOperation(followingOperation);
        }
    }

    @Override
    public void setNewTactTime() {
        for(Operation operation: operationsToCreate) {
            if(operationNotScheduled()) {
                operation.setNewTactTime();
            }
        }
    }

    @Override
    public void setNewReverseTactTime() {
        for(Operation operation: operationsToCreate) {
            if(operationNotScheduled()) {
                operation.setNewReverseTactTime();
            }
        }
    }

    //--------
    //эта над этой частью надо подумать
    @Override
    public ArrayList<IStructuralUnitOfResource> getResourcesToBorrow() {
        HashSet<IStructuralUnitOfResource> resultOfWork = new HashSet<>();
        for(Operation operation: operationsToCreate) {
            if(operation.operationNotScheduled()) {
                resultOfWork.addAll(operation.getResourcesToBorrow());
            }
        }
        return new ArrayList<>(resultOfWork);
    }

    @Override
    public int getCountOfOperations() {
        LocalDateTime tactTime = getTactTime();
        int count = 0;
        for(Operation operation: operationsToCreate) {
            if(operation.operationNotScheduled() && operation.getTactTime().isEqual(tactTime)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void installOperation() {
        for(Operation operation: operationsToCreate) {
            if(operation.operationNotScheduled()) {
                operation.installOperation();
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
        for(Operation operation: operationsToCreate) {
            if(operation.getTactTime().isEqual(currentTactTime) && operation.operationNotScheduled()) {
                operation.installOperationForSpecificResource(currentRecourse);
                countInstalling++;
            }
            if(countInstalling == numberOfOperations) {
                break;
            }
        }
    }

    @Override
    public void installReverseOperation() {
        for(Operation operation: operationsToCreate) {
            if(operation.operationNotScheduled()) {
                operation.installReverseOperation();
            }
        }
    }

    @Override
    public void setTactTimeByEndTimeOfPrevious() {
        for(Operation operation: operationsToCreate) {
            operation.setTactTimeByEndTimeOfPrevious();
        }
    }

    @Override
    public boolean isCanBePlacedInReverseFront() {
        for(Operation operation: operationsToCreate) {
            if(operation.isCanBePlacedInReverseFront()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clean() {
        for(Operation operation: operationsToCreate) {
            operation.clean();
        }
    }

    @Override
    public void fullClean() {
        for(Operation operation: operationsToCreate) {
            operation.fullClean();
        }
    }
}
