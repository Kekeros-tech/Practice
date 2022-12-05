package com.company;

import java.util.Collection;

public class CellElectricFurnace {
    private double temperature;
    private Collection<CellWithVoltage> cellsWithVoltage;

    public double getTemperature() { return temperature; }
    public Collection<CellWithVoltage> getCellsWithVoltage() { return cellsWithVoltage; }

    CellElectricFurnace(double temperature, Collection<CellWithVoltage> cellsWithVoltage) {
        this.temperature = temperature;
        this.cellsWithVoltage = cellsWithVoltage;
    }
}
