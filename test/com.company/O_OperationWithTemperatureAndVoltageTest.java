package com.company;

import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class O_OperationWithTemperatureAndVoltageTest {

    @Test
    public void firstEasyTestByOneOperation() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        CellWithVoltage cellWithVoltage = new CellWithVoltage(10, 2, "14-08-2021 09:00");
        ArrayList<CellWithVoltage> cells = new ArrayList<>();
        cells.add(cellWithVoltage);
        CellElectricFurnace cellElectricFurnace = new CellElectricFurnace(10, cells);

        R_ElectricFurnace firstRecourse = new R_ElectricFurnace("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);
        firstRecourse.addCellElectricFurnace(cellElectricFurnace);

        Group groupOfRes = new Group();
        groupOfRes.addRecourseInTheGroup(firstRecourse);

        Operation operation = new O_OperationWithTemperatureAndVoltage(10, 10);
        operation.setResourceGroup(groupOfRes);
        operation.setDurationOfExecution(Duration.ofHours(10));
        operation.setOperatingMode(1);

        Operation operationForSecondSeries = new O_OperationWithTemperatureAndVoltage(10, 10);
        operationForSecondSeries.setResourceGroup(groupOfRes);
        operationForSecondSeries.setDurationOfExecution(Duration.ofHours(4));
        operationForSecondSeries.setOperatingMode(0);

        ArrayList<IOperation> operationsForFirstSeries = new ArrayList<>();
        operationsForFirstSeries.add(operation);

        ArrayList<IOperation> operationsForSecondSeries = new ArrayList<>();
        operationsForSecondSeries.add(operationForSecondSeries);

        Series firstSeries = new Series(operationsForFirstSeries, "01-09-2021 00:00", "15-08-2021 09:00");
        operation.setSerialAffiliation(firstSeries);

        Series secondSeries = new Series(operationsForSecondSeries, "01-09-2021 00:00", "15-08-2021 10:00");
        operationForSecondSeries.setSerialAffiliation(secondSeries);

        ArrayList<Series> series = new ArrayList<>();
        series.add(firstSeries);
        series.add(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Main.takeSeriesToWork(series);

        operationsForFirstSeries.add(operationForSecondSeries);

        for(IOperation current: operationsForFirstSeries) {
            //System.out.println(current.getCNumberOfAssignedRecourse());
            System.out.println(current);
        }


    }
}
