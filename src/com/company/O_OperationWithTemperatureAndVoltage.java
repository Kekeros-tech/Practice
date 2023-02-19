package com.company;

import java.time.Duration;

public class O_OperationWithTemperatureAndVoltage extends Operation {
    private double temperatureOfOperation;
    private double voltageOfOperation;

    O_OperationWithTemperatureAndVoltage(double temperatureOfOperation, double voltageOfOperation) {
        super();
        this.temperatureOfOperation = temperatureOfOperation;
        this.voltageOfOperation = voltageOfOperation;
    }

    O_OperationWithTemperatureAndVoltage(O_OperationWithTemperatureAndVoltage copyOperation) {
        super((Operation) copyOperation);
        temperatureOfOperation = copyOperation.temperatureOfOperation;
        voltageOfOperation = copyOperation.voltageOfOperation;
    }

    O_OperationWithTemperatureAndVoltage(Series serialAffiliation,
                                         Group resourceGroup,
                                         Duration durationOfExecution,
                                         int currentOperationMode,
                                         double temperatureOfOperation,
                                         double voltageOfOperation) {
        super(serialAffiliation, resourceGroup, durationOfExecution, currentOperationMode);
        this.temperatureOfOperation = temperatureOfOperation;
        this.voltageOfOperation = voltageOfOperation;
    }

    public double getTemperatureOfOperation() { return temperatureOfOperation; }
    public double getVoltageOfOperation() { return voltageOfOperation; }

    public void setTemperatureOfOperation(double temperatureOfOperation) { this.temperatureOfOperation = temperatureOfOperation; }
    public void setVoltageOfOperation(double voltageOfOperation) { this.voltageOfOperation = voltageOfOperation; }

    @Override
    public Operation clone() {
        return new O_OperationWithTemperatureAndVoltage(this);
    }

    @Override
    public void installOperation() {
        super.installOperation();
    }
}
