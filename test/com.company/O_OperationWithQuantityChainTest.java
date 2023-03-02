package com.company;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.junit.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

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

        Operation firstOperation = new O_OperationWithTemperatureAndVoltage(10, 10);
        firstOperation.setResourceGroup(firstRes);
        firstOperation.setDurationOfExecution(Duration.ofHours(3));
        firstOperation.setOperatingMode(0);
        firstOperation.setSerialAffiliation(mySeries);

        Operation secondOperation = new O_OperationWithTemperatureAndVoltage(10, 10);
        secondOperation.setResourceGroup(firstRes);
        secondOperation.setDurationOfExecution(Duration.ofHours(10));
        secondOperation.setOperatingMode(1);
        secondOperation.setSerialAffiliation(mySeries);

        O_OperationWithQuantityChain operationChain = new O_OperationWithQuantityChain(firstOperation, 10);
        O_OperationWithQuantityChain operationChain2 = new O_OperationWithQuantityChain(secondOperation, 2);

        IOperation firstOperationForWork = new OperationWithPriorityNew(operationChain);
        IOperation secondOperationForWork = new OperationWithPriorityNew(operationChain2);

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

        Operation firstOperationOfFirstSeries = new O_OperationWithTemperatureAndVoltage(10, 10);
        firstOperationOfFirstSeries.setResourceGroup(groupOfElectricFurnace);
        firstOperationOfFirstSeries.setDurationOfExecution(Duration.ofHours(3));
        firstOperationOfFirstSeries.setOperatingMode(0);
        firstOperationOfFirstSeries.setSerialAffiliation(firstSeries);

        O_OperationWithQuantityChain firstOperationWithCount = new O_OperationWithQuantityChain(firstOperationOfFirstSeries, 2);

        Operation secondOperationFirstSeries = new O_OperationWithTemperatureAndVoltage(10, 20);
        secondOperationFirstSeries.setResourceGroup(groupOfElectricFurnace);
        secondOperationFirstSeries.setDurationOfExecution(Duration.ofHours(10));
        secondOperationFirstSeries.setOperatingMode(1);
        secondOperationFirstSeries.setSerialAffiliation(firstSeries);

        O_OperationWithQuantityChain secondOperationWithCount = new O_OperationWithQuantityChain(secondOperationFirstSeries, 2);

        IOperation thirdOperation = new OperationWithPriorityNew();
        thirdOperation.setResourceGroup(groupOfSimpleRecourse);
        thirdOperation.setDurationOfExecution(Duration.ofHours(5));
        thirdOperation.setOperatingMode(0);
        thirdOperation.setSerialAffiliation(firstSeries);

        IOperation firstOperation = new OperationWithPriorityNew(firstOperationWithCount);
        IOperation secondOperation = new OperationWithPriorityNew(secondOperationWithCount);

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

            O_OperationWithQuantityChain firstOperationWithCountOfFirstSeries = new O_OperationWithQuantityChain
                    (new O_OperationWithTemperatureAndVoltage(firstSeries, groupOfElectricFurnace, Duration.ofHours(3),
                            0, 10, 10), 5);

            O_OperationWithQuantityChain secondOperationWithCountOfFirstSeries = new O_OperationWithQuantityChain
                    (new O_OperationWithTemperatureAndVoltage(firstSeries, groupOfElectricFurnace, Duration.ofHours(5),
                            0, 10, 20), 6);

            O_OperationWithQuantityChain thirdOperationWithCountOfFirstSeries = new O_OperationWithQuantityChain
                    (new O_OperationWithTemperatureAndVoltage(firstSeries, groupOfElectricFurnace, Duration.ofHours(10),
                            1, 15, 220), 3);

            IOperation firstOperationOfFirstSeries = new OperationWithPriorityNew(firstOperationWithCountOfFirstSeries);
            IOperation secondOperationOfFirstSeries = new OperationWithPriorityNew(secondOperationWithCountOfFirstSeries);
            IOperation thirdOperationOfFirstSeries = new OperationWithPriorityNew(thirdOperationWithCountOfFirstSeries);
            IOperation fourthOperationOfFirstSeries = new OperationWithPriorityNew(firstSeries, groupOfSimpleRecourse, Duration.ofHours(5), 0);
            IOperation fifthOperationOfFirstSeries = new OperationWithPriorityNew(firstSeries, groupOfSimpleRecourse, Duration.ofHours(10), 1);
            IOperation sixthOperationOfFirstSeries = new OperationWithPriorityNew(firstSeries, groupOfSimpleRecourse, Duration.ofHours(17), 1);
            IOperation seventhOperationOfFirstSeries = new OperationWithPriorityNew(firstSeries, groupOfSimpleRecourse, Duration.ofHours(1), 0);

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

            O_OperationWithQuantityChain firstOperationWithCountOfFirstSeries = new O_OperationWithQuantityChain
                    (new O_OperationWithTemperatureAndVoltage(secondSeries, groupOfElectricFurnace, Duration.ofHours(3),
                            0, 10, 10), 10);

            O_OperationWithQuantityChain secondOperationWithCountOfFirstSeries = new O_OperationWithQuantityChain
                    (new O_OperationWithTemperatureAndVoltage(secondSeries, groupOfElectricFurnace, Duration.ofHours(4),
                            1, 15, 220), 7);

            IOperation firstOperation = new OperationWithPriorityNew(firstOperationWithCountOfFirstSeries);
            IOperation secondOperation = new OperationWithPriorityNew(secondOperationWithCountOfFirstSeries);
            IOperation thirdOperation = new OperationWithPriorityNew(secondSeries, groupOfSimpleRecourse, Duration.ofHours(10), 1);
            IOperation fourthOperation = new OperationWithPriorityNew(secondSeries, groupOfSimpleRecourse, Duration.ofHours(3), 0);
            IOperation fifthOperation = new OperationWithPriorityNew(secondSeries, groupOfSimpleRecourse, Duration.ofHours(12), 1);

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

        OperationsArrangementAlgorithmWithCPAndFutureFrontNew algo = new OperationsArrangementAlgorithmWithCPAndFutureFrontNew(
                seriesToWork, new ControlParameters(2, 1, 0), Duration.ofHours(8)
        );

        OA_byEGOServer ego = new OA_byEGOServer("D:\\MY_EGO_SERVER\\main.py", 40,
                "D:\\MY_EGO_SERVER\\secondFullTestWithDifferentSeries.txt");
        ego.calculateBestSolution(algo);
        System.out.println(ego.getBestSolution().getSeconds());
        /*OA_byEGO ego = new OA_byEGO("D:\\My_EGO_realization\\hello.py",
                40, "D:\\My_EGO_realization\\secondFullTestWithDifferentSeries.txt");

        ego.calculateBestSolution(algo);
        ego.resetResult();
        Duration resultDuration = ego.getBestSolution();

        System.out.println(resultDuration.getSeconds());*/
    }
}