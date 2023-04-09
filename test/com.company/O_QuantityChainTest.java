package com.company;

import com.company.arrangement_algo.Algo_WithCPAndFuture;
import com.company.control_param.ControlParameters;
import com.company.operation.*;
import com.company.param_selection_algo.PSA_byEGOServer;
import com.company.recourse.*;
import com.company.recourse.electric_furnace.*;
import com.company.recourse.pmc_machine.Recourse;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

public class O_QuantityChainTest {

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


        O_Basic firstOBasic = new O_Basic();
        firstOBasic.setResourceGroup(firstRes);
        firstOBasic.setDurationOfExecution(Duration.ofHours(3));
        firstOBasic.setOperatingMode(0);

        ArrayList<IOperation> operations = new ArrayList<>();
        operations.add(firstOBasic);

        Series mySeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 10:00");
        firstOBasic.setSerialAffiliation(mySeries);

        O_QuantityChain resultOfWork = new O_QuantityChain(firstOBasic, 10);

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

        O_Basic firstOBasic = new O_TemperatureAndVoltage(10, 10);
        firstOBasic.setResourceGroup(firstRes);
        firstOBasic.setDurationOfExecution(Duration.ofHours(10));
        firstOBasic.setOperatingMode(1);
        firstOBasic.setSerialAffiliation(mySeries);

        O_QuantityChain operationChain = new O_QuantityChain(firstOBasic, 5);

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

        O_Basic firstOBasic = new O_TemperatureAndVoltage(10, 10);
        firstOBasic.setResourceGroup(firstRes);
        firstOBasic.setDurationOfExecution(Duration.ofHours(10));
        firstOBasic.setOperatingMode(1);
        firstOBasic.setSerialAffiliation(mySeries);

        O_Basic followingOBasic = new O_Basic();
        followingOBasic.setResourceGroup(simpleRes);
        followingOBasic.setDurationOfExecution(Duration.ofHours(4));
        followingOBasic.setOperatingMode(0);
        followingOBasic.setSerialAffiliation(mySeries);

        O_QuantityChain operationChain = new O_QuantityChain(firstOBasic, 5);

        operationChain.addFollowingOperation(followingOBasic);

        Collection<IOperation> operations = new ArrayList<>();
        operations.add(operationChain);
        operations.add(followingOBasic);

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

    @Test
    public void testForMaxFlowSolution() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        CellWithVoltage cellWithVoltage = new CellWithVoltage(10, 3, "14-08-2021 09:00");
        ArrayList<CellWithVoltage> cells = new ArrayList<>();
        cells.add(cellWithVoltage);
        CellElectricFurnace cellElectricFurnace = new CellElectricFurnace(10, cells);

        R_ElectricFurnace firstRecourse = new R_ElectricFurnace("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);
        firstRecourse.addCellElectricFurnace(cellElectricFurnace);

        cellWithVoltage.setCoreResource(firstRecourse);

        Group firstRes = new Group();
        firstRes.addRecourseInTheGroup(firstRecourse);

        Series mySeries = new Series("30-08-2021 00:00", "15-08-2021 10:00");

        O_Basic firstOBasic = new O_TemperatureAndVoltage(10, 10);
        firstOBasic.setResourceGroup(firstRes);
        firstOBasic.setDurationOfExecution(Duration.ofHours(3));
        firstOBasic.setOperatingMode(0);
        firstOBasic.setSerialAffiliation(mySeries);

        O_Basic secondOBasic = new O_TemperatureAndVoltage(10, 10);
        secondOBasic.setResourceGroup(firstRes);
        secondOBasic.setDurationOfExecution(Duration.ofHours(10));
        secondOBasic.setOperatingMode(1);
        secondOBasic.setSerialAffiliation(mySeries);

        O_QuantityChain operationChain = new O_QuantityChain(firstOBasic, 10);
        O_QuantityChain operationChain2 = new O_QuantityChain(secondOBasic, 2);

        IOperation firstOperationForWork = new O_Priority(operationChain);
        IOperation secondOperationForWork = new O_Priority(operationChain2);

        Collection<IOperation> operations = new ArrayList<>();
        operations.add(firstOperationForWork);
        operations.add(secondOperationForWork);

        mySeries.setOperationsToCreate(operations);

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(mySeries);

        firstRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());

        Main.testAlgo(seriesToWork, new ControlParameters(1, 1, 0));

        for(Series currentSeries: seriesToWork) {
            for(IOperation currentOperationFromSeries: currentSeries.getOperationsToCreate()){
                System.out.println(currentOperationFromSeries);
            }
        }
    }

    @Test
    public void firstFullTest() {
        ArrayList<WorkingHours> firstSchedule = new ArrayList<>();
        firstSchedule.add(new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00"));
        firstSchedule.add(new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00"));

        ArrayList<S_CellWithVoltage> settingsForCellsWithVoltage = new ArrayList<>();
        settingsForCellsWithVoltage.add(new S_CellWithVoltage(5, 10));
        settingsForCellsWithVoltage.add(new S_CellWithVoltage(3, 20));

        ArrayList<S_CellElectricFurnace> settingForCellOfElectricFurnace = new ArrayList<>();
        settingForCellOfElectricFurnace.add(new S_CellElectricFurnace(15, settingsForCellsWithVoltage));
        settingForCellOfElectricFurnace.add(new S_CellElectricFurnace(10, settingsForCellsWithVoltage));

        IResource electricFurnace = new R_ElectricFurnace(firstSchedule, settingForCellOfElectricFurnace, "14-08-2021 09:00");

        IResource simpleRecourse = new Recourse(firstSchedule, "15-08-2021 10:00");

        Group groupOfElectricFurnace = new Group();
        groupOfElectricFurnace.addRecourseInTheGroup(electricFurnace);

        Group groupOfSimpleRecourse = new Group();
        groupOfSimpleRecourse.addRecourseInTheGroup(simpleRecourse);

        Series firstSeries = new Series("30-12-2021 00:00", "15-08-2021 10:00");

        O_Basic firstOBasicOfFirstSeries = new O_TemperatureAndVoltage(10, 10);
        firstOBasicOfFirstSeries.setResourceGroup(groupOfElectricFurnace);
        firstOBasicOfFirstSeries.setDurationOfExecution(Duration.ofHours(3));
        firstOBasicOfFirstSeries.setOperatingMode(0);
        firstOBasicOfFirstSeries.setSerialAffiliation(firstSeries);

        O_QuantityChain firstOperationWithCount = new O_QuantityChain(firstOBasicOfFirstSeries, 2);

        O_Basic secondOBasicFirstSeries = new O_TemperatureAndVoltage(10, 20);
        secondOBasicFirstSeries.setResourceGroup(groupOfElectricFurnace);
        secondOBasicFirstSeries.setDurationOfExecution(Duration.ofHours(10));
        secondOBasicFirstSeries.setOperatingMode(1);
        secondOBasicFirstSeries.setSerialAffiliation(firstSeries);

        O_QuantityChain secondOperationWithCount = new O_QuantityChain(secondOBasicFirstSeries, 2);

        IOperation thirdOperation = new O_Priority();
        thirdOperation.setResourceGroup(groupOfSimpleRecourse);
        thirdOperation.setDurationOfExecution(Duration.ofHours(5));
        thirdOperation.setOperatingMode(0);
        thirdOperation.setSerialAffiliation(firstSeries);

        IOperation firstOperation = new O_Priority(firstOperationWithCount);
        IOperation secondOperation = new O_Priority(secondOperationWithCount);

        firstOperation.addFollowingOperation(thirdOperation);
        secondOperation.addFollowingOperation(thirdOperation);

        Collection<IOperation> operationsToCreate = new ArrayList<>();
        operationsToCreate.add(firstOperation);
        operationsToCreate.add(secondOperation);
        operationsToCreate.add(thirdOperation);

        firstSeries.setOperationsToCreate(operationsToCreate);

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);

        simpleRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        electricFurnace.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Main.testAlgo(seriesToWork, new ControlParameters(2, 1, 0));

        for(Series currentSeries: seriesToWork) {
            for(IOperation currentOperationFromSeries: currentSeries.getOperationsToCreate()){
                System.out.println(currentOperationFromSeries);
            }
        }
    }

    @Test
    public void secondFullTestWithDifferentSeries() throws IOException{
        ArrayList<WorkingHours> firstSchedule = new ArrayList<>();
        firstSchedule.add(new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00"));
        firstSchedule.add(new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00"));

        ArrayList<WorkingHours> secondSchedule = new ArrayList<>();
        secondSchedule.add(new WorkingHours("14-08-2021 09:00", "14-08-2021 16:00"));
        secondSchedule.add(new WorkingHours("15-08-2021 09:00", "15-08-2021 16:00"));

        ArrayList<S_CellWithVoltage> settingsForCellsWithVoltageFirstELF = new ArrayList<>();
        settingsForCellsWithVoltageFirstELF.add(new S_CellWithVoltage(5, 10));
        settingsForCellsWithVoltageFirstELF.add(new S_CellWithVoltage(3, 20));
        settingsForCellsWithVoltageFirstELF.add(new S_CellWithVoltage(4, 220));

        ArrayList<S_CellElectricFurnace> settingForCellOfElectricFurnaceFirstELF = new ArrayList<>();
        settingForCellOfElectricFurnaceFirstELF.add(new S_CellElectricFurnace(15, settingsForCellsWithVoltageFirstELF));
        settingForCellOfElectricFurnaceFirstELF.add(new S_CellElectricFurnace(10, settingsForCellsWithVoltageFirstELF));

        IResource firstElectricFurnace = new R_ElectricFurnace(firstSchedule, settingForCellOfElectricFurnaceFirstELF, "14-08-2021 09:00");

        ArrayList<S_CellWithVoltage> settingsForCellsWithVoltageSecondELF = new ArrayList<>();
        settingsForCellsWithVoltageSecondELF.add(new S_CellWithVoltage(10, 10));
        settingsForCellsWithVoltageSecondELF.add(new S_CellWithVoltage(2, 20));
        settingsForCellsWithVoltageSecondELF.add(new S_CellWithVoltage(2, 220));

        ArrayList<S_CellElectricFurnace> settingForCellOfElectricFurnaceSecondELF = new ArrayList<>();
        settingForCellOfElectricFurnaceSecondELF.add(new S_CellElectricFurnace(15, settingsForCellsWithVoltageSecondELF));
        settingForCellOfElectricFurnaceSecondELF.add(new S_CellElectricFurnace(10, settingsForCellsWithVoltageSecondELF));

        IResource secondElectricFurnace = new R_ElectricFurnace(firstSchedule, settingForCellOfElectricFurnaceSecondELF, "18-08-2021 09:00");

        IResource firstSimpleResource = new Recourse(firstSchedule, "15-08-2021 10:00");
        IResource secondSimpleResource = new Recourse(secondSchedule, "18-08-2021 12:00");

        Group groupOfElectricFurnace = new Group();
        groupOfElectricFurnace.addRecourseInTheGroup(firstElectricFurnace);
        groupOfElectricFurnace.addRecourseInTheGroup(secondElectricFurnace);

        Group groupOfSimpleRecourse = new Group();
        groupOfSimpleRecourse.addRecourseInTheGroup(firstSimpleResource);
        groupOfSimpleRecourse.addRecourseInTheGroup(secondSimpleResource);

        Series firstSeries = new Series("30-11-2021 00:00", "15-08-2021 09:00");
        {

            O_QuantityChain firstOperationWithCountOfFirstSeries = new O_QuantityChain
                    (new O_TemperatureAndVoltage(firstSeries, groupOfElectricFurnace, Duration.ofHours(3),
                            0, 10, 10), 5);

            O_QuantityChain secondOperationWithCountOfFirstSeries = new O_QuantityChain
                    (new O_TemperatureAndVoltage(firstSeries, groupOfElectricFurnace, Duration.ofHours(5),
                            0, 10, 20), 6);

            O_QuantityChain thirdOperationWithCountOfFirstSeries = new O_QuantityChain
                    (new O_TemperatureAndVoltage(firstSeries, groupOfElectricFurnace, Duration.ofHours(10),
                            1, 15, 220), 3);

            IOperation firstOperationOfFirstSeries = new O_Priority(firstOperationWithCountOfFirstSeries);
            IOperation secondOperationOfFirstSeries = new O_Priority(secondOperationWithCountOfFirstSeries);
            IOperation thirdOperationOfFirstSeries = new O_Priority(thirdOperationWithCountOfFirstSeries);
            IOperation fourthOperationOfFirstSeries = new O_Priority(firstSeries, groupOfSimpleRecourse, Duration.ofHours(5), 0);
            IOperation fifthOperationOfFirstSeries = new O_Priority(firstSeries, groupOfSimpleRecourse, Duration.ofHours(10), 1);
            IOperation sixthOperationOfFirstSeries = new O_Priority(firstSeries, groupOfSimpleRecourse, Duration.ofHours(17), 1);
            IOperation seventhOperationOfFirstSeries = new O_Priority(firstSeries, groupOfSimpleRecourse, Duration.ofHours(1), 0);

            firstOperationOfFirstSeries.addFollowingOperation(fourthOperationOfFirstSeries);
            secondOperationOfFirstSeries.addFollowingOperation(fifthOperationOfFirstSeries);
            secondOperationOfFirstSeries.addFollowingOperation(sixthOperationOfFirstSeries);
            thirdOperationOfFirstSeries.addFollowingOperation(sixthOperationOfFirstSeries);
            fourthOperationOfFirstSeries.addFollowingOperation(seventhOperationOfFirstSeries);
            fifthOperationOfFirstSeries.addFollowingOperation(seventhOperationOfFirstSeries);
            sixthOperationOfFirstSeries.addFollowingOperation(seventhOperationOfFirstSeries);

            Collection<IOperation> operationsToCreate = new ArrayList<>();
            operationsToCreate.add(firstOperationOfFirstSeries);
            operationsToCreate.add(secondOperationOfFirstSeries);
            operationsToCreate.add(thirdOperationOfFirstSeries);
            operationsToCreate.add(fourthOperationOfFirstSeries);
            operationsToCreate.add(fifthOperationOfFirstSeries);
            operationsToCreate.add(sixthOperationOfFirstSeries);
            operationsToCreate.add(seventhOperationOfFirstSeries);

            firstSeries.setOperationsToCreate(operationsToCreate);
        }

        Series secondSeries = new Series("30-10-2021 00:00", "16-08-2021 09:00");
        {

            O_QuantityChain firstOperationWithCountOfFirstSeries = new O_QuantityChain
                    (new O_TemperatureAndVoltage(secondSeries, groupOfElectricFurnace, Duration.ofHours(3),
                            0, 10, 10), 10);

            O_QuantityChain secondOperationWithCountOfFirstSeries = new O_QuantityChain
                    (new O_TemperatureAndVoltage(secondSeries, groupOfElectricFurnace, Duration.ofHours(4),
                            1, 15, 220), 7);

            IOperation firstOperation = new O_Priority(firstOperationWithCountOfFirstSeries);
            IOperation secondOperation = new O_Priority(secondOperationWithCountOfFirstSeries);
            IOperation thirdOperation = new O_Priority(secondSeries, groupOfSimpleRecourse, Duration.ofHours(10), 1);
            IOperation fourthOperation = new O_Priority(secondSeries, groupOfSimpleRecourse, Duration.ofHours(3), 0);
            IOperation fifthOperation = new O_Priority(secondSeries, groupOfSimpleRecourse, Duration.ofHours(12), 1);

            firstOperation.addFollowingOperation(fourthOperation);
            secondOperation.addFollowingOperation(fourthOperation);
            thirdOperation.addFollowingOperation(fourthOperation);
            fourthOperation.addFollowingOperation(fifthOperation);

            Collection<IOperation> operationsToCreate = new ArrayList<>();
            operationsToCreate.add(firstOperation);
            operationsToCreate.add(secondOperation);
            operationsToCreate.add(thirdOperation);
            operationsToCreate.add(fourthOperation);
            operationsToCreate.add(fifthOperation);

            secondSeries.setOperationsToCreate(operationsToCreate);
        }

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        firstElectricFurnace.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        secondElectricFurnace.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        firstSimpleResource.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        secondSimpleResource.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Algo_WithCPAndFuture algo = new Algo_WithCPAndFuture(
                seriesToWork, new ControlParameters(2, 1, 0), Duration.ofHours(8)
        );

        algo.takeSeriesToWork();

        Visualization visualization = new Visualization("secondFullTestWithDifferentSeries-simple");
        visualization.visualizeSolution(seriesToWork);

        PSA_byEGOServer ego = new PSA_byEGOServer(40, "secondFullTestWithDifferentSeries.txt");
        ego.calculateBestSolution(algo);
        System.out.println(ego.getBestSolution().getSeconds());

        algo = new Algo_WithCPAndFuture(seriesToWork, ego.getBestControlParameters(), ego.getBestDurationParameter());

        algo.takeSeriesToWork();

        visualization = new Visualization("secondFullTestWithDifferentSeries-best");
        visualization.visualizeSolution(seriesToWork);

    }
}