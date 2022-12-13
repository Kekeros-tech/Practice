package com.company;

import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class O_OperationWithQuantityChainTest {

    @Test
    public void OperationDefinition() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        Recourse firstRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        Recourse secondRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        Group firstRes = new Group();
        firstRes.addRecourseInTheGroup(firstRecourse);


        Operation firstOperation = new Operation();
        firstOperation.setResourceGroup(firstRes);
        firstOperation.setDurationOfExecution(Duration.ofHours(3));
        firstOperation.setOperatingMode(0);

        ArrayList<IOperation> operations = new ArrayList<>();
        operations.add(firstOperation);

        Series mySeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 10:00");
        firstOperation.setSerialAffiliation(mySeries);

        O_OperationWithQuantityChain resultOfWork = new O_OperationWithQuantityChain(firstOperation, 10);

        firstRes.addRecourseInTheGroup(secondRecourse);
    }

    @Test
    public void seriesConsistsOfOneOperationWithQuantity() {
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

        Group firstRes = new Group();
        firstRes.addRecourseInTheGroup(firstRecourse);

        Series mySeries = new Series("30-08-2021 00:00", "15-08-2021 10:00");

        Operation firstOperation = new O_OperationWithTemperatureAndVoltage(10, 10);
        firstOperation.setResourceGroup(firstRes);
        firstOperation.setDurationOfExecution(Duration.ofHours(10));
        firstOperation.setOperatingMode(1);
        firstOperation.setSerialAffiliation(mySeries);

        O_OperationWithQuantityChain operationChain = new O_OperationWithQuantityChain(firstOperation, 5);

        Collection<IOperation> operations = new ArrayList<>();
        operations.add(operationChain);

        mySeries.setOperationsToCreate(operations);

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(mySeries);

        firstRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());

        Main.takeSeriesToWork(seriesToWork);

        for(Series currentSeries: seriesToWork) {
            for(IOperation currentOperationFromSeries: currentSeries.getOperationsToCreate()){
                System.out.println(currentOperationFromSeries);
            }
        }
    }

    @Test
    public void seriesConsistsOfOneOperationWithQuantityAndOneFollowingOperation() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        Recourse justRes = new Recourse("14-08-2021 09:00");
        justRes.addSchedule(workingHoursForFirst0);
        justRes.addSchedule(workingHoursForFirst1);

        CellWithVoltage cellWithVoltage = new CellWithVoltage(10, 2, "14-08-2021 09:00");
        ArrayList<CellWithVoltage> cells = new ArrayList<>();
        cells.add(cellWithVoltage);
        CellElectricFurnace cellElectricFurnace = new CellElectricFurnace(10, cells);

        R_ElectricFurnace firstRecourse = new R_ElectricFurnace("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);
        firstRecourse.addCellElectricFurnace(cellElectricFurnace);

        Group firstRes = new Group();
        firstRes.addRecourseInTheGroup(firstRecourse);

        Group simpleRes = new Group();
        simpleRes.addRecourseInTheGroup(justRes);

        Series mySeries = new Series("30-08-2021 00:00", "15-08-2021 10:00");

        Operation firstOperation = new O_OperationWithTemperatureAndVoltage(10, 10);
        firstOperation.setResourceGroup(firstRes);
        firstOperation.setDurationOfExecution(Duration.ofHours(10));
        firstOperation.setOperatingMode(1);
        firstOperation.setSerialAffiliation(mySeries);

        Operation followingOperation = new Operation();
        followingOperation.setResourceGroup(simpleRes);
        followingOperation.setDurationOfExecution(Duration.ofHours(4));
        followingOperation.setOperatingMode(0);
        followingOperation.setSerialAffiliation(mySeries);

        O_OperationWithQuantityChain operationChain = new O_OperationWithQuantityChain(firstOperation, 5);

        operationChain.addFollowingOperation(followingOperation);

        Collection<IOperation> operations = new ArrayList<>();
        operations.add(operationChain);
        operations.add(followingOperation);

        mySeries.setOperationsToCreate(operations);

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(mySeries);

        firstRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        justRes.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());

        Main.takeSeriesToWork(seriesToWork);

        for(Series currentSeries: seriesToWork) {
            for(IOperation currentOperationFromSeries: currentSeries.getOperationsToCreate()){
                System.out.println(currentOperationFromSeries);
            }
        }
    }
}