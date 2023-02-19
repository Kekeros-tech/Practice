package com.company;

public class S_CellWithVoltage {
    private int cellSize;
    private double voltage;

    S_CellWithVoltage(int cellSize, double voltage) {
        this.cellSize = cellSize;
        this.voltage = voltage;
    }

    public double getVoltage() { return voltage; }

    public int getCellSize() { return cellSize; }
}
