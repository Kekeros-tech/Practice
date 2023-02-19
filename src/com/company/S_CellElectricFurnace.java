package com.company;

import java.util.ArrayList;
import java.util.Collection;

public class S_CellElectricFurnace {
    double temperature;
    ArrayList<S_CellWithVoltage> settingsForCellWithVoltage;

    S_CellElectricFurnace(double temperature, Collection<S_CellWithVoltage> settingsForCellWithVoltage) {
        this.temperature = temperature;
        this.settingsForCellWithVoltage = new ArrayList<>(settingsForCellWithVoltage);
    }

    public double getTemperature() {
        return temperature;
    }

    public ArrayList<S_CellWithVoltage> getSettingsForCellWithVoltage() {
        return settingsForCellWithVoltage;
    }

    public S_CellWithVoltage getSettingsForCellWithVoltage(int index) {
        return settingsForCellWithVoltage.get(index);
    }
}
