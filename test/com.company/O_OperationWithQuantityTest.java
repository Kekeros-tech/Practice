package com.company;

import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class O_OperationWithQuantityTest {

    @Test
    public void easyTestForOneOperation() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        CellWithVoltage cellWithVoltage = new CellWithVoltage(12, 200);
        ArrayList<CellWithVoltage> cells = new ArrayList<>();
        cells.add(cellWithVoltage);
        CellElectricFurnace cellElectricFurnace = new CellElectricFurnace(25, cells);

        R_ElectricFurnace resource = new R_ElectricFurnace("14-08-2021 09:00");
        resource.addSchedule(workingHoursForFirst0);
        resource.addSchedule(workingHoursForFirst1);
        resource.addCellElectricFurnace(cellElectricFurnace);

        Group justFirst = new Group();
        justFirst.addRecourseInTheGroup(resource);

        O_OperationWithQuantity operation = new O_OperationWithQuantity();
        operation.setResourceGroup(justFirst);
        operation.setDurationOfExecution(Duration.ofHours(3));
        operation.setOperatingMode(0);
        operation.setQuantity(100);
        operation.setRequiredTemperature(25);
        operation.setRequiredVoltage(12);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(operation);

        Series mySeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 10:00");
        operation.setSerialAffiliation(mySeries);

        resource.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());

        ArrayList<Series> seriesForWork = new ArrayList<>();
        seriesForWork.add(mySeries);

        OperationsArrangementAlgorithm algo = new OperationsArrangementAlgorithm(seriesForWork);
        algo.takeSeriesToWork();

        for(Operation oper: operations) {
            for(WorkingHours workingHours: ((O_OperationWithQuantity) oper).getCWorkingIntervals()){
                System.out.println(workingHours.toString());
            }
        }
    }
}