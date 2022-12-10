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

        ArrayList<IOperation> operations = new ArrayList<>();
        operations.add(operation);

        Series firstSeries = new Series(operations, "01-09-2021 00:00", "15-08-2021 09:00");
        operation.setSerialAffiliation(firstSeries);

        ArrayList<Series> series = new ArrayList<>();
        series.add(firstSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Main.takeSeriesToWork(series);

        System.out.println(operation.getCNumberOfAssignedRecourse());
        System.out.println(operation.getCWorkingInterval());
    }
}
