package com.company.operation;

import com.company.Group;
import com.company.Series;

import java.time.Duration;

public class O_TemperatureAndVoltage extends O_Basic {
    private double temperatureOfOperation;
    private double voltageOfOperation;

    public O_TemperatureAndVoltage(double temperatureOfOperation, double voltageOfOperation) {
        super();
        this.temperatureOfOperation = temperatureOfOperation;
        this.voltageOfOperation = voltageOfOperation;
    }

    O_TemperatureAndVoltage(O_TemperatureAndVoltage copyOperation) {
        super((O_Basic) copyOperation);
        temperatureOfOperation = copyOperation.temperatureOfOperation;
        voltageOfOperation = copyOperation.voltageOfOperation;
    }

    public O_TemperatureAndVoltage(Series serialAffiliation,
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
    public O_Basic clone() {
        return new O_TemperatureAndVoltage(this);
    }

    @Override
    public void installOperation() {
        super.installOperation();
    }
}
