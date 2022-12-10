package com.company;

public class O_OperationWithTemperatureAndVoltage extends Operation {
    private double temperatureOfOperation;
    private double voltageOfOperation;

    O_OperationWithTemperatureAndVoltage(double temperatureOfOperation, double voltageOfOperation) {
        this.temperatureOfOperation = temperatureOfOperation;
        this.voltageOfOperation = voltageOfOperation;
    }

    public double getTemperatureOfOperation() { return temperatureOfOperation; }
    public double getVoltageOfOperation() { return voltageOfOperation; }

    public void setTemperatureOfOperation(double temperatureOfOperation) { this.temperatureOfOperation = temperatureOfOperation; }
    public void setVoltageOfOperation(double voltageOfOperation) { this.voltageOfOperation = voltageOfOperation; }
}
