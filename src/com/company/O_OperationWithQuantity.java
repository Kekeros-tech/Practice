package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class O_OperationWithQuantity extends Operation {
    private double requiredTemperature;
    private double requiredVoltage;
    private int quantity;

    private int CNumberOfProcessedParts;
    private Collection<IResource> CAssignedResources;
    private Collection<WorkingHours> CWorkingIntervals;

    public double getRequiredTemperature() { return requiredTemperature; }
    public double getRequiredVoltage() { return requiredVoltage; }
    public int getQuantity() { return quantity; }
    public int getCNumberOfProcessedParts() {return CNumberOfProcessedParts; }
    public Collection<IResource> getCAssignedResources() {return CAssignedResources; }
    public Collection<WorkingHours> getCWorkingIntervals() { return CWorkingIntervals; }

    public void setRequiredTemperature(double requiredTemperature) {this.requiredTemperature = requiredTemperature;}
    public void setRequiredVoltage(double voltage) { this.requiredVoltage = voltage; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCNumberOfProcessedParts(int quantity) { this.CNumberOfProcessedParts = quantity; }
    public void setCAssignedResources(Collection<IResource> resources) { this.CAssignedResources = resources; }
    public void setCWorkingIntervals(Collection<WorkingHours> CWorkingIntervals) { this.CWorkingIntervals = CWorkingIntervals; }

    /*O_OperationWithQuantity(Group                 resourceGroup,
                            Series                serialAffiliation,
                            Collection<Operation> previousOperations,
                            Collection<Operation> followingOperations,
                            Duration              durationOfExecution,
                            int                   currentOperatingMode,
                            int                   quantity) {
        super(resourceGroup, serialAffiliation, previousOperations, followingOperations, durationOfExecution, currentOperatingMode);
        this.quantity = quantity;
        CNumberOfProcessedParts = 0;
        CAssignedResources = new ArrayList<>();
        CWorkingIntervals = new ArrayList<>();
    }*/

    O_OperationWithQuantity() {
        super();
        quantity = 0;
        CNumberOfProcessedParts = 0;
        CAssignedResources = new ArrayList<>();
        CWorkingIntervals = new ArrayList<>();
    }

    public void addAssignedResource(IResource resource) {
        CAssignedResources.add(resource);
    }

    public void addWorkingInterval(WorkingHours workingHours) {
        CWorkingIntervals.add(workingHours);
    }

    public void handleDetails(int countOfDetails){
        CNumberOfProcessedParts += countOfDetails;
    }

    public int getCountOfPartsToProcess() {
        return quantity - CNumberOfProcessedParts;
    }

    public boolean limitOnAssignedResource() {
        if(CAssignedResources == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean operationNotScheduled() {
        if(CNumberOfProcessedParts != quantity) {
            return true;
        }
        else return false;
    }

    public void installOperation() {
        /*switch (currentOperatingMode) {
            case canNotBeInterrupted: {
                for(IResource recourse: resourceGroup.getRecoursesInTheGroup()) {
                    recourse.takeResWhichCanNotBeInterrupted(this);
                }
            }
            case canBeInterrupted: {

            }
        }*/
    }
}
