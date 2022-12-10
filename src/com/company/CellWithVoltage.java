package com.company;

import java.time.LocalDateTime;

public class CellWithVoltage implements IStructuralUnitOfResource{
    private double voltage;
    private int capacity;

    private int CCellСurrentOccupancy;
    private LocalDateTime arrivalTime;
    private LocalDateTime releaseTime;

    public double getVoltage() { return voltage; }
    public int getCapacity() { return capacity; }
    public LocalDateTime getReleaseTime() {return releaseTime; }

    public void setReleaseTime(LocalDateTime releaseTime) { this.releaseTime = releaseTime; }
    public void setVoltage(double voltage) { this.voltage = voltage; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public void setCCellСurrentOccupancy(int CCellСurrentOccupancy) { this.CCellСurrentOccupancy = CCellСurrentOccupancy; }

    public CellWithVoltage(double voltage, int capacity, String arrivalTime) {
        this.voltage = voltage;
        this.capacity = capacity;
        CCellСurrentOccupancy = 0;
        this.arrivalTime = LocalDateTime.parse(arrivalTime, WorkingHours.formatter);
        this.releaseTime = LocalDateTime.parse(arrivalTime, WorkingHours.formatter);
    }

    public boolean canFitOneItem() {
        if(CCellСurrentOccupancy != capacity) {
            return true;
        }
        return false;
    }

    public boolean isFree(LocalDateTime tactTime) {
        if(releaseTime == null) return true;
        if(tactTime != null && tactTime.isBefore(releaseTime)) {
            return false;
        }
        return true;
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
        this.releaseTime = arrivalTime;
    }
}
