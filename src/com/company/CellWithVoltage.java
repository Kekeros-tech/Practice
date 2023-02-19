package com.company;

import java.time.LocalDateTime;

public class CellWithVoltage implements IStructuralUnitOfResource{
    private double voltage;
    private int capacity;

    //не нравится такое решение
    private IResource coreResource;

    //private int CCellCurrentOccupancy;
    private CurrentCellLoad cellOccupancy;
    private LocalDateTime arrivalTime;
    //private LocalDateTime releaseTime;


    public void setCoreResource(IResource coreResource) { this.coreResource = coreResource; }

    public double getVoltage() { return voltage; }
    public int getCapacity() { return capacity; }
    public IResource getCoreResource() { return coreResource; }
    public LocalDateTime getReleaseTime() { return cellOccupancy.getLastReleaseDate(); }
    public LocalDateTime getReleaseTime(LocalDateTime tactTime) {
        LocalDateTime currentReleaseTime = cellOccupancy.getCurrentReleaseTimeAtPointInTime(tactTime);
        if(currentReleaseTime == LocalDateTime.MIN) {
            return arrivalTime;
        }
        return cellOccupancy.getCurrentReleaseTimeAtPointInTime(tactTime);
    }

    public void setReleaseTime(LocalDateTime releaseTime) {
        /*if(releaseTime.isAfter(releaseTime)) {
            CCellCurrentOccupancy = 0;
        }
        CCellCurrentOccupancy++;
        this.releaseTime = releaseTime;*/
    }

    @Override
    public void setReleaseTime(int count, WorkingHours workingHours) {
        cellOccupancy.addCurrentCellLoads(new ResultOfCurrentCellLoad(count, workingHours));
    }

    @Override
    public int getResourceAmount(LocalDateTime tactTime) {
        int result = capacity - cellOccupancy.getCurrentCellLoadsAtPointInTime(tactTime);
        if(result < 0) {
            result = 0;
        }
        return result;
    }

    public CellWithVoltage(double voltage, int capacity, String arrivalTime) {
        this.voltage = voltage;
        this.capacity = capacity;
        //CCellCurrentOccupancy = 0;
        this.arrivalTime = LocalDateTime.parse(arrivalTime, WorkingHours.formatter);
        this.cellOccupancy = new CurrentCellLoad(
                new ResultOfCurrentCellLoad(0, new WorkingHours(arrivalTime, arrivalTime)));

        //this.releaseTime = LocalDateTime.parse(arrivalTime, WorkingHours.formatter);
    }

    public CellWithVoltage(S_CellWithVoltage settings, LocalDateTime arrivalTime, IResource coreResource) {
        this.voltage = settings.getVoltage();
        this.capacity = settings.getCellSize();
        this.arrivalTime = arrivalTime;
        this.coreResource = coreResource;
        this.cellOccupancy = new CurrentCellLoad(
                new ResultOfCurrentCellLoad(0, new WorkingHours(arrivalTime, arrivalTime))
        );
    }

    public boolean canFitOneItem(LocalDateTime tactTime) {
        int currentEmployment = cellOccupancy.getCurrentCellLoadsAtPointInTime(tactTime);
        if(currentEmployment < capacity) {
            return true;
        }
        return false;
        /*int currentEmployment = CCellCurrentOccupancy;
        if(tactTime.isAfter(releaseTime)) {
            currentEmployment = 0;
        }
        if(tactTime.isAfter(releaseTime)) {

        }
        if(CCellCurrentOccupancy != capacity) {
            return true;
        }
        return false;*/
    }

    public boolean isFree(LocalDateTime tactTime) {
        LocalDateTime releaseTime = cellOccupancy.getCurrentReleaseTimeAtPointInTime(tactTime);
        if(tactTime.isBefore(releaseTime)) {
            return false;
        }
        return true;
        /*if(releaseTime == null) return true;
        if(tactTime != null && tactTime.isBefore(releaseTime)) {
            return false;
        }
        return true;*/
    }

    public int takeСell(int capacity) {
        int result = this.capacity - capacity;
        if(result < 0)
        {
            return this.capacity;
        }
        return result;
    }

    @Override
    public void clean() {
        this.cellOccupancy = new CurrentCellLoad(
                new ResultOfCurrentCellLoad(0, new WorkingHours(arrivalTime, arrivalTime)));
    }
}
