package com.company;

import java.time.LocalDateTime;

public class CellWithVoltage {
    private double voltage;
    private int capacity;

    private int CCellСurrentOccupancy;
    private LocalDateTime releaseTime;

    public double getVoltage() { return voltage; }
    public int getCapacity() { return capacity; }
    public LocalDateTime getReleaseTime() {return releaseTime; }

    public void setReleaseTime(LocalDateTime releaseTime) { this.releaseTime = releaseTime; }
    public void setVoltage(double voltage) { this.voltage = voltage; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public void setCCellСurrentOccupancy(int CCellСurrentOccupancy) { this.CCellСurrentOccupancy = CCellСurrentOccupancy; }

    public CellWithVoltage(double voltage, int capacity) {
        this.voltage = voltage;
        this.capacity = capacity;
        CCellСurrentOccupancy = 0;
        releaseTime = null;
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

}
