package com.company;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

public class O_OperationWithPriorityAndQuantity extends OperationWithPriorityNew {
    private double requiredTemperature;
    private double requiredVoltage;
    private int requiredCapacity;

    private int CPartsProcessed;

    public double getRequiredTemperature() { return requiredTemperature; }
    public double getRequiredVoltage() { return requiredVoltage; }
    public double getRequiredCapacity() { return requiredCapacity; }
    public int getCPartsProcessed() { return CPartsProcessed; }

    O_OperationWithPriorityAndQuantity(Group                 resourceGroup,
                                       Series                serialAffiliation,
                                       Collection<Operation> previousOperations,
                                       Collection<Operation> followingOperations,
                                       Duration              durationOfExecution,
                                       int                   currentOperatingMode,
                                       PriorityType          priority,
                                       double                requiredTemperature,
                                       double                requiredVoltage,
                                       int                   requiredCapacity) {
        super(resourceGroup, serialAffiliation, previousOperations, followingOperations, durationOfExecution, currentOperatingMode, priority);
        this.requiredTemperature = requiredTemperature;
        this.requiredCapacity = requiredCapacity;
        this.requiredVoltage = requiredVoltage;
        this.CPartsProcessed = 0;
    }

    public O_OperationWithPriorityAndQuantity() {
        super();
    }

    public void setCPartsProcessed(int CPartsProcessed) { this.CPartsProcessed = CPartsProcessed; }
    public void setRequiredCapacity(int requiredCapacity) { this.requiredCapacity = requiredCapacity; }
    public void setRequiredTemperature(double requiredTemperature) { this.requiredTemperature = requiredTemperature; }
    public void setRequiredVoltage(double requiredVoltage) { this.requiredVoltage = requiredVoltage; }

    @Override
    public ArrayList<IResource> getResourcesToBorrow() {
        ArrayList<IResource> recoursesToBorrow = new ArrayList<>();

        for(IResource recourse: resourceGroup.getRecoursesInTheGroup()) {
            switch (currentOperatingMode) {
                case canNotBeInterrupted: {
                    if(recourse.takeWhichCanNotBeInterrupted(this) != null){
                        recoursesToBorrow.add(recourse);
                    }
                    break;
                }
                case canBeInterrupted: {
                    if(recourse.takeWhichCanBeInterrupted(this) != null) {
                        recoursesToBorrow.add(recourse);
                    }
                    break;
                }
            }
        }
        return recoursesToBorrow;
    }
}
