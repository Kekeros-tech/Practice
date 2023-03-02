package com.company.recourse.electric_furnace;

import java.util.Collection;

public class CellElectricFurnace {
    private double temperature;
    private Collection<CellWithVoltage> cellsWithVoltage;

    public double getTemperature() { return temperature; }
    public Collection<CellWithVoltage> getCellsWithVoltage() { return cellsWithVoltage; }

    public CellElectricFurnace(double temperature, Collection<CellWithVoltage> cellsWithVoltage) {
        this.temperature = temperature;
        this.cellsWithVoltage = cellsWithVoltage;
    }
}
