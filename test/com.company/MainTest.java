package com.company;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import jdk.nashorn.internal.runtime.OptimisticReturnFilters;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MainTest {


    @Test
    public void easyParallelTest() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        Recourse firstRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        WorkingHours workingHoursForSecond0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 16:00");
        WorkingHours workingHoursForSecond1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 16:00");

        Recourse secondRecourse = new Recourse("16-08-2021 10:00");
        secondRecourse.addSchedule(workingHoursForSecond0);
        secondRecourse.addSchedule(workingHoursForSecond1);

        WorkingHours workingHoursForThird0 = new WorkingHours("15-08-2021 09:00", "15-08-2021 19:00");
        WorkingHours workingHoursForThird1 = new WorkingHours("18-08-2021 09:00", "18-08-2021 19:00");

        Recourse thirdRecourse = new Recourse("14-08-2021 10:00");
        thirdRecourse.addSchedule(workingHoursForThird0);
        thirdRecourse.addSchedule(workingHoursForThird1);



        Group firstAndSecond = new Group();
        firstAndSecond.addRecourseInTheGroup(firstRecourse);
        firstAndSecond.addRecourseInTheGroup(secondRecourse);

        Group firstAndThird = new Group();
        firstAndThird.addRecourseInTheGroup(firstRecourse);
        firstAndThird.addRecourseInTheGroup(thirdRecourse);

        Group allRecourses = new Group();
        allRecourses.addRecourseInTheGroup(firstRecourse);
        allRecourses.addRecourseInTheGroup(secondRecourse);
        allRecourses.addRecourseInTheGroup(thirdRecourse);



        Operation firstOperation = new Operation();
        firstOperation.setResourceGroup(firstAndSecond);
        firstOperation.setDurationOfExecution(Duration.ofHours(3));
        firstOperation.setOperatingMode(0);

        Operation secondOperation = new Operation();
        secondOperation.setResourceGroup(firstAndThird);
        secondOperation.setDurationOfExecution(Duration.ofHours(9));
        secondOperation.setOperatingMode(0);

        Operation thirdOperation = new Operation();
        thirdOperation.setResourceGroup(allRecourses);
        thirdOperation.setDurationOfExecution(Duration.ofHours(4));
        thirdOperation.setOperatingMode(1);

        Operation fourthOperation = new Operation();
        fourthOperation.setResourceGroup(allRecourses);
        fourthOperation.setDurationOfExecution(Duration.ofHours(2));
        fourthOperation.setOperatingMode(0);

        firstOperation.addFollowingOperation(fourthOperation);
        secondOperation.addFollowingOperation(fourthOperation);
        thirdOperation.addFollowingOperation(fourthOperation);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(firstOperation);
        operations.add(secondOperation);
        operations.add(thirdOperation);
        operations.add(fourthOperation);

        Series mySeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 10:00");

        firstOperation.setSerialAffiliation(mySeries);
        secondOperation.setSerialAffiliation(mySeries);
        thirdOperation.setSerialAffiliation(mySeries);
        fourthOperation.setSerialAffiliation(mySeries);

        firstRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        thirdRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());

        Main.installOperationsUntilDeadline(mySeries);

        WorkingHours expectation = new WorkingHours("15-08-2021 10:00", "15-08-2021 13:00");
        assertEquals(expectation.toString(),firstOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("15-08-2021 10:00", "15-08-2021 19:00");
        assertEquals(expectation.toString(),secondOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("15-08-2021 13:00", "16-08-2021 12:00");
        assertEquals(expectation.toString(),thirdOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("16-08-2021 12:00", "16-08-2021 14:00");
        assertEquals(expectation.toString(),fourthOperation.getCWorkingInterval().toString());
    }


    @Test
    public void easySequentialTest() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        Recourse firstRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        WorkingHours workingHoursForSecond0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 16:00");
        WorkingHours workingHoursForSecond1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 16:00");

        Recourse secondRecourse = new Recourse("16-08-2021 10:00");
        secondRecourse.addSchedule(workingHoursForSecond0);
        secondRecourse.addSchedule(workingHoursForSecond1);

        WorkingHours workingHoursForThird0 = new WorkingHours("15-08-2021 09:00", "15-08-2021 19:00");
        WorkingHours workingHoursForThird1 = new WorkingHours("18-08-2021 09:00", "18-08-2021 19:00");

        Recourse thirdRecourse = new Recourse("14-08-2021 10:00");
        thirdRecourse.addSchedule(workingHoursForThird0);
        thirdRecourse.addSchedule(workingHoursForThird1);


        Group firstAndSecond = new Group();
        firstAndSecond.addRecourseInTheGroup(firstRecourse);
        firstAndSecond.addRecourseInTheGroup(secondRecourse);

        Group firstAndThird = new Group();
        firstAndThird.addRecourseInTheGroup(firstRecourse);
        firstAndThird.addRecourseInTheGroup(thirdRecourse);

        Group allRecourses = new Group();
        allRecourses.addRecourseInTheGroup(firstRecourse);
        allRecourses.addRecourseInTheGroup(secondRecourse);
        allRecourses.addRecourseInTheGroup(thirdRecourse);


        Operation firstOperation = new Operation();
        firstOperation.setResourceGroup(firstAndSecond);
        firstOperation.setDurationOfExecution(Duration.ofHours(10));
        firstOperation.setOperatingMode(1);

        Operation secondOperation = new Operation();
        secondOperation.setResourceGroup(firstAndThird);
        secondOperation.setDurationOfExecution(Duration.ofHours(4));
        secondOperation.setOperatingMode(0);

        Operation thirdOperation = new Operation();
        thirdOperation.setResourceGroup(allRecourses);
        thirdOperation.setDurationOfExecution(Duration.ofHours(8));
        thirdOperation.setOperatingMode(1);

        Operation fourthOperation = new Operation();
        fourthOperation.setResourceGroup(allRecourses);
        fourthOperation.setDurationOfExecution(Duration.ofHours(3));
        fourthOperation.setOperatingMode(0);

        firstOperation.addFollowingOperation(secondOperation);
        secondOperation.addFollowingOperation(thirdOperation);
        secondOperation.addFollowingOperation(fourthOperation);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(firstOperation);
        operations.add(secondOperation);
        operations.add(thirdOperation);
        operations.add(fourthOperation);

        Series mySeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 10:00");

        firstOperation.setSerialAffiliation(mySeries);
        secondOperation.setSerialAffiliation(mySeries);
        thirdOperation.setSerialAffiliation(mySeries);
        fourthOperation.setSerialAffiliation(mySeries);

        firstRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        thirdRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());

        Main.installOperationsUntilDeadline(mySeries);

        WorkingHours expectation = new WorkingHours("15-08-2021 10:00", "17-08-2021 10:00");
        assertEquals(expectation.toString(), firstOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("17-08-2021 10:00", "17-08-2021 14:00");
        assertEquals(expectation.toString(), secondOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("17-08-2021 14:00", "18-08-2021 15:00");
        assertEquals(expectation.toString(), thirdOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("18-08-2021 09:00", "18-08-2021 12:00");
        assertEquals(expectation.toString(), fourthOperation.getCWorkingInterval().toString());
    }


    @Test
    public void reverseTest() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 10:00", "14-08-2021 19:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 10:00", "15-08-2021 19:00");

        Recourse firstRecourse = new Recourse("14-08-2021 10:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        WorkingHours workingHoursForSecond0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForSecond1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        Recourse secondRecourse = new Recourse("14-08-2021 09:00");
        secondRecourse.addSchedule(workingHoursForSecond0);
        secondRecourse.addSchedule(workingHoursForSecond1);

        WorkingHours workingHoursForThird0 = new WorkingHours("14-08-2021 10:00", "14-08-2021 13:00");
        WorkingHours workingHoursForThird1 = new WorkingHours("15-08-2021 10:00", "15-08-2021 13:00");

        Recourse thirdRecourse = new Recourse("14-08-2021 10:00");
        thirdRecourse.addSchedule(workingHoursForThird0);
        thirdRecourse.addSchedule(workingHoursForThird1);


        Group firstAndSecond = new Group();
        firstAndSecond.addRecourseInTheGroup(firstRecourse);
        firstAndSecond.addRecourseInTheGroup(secondRecourse);

        Group firstAndThird = new Group();
        firstAndThird.addRecourseInTheGroup(firstRecourse);
        firstAndThird.addRecourseInTheGroup(thirdRecourse);

        Group third = new Group();
        third.addRecourseInTheGroup(thirdRecourse);

        Group second = new Group();
        second.addRecourseInTheGroup(secondRecourse);


        Operation firstOperation = new Operation();
        firstOperation.setResourceGroup(firstAndSecond);
        firstOperation.setDurationOfExecution(Duration.ofHours(6));
        firstOperation.setOperatingMode(0);

        Operation secondOperation = new Operation();
        secondOperation.setResourceGroup(firstAndThird);
        secondOperation.setDurationOfExecution(Duration.ofHours(2));
        secondOperation.setOperatingMode(0);

        Operation thirdOperation = new Operation();
        thirdOperation.setResourceGroup(third);
        thirdOperation.setDurationOfExecution(Duration.ofHours(3));
        thirdOperation.setOperatingMode(0);

        Operation fourthOperation = new Operation();
        fourthOperation.setResourceGroup(second);
        fourthOperation.setDurationOfExecution(Duration.ofHours(5));
        fourthOperation.setOperatingMode(0);

        firstOperation.addFollowingOperation(secondOperation);
        secondOperation.addFollowingOperation(thirdOperation);
        secondOperation.addFollowingOperation(fourthOperation);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(firstOperation);
        operations.add(secondOperation);
        operations.add(thirdOperation);
        operations.add(fourthOperation);

        Series mySeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 00:00");
        firstOperation.setSerialAffiliation(mySeries);
        secondOperation.setSerialAffiliation(mySeries);
        thirdOperation.setSerialAffiliation(mySeries);
        fourthOperation.setSerialAffiliation(mySeries);

        firstRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        thirdRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());

        Main.installOperationsUntilDeadline(mySeries);

        mySeries.clean();

        Main.installReverseOperationsUntilDeadline(mySeries);

        LocalDateTime expectation = LocalDateTime.of(2021,8,28,11,00);
        assertEquals(expectation.toString(), firstOperation.getCLateStartTime().toString());

        expectation = LocalDateTime.of(2021, 8,28,17,00);
        assertEquals(expectation.toString(), secondOperation.getCLateStartTime().toString());

        expectation = LocalDateTime.of(2021, 8,29,10,00);
        assertEquals(expectation.toString(), thirdOperation.getCLateStartTime().toString());

        expectation = LocalDateTime.of(2021, 8,29,9,00);
        assertEquals(expectation.toString(), fourthOperation.getCLateStartTime().toString());
    }

    @Test
    public void reverseSecondTest() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 10:00", "14-08-2021 18:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 10:00", "15-08-2021 18:00");

        Recourse firstRecourse = new Recourse("15-08-2021 10:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        WorkingHours workingHoursForSecond0 = new WorkingHours("14-08-2021 10:00", "14-08-2021 16:00");
        WorkingHours workingHoursForSecond1 = new WorkingHours("15-08-2021 10:00", "15-08-2021 16:00");
        WorkingHours workingHoursForSecond2 = new WorkingHours("18-08-2021 10:00", "18-08-2021 16:00");
        WorkingHours workingHoursForSecond3 = new WorkingHours("19-08-2021 10:00", "19-08-2021 16:00");

        Recourse secondRecourse = new Recourse("16-08-2021 14:00");
        secondRecourse.addSchedule(workingHoursForSecond0);
        secondRecourse.addSchedule(workingHoursForSecond1);
        secondRecourse.addSchedule(workingHoursForSecond2);
        secondRecourse.addSchedule(workingHoursForSecond3);

        WorkingHours workingHoursForThird0 = new WorkingHours("14-08-2021 12:00", "14-08-2021 20:00");
        WorkingHours workingHoursForThird1 = new WorkingHours("15-08-2021 12:00", "15-08-2021 20:00");

        Recourse thirdRecourse = new Recourse("14-08-2021 10:00");
        thirdRecourse.addSchedule(workingHoursForThird0);
        thirdRecourse.addSchedule(workingHoursForThird1);


        Group firstAndSecond = new Group();
        firstAndSecond.addRecourseInTheGroup(firstRecourse);
        firstAndSecond.addRecourseInTheGroup(secondRecourse);

        Group firstAndThird = new Group();
        firstAndThird.addRecourseInTheGroup(firstRecourse);
        firstAndThird.addRecourseInTheGroup(thirdRecourse);

        Group second = new Group();
        second.addRecourseInTheGroup(secondRecourse);

        Group allRecourses = new Group();
        allRecourses.addRecourseInTheGroup(firstRecourse);
        allRecourses.addRecourseInTheGroup(secondRecourse);
        allRecourses.addRecourseInTheGroup(thirdRecourse);


        Operation firstOperation = new Operation();
        firstOperation.setResourceGroup(firstAndThird);
        firstOperation.setDurationOfExecution(Duration.ofHours(5));
        firstOperation.setOperatingMode(0);

        Operation secondOperation = new Operation();
        secondOperation.setResourceGroup(firstAndSecond);
        secondOperation.setDurationOfExecution(Duration.ofHours(10));
        secondOperation.setOperatingMode(1);

        Operation thirdOperation = new Operation();
        thirdOperation.setResourceGroup(firstAndThird);
        thirdOperation.setDurationOfExecution(Duration.ofHours(7));
        thirdOperation.setOperatingMode(0);

        Operation fourthOperation = new Operation();
        fourthOperation.setResourceGroup(second);
        fourthOperation.setDurationOfExecution(Duration.ofHours(12));
        fourthOperation.setOperatingMode(1);

        Operation fifthOperation = new Operation();
        fifthOperation.setResourceGroup(allRecourses);
        fifthOperation.setDurationOfExecution(Duration.ofHours(6));
        fifthOperation.setOperatingMode(0);

        firstOperation.addFollowingOperation(secondOperation);
        thirdOperation.addFollowingOperation(fourthOperation);
        secondOperation.addFollowingOperation(fifthOperation);
        fourthOperation.addFollowingOperation(fifthOperation);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(firstOperation);
        operations.add(secondOperation);
        operations.add(thirdOperation);
        operations.add(fourthOperation);
        operations.add(fifthOperation);

        Series mySeries = new Series(operations, "30-08-2021 00:00", "14-08-2021 12:00");
        firstOperation.setSerialAffiliation(mySeries);
        secondOperation.setSerialAffiliation(mySeries);
        thirdOperation.setSerialAffiliation(mySeries);
        fourthOperation.setSerialAffiliation(mySeries);
        fifthOperation.setSerialAffiliation(mySeries);

        firstRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        thirdRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());


        Main.installOperationsUntilDeadline(mySeries);

        mySeries.clean();

        Main.installReverseOperationsUntilDeadline(mySeries);

        LocalDateTime expectation = LocalDateTime.of(2021,8,27,15,00);
        assertEquals(expectation.toString(), firstOperation.getCLateStartTime().toString());

        expectation = LocalDateTime.of(2021, 8,28,12,00);
        assertEquals(expectation.toString(), secondOperation.getCLateStartTime().toString());

        expectation = LocalDateTime.of(2021, 8,25,13,00);
        assertEquals(expectation.toString(), thirdOperation.getCLateStartTime().toString());

        expectation = LocalDateTime.of(2021, 8,26,10,00);
        assertEquals(expectation.toString(), fourthOperation.getCLateStartTime().toString());

        expectation = LocalDateTime.of(2021, 8,29,14,00);
        assertEquals(expectation.toString(), fifthOperation.getCLateStartTime().toString());
    }

    @Test
    public void fullFirstTest() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 10:00", "14-08-2021 18:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 10:00", "15-08-2021 18:00");

        Recourse firstRecourse = new Recourse("15-08-2021 12:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        WorkingHours workingHoursForSecond0 = new WorkingHours("14-08-2021 10:00", "14-08-2021 16:00");
        WorkingHours workingHoursForSecond1 = new WorkingHours("15-08-2021 10:00", "15-08-2021 16:00");
        WorkingHours workingHoursForSecond2 = new WorkingHours("18-08-2021 10:00", "18-08-2021 16:00");
        WorkingHours workingHoursForSecond3 = new WorkingHours("19-08-2021 10:00", "19-08-2021 16:00");

        Recourse secondRecourse = new Recourse("16-08-2021 14:00");
        secondRecourse.addSchedule(workingHoursForSecond0);
        secondRecourse.addSchedule(workingHoursForSecond1);
        secondRecourse.addSchedule(workingHoursForSecond2);
        secondRecourse.addSchedule(workingHoursForSecond3);

        WorkingHours workingHoursForThird0 = new WorkingHours("14-08-2021 12:00", "14-08-2021 20:00");
        WorkingHours workingHoursForThird1 = new WorkingHours("15-08-2021 12:00", "15-08-2021 20:00");

        Recourse thirdRecourse = new Recourse("14-08-2021 10:00");
        thirdRecourse.addSchedule(workingHoursForThird0);
        thirdRecourse.addSchedule(workingHoursForThird1);


        Group firstAndSecond = new Group();
        firstAndSecond.addRecourseInTheGroup(firstRecourse);
        firstAndSecond.addRecourseInTheGroup(secondRecourse);

        Group firstAndThird = new Group();
        firstAndThird.addRecourseInTheGroup(firstRecourse);
        firstAndThird.addRecourseInTheGroup(thirdRecourse);

        Group second = new Group();
        second.addRecourseInTheGroup(secondRecourse);

        Group allRecourses = new Group();
        allRecourses.addRecourseInTheGroup(firstRecourse);
        allRecourses.addRecourseInTheGroup(secondRecourse);
        allRecourses.addRecourseInTheGroup(thirdRecourse);


        Operation firstOperation = new Operation();
        firstOperation.setResourceGroup(firstAndThird);
        firstOperation.setDurationOfExecution(Duration.ofHours(5));
        firstOperation.setOperatingMode(0);

        Operation secondOperation = new Operation();
        secondOperation.setResourceGroup(firstAndSecond);
        secondOperation.setDurationOfExecution(Duration.ofHours(10));
        secondOperation.setOperatingMode(1);

        Operation thirdOperation = new Operation();
        thirdOperation.setResourceGroup(firstAndThird);
        thirdOperation.setDurationOfExecution(Duration.ofHours(7));
        thirdOperation.setOperatingMode(0);

        Operation fourthOperation = new Operation();
        fourthOperation.setResourceGroup(second);
        fourthOperation.setDurationOfExecution(Duration.ofHours(12));
        fourthOperation.setOperatingMode(1);

        Operation fifthOperation = new Operation();
        fifthOperation.setResourceGroup(allRecourses);
        fifthOperation.setDurationOfExecution(Duration.ofHours(6));
        fifthOperation.setOperatingMode(0);

        //Специальная операция
        Operation specialOperation = new Operation();
        specialOperation.setResourceGroup(firstAndThird);
        specialOperation.setDurationOfExecution(Duration.ofHours(4));
        specialOperation.setOperatingMode(0);

        ArrayList<Operation> operationsForFirstSeries = new ArrayList<>();
        operationsForFirstSeries.add(specialOperation);
        Series firstSeries = new Series(operationsForFirstSeries, "16-08-2021 00:00", "14-08-2021 12:00");
        specialOperation.setSerialAffiliation(firstSeries);

        firstOperation.addFollowingOperation(secondOperation);
        thirdOperation.addFollowingOperation(fourthOperation);
        secondOperation.addFollowingOperation(fifthOperation);
        fourthOperation.addFollowingOperation(fifthOperation);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(firstOperation);
        operations.add(secondOperation);
        operations.add(thirdOperation);
        operations.add(fourthOperation);
        operations.add(fifthOperation);

        Series mySeries = new Series(operations, "30-08-2021 00:00", "14-08-2021 12:00");
        firstOperation.setSerialAffiliation(mySeries);
        secondOperation.setSerialAffiliation(mySeries);
        thirdOperation.setSerialAffiliation(mySeries);
        fourthOperation.setSerialAffiliation(mySeries);
        fifthOperation.setSerialAffiliation(mySeries);

        firstRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
        thirdRecourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());

        ArrayList<Series> seriesForWork = new ArrayList<>();
        seriesForWork.add(firstSeries);
        seriesForWork.add(mySeries);

        Main.takeSeriesToWork(seriesForWork);

        WorkingHours expectation = new WorkingHours("14-08-2021 12:00", "14-08-2021 16:00");
        assertEquals(expectation.toString(), specialOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("15-08-2021 12:00", "15-08-2021 17:00");
        assertEquals(expectation.toString(), firstOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("15-08-2021 17:00", "17-08-2021 11:00");
        assertEquals(expectation.toString(), secondOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("15-08-2021 12:00", "15-08-2021 19:00");
        assertEquals(expectation.toString(), thirdOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("18-08-2021 10:00", "19-08-2021 16:00");
        assertEquals(expectation.toString(), fourthOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("20-08-2021 10:00", "20-08-2021 16:00");
        assertEquals(expectation.toString(), fifthOperation.getCWorkingInterval().toString());
    }

    @Test
    public void choiceFrontOfWork() {
        Operation firstOperation = new Operation();
        Operation secondOperation = new Operation();
        Operation thirdOperation = new Operation();
        Operation fourthOperation = new Operation();

        ArrayList operationsToCreate = new ArrayList();
        operationsToCreate.add(firstOperation);
        operationsToCreate.add(secondOperation);
        operationsToCreate.add(thirdOperation);
        operationsToCreate.add(fourthOperation);

        ArrayList correctFrontOfWork = new ArrayList();
        correctFrontOfWork.add(firstOperation);
        correctFrontOfWork.add(secondOperation);
        correctFrontOfWork.add(thirdOperation);
        correctFrontOfWork.add(fourthOperation);

        assertEquals(Main.choiceFrontOfWork(operationsToCreate), correctFrontOfWork);


        firstOperation.addFollowingOperation(fourthOperation);
        secondOperation.addFollowingOperation(fourthOperation);
        thirdOperation.addFollowingOperation(fourthOperation);

        correctFrontOfWork = new ArrayList();
        correctFrontOfWork.add(firstOperation);
        correctFrontOfWork.add(secondOperation);
        correctFrontOfWork.add(thirdOperation);
        assertEquals(Main.choiceFrontOfWork(operationsToCreate),correctFrontOfWork);


        firstOperation.addFollowingOperation(thirdOperation);
        secondOperation.addFollowingOperation(thirdOperation);

        correctFrontOfWork = new ArrayList();
        correctFrontOfWork.add(firstOperation);
        correctFrontOfWork.add(secondOperation);
        assertEquals(Main.choiceFrontOfWork(operationsToCreate),correctFrontOfWork);
    }

    @Test
    public void sortByLateStartTimeTest() {
        WorkingHours workingHoursForRecurse = new WorkingHours("14-08-2021 10:00", "14-08-2021 18:00");
        Recourse recourse = new Recourse("14-08-2021 10:00");
        recourse.addSchedule(workingHoursForRecurse);

        Group recourseGroup = new Group();
        recourseGroup.addRecourseInTheGroup(recourse);

        Operation firstOperation = new Operation();
        firstOperation.setResourceGroup(recourseGroup);
        firstOperation.setDurationOfExecution(Duration.ofHours(1));
        firstOperation.setOperatingMode(0);

        Operation secondOperation = new Operation();
        secondOperation.setResourceGroup(recourseGroup);
        secondOperation.setDurationOfExecution(Duration.ofHours(1));
        secondOperation.setOperatingMode(0);

        Operation thirdOperation = new Operation();
        thirdOperation.setResourceGroup(recourseGroup);
        thirdOperation.setDurationOfExecution(Duration.ofHours(1));
        thirdOperation.setOperatingMode(0);

        Operation fourthOperation = new Operation();
        fourthOperation.setResourceGroup(recourseGroup);
        fourthOperation.setDurationOfExecution(Duration.ofHours(1));
        fourthOperation.setOperatingMode(0);

        firstOperation.addFollowingOperation(secondOperation);
        secondOperation.addFollowingOperation(thirdOperation);
        thirdOperation.addFollowingOperation(fourthOperation);

        ArrayList<Operation> operationsToCreate = new ArrayList();
        operationsToCreate.add(firstOperation);
        operationsToCreate.add(secondOperation);
        operationsToCreate.add(thirdOperation);
        operationsToCreate.add(fourthOperation);

        Series firstSeries = new Series(operationsToCreate, "14-08-2021 14:00", "14-08-2021 10:00");
        firstOperation.setSerialAffiliation(firstSeries);
        secondOperation.setSerialAffiliation(firstSeries);
        thirdOperation.setSerialAffiliation(firstSeries);
        fourthOperation.setSerialAffiliation(firstSeries);

        Operation otherFirstOperation = new Operation();
        otherFirstOperation.setResourceGroup(recourseGroup);
        otherFirstOperation.setDurationOfExecution(Duration.ofHours(1));
        otherFirstOperation.setOperatingMode(0);

        Operation otherSecondOperation = new Operation();
        otherSecondOperation.setResourceGroup(recourseGroup);
        otherSecondOperation.setDurationOfExecution(Duration.ofHours(1));
        otherSecondOperation.setOperatingMode(0);

        Operation otherThirdOperation = new Operation();
        otherThirdOperation.setResourceGroup(recourseGroup);
        otherThirdOperation.setDurationOfExecution(Duration.ofHours(1));
        otherThirdOperation.setOperatingMode(0);

        Operation otherFourthOperation = new Operation();
        otherFourthOperation.setResourceGroup(recourseGroup);
        otherFourthOperation.setDurationOfExecution(Duration.ofHours(1));
        otherFourthOperation.setOperatingMode(0);

        otherFirstOperation.addFollowingOperation(otherSecondOperation);
        otherSecondOperation.addFollowingOperation(otherThirdOperation);
        otherThirdOperation.addFollowingOperation(otherFourthOperation);

        ArrayList<Operation> otherOperationsToCreate = new ArrayList();
        otherOperationsToCreate.add(otherFirstOperation);
        otherOperationsToCreate.add(otherSecondOperation);
        otherOperationsToCreate.add(otherThirdOperation);
        otherOperationsToCreate.add(otherFourthOperation);

        Series otherSeries = new Series(otherOperationsToCreate, "30-08-2021 00:00", "14-08-2021 10:00");
        otherFirstOperation.setSerialAffiliation(otherSeries);
        otherSecondOperation.setSerialAffiliation(otherSeries);
        otherThirdOperation.setSerialAffiliation(otherSeries);
        otherFourthOperation.setSerialAffiliation(otherSeries);

        recourse.fillScheduleUsingPreviousData(otherSeries.getDeadlineForCompletion());

        ArrayList<Series> seriesForWork = new ArrayList<>();
        seriesForWork.add(firstSeries);
        seriesForWork.add(otherSeries);

        Main.takeSeriesToWork(seriesForWork);

        WorkingHours expectation = new WorkingHours("14-08-2021 10:00","14-08-2021 11:00" );
        assertEquals(expectation.toString(), firstOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("14-08-2021 11:00","14-08-2021 12:00" );
        assertEquals(expectation.toString(), secondOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("14-08-2021 12:00","14-08-2021 13:00" );
        assertEquals(expectation.toString(), thirdOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("14-08-2021 13:00","14-08-2021 14:00" );
        assertEquals(expectation.toString(), fourthOperation.getCWorkingInterval().toString());



        expectation = new WorkingHours("14-08-2021 14:00","14-08-2021 15:00" );
        assertEquals(expectation.toString(), otherFirstOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("14-08-2021 15:00","14-08-2021 16:00" );
        assertEquals(expectation.toString(), otherSecondOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("14-08-2021 16:00","14-08-2021 17:00" );
        assertEquals(expectation.toString(), otherThirdOperation.getCWorkingInterval().toString());

        expectation = new WorkingHours("14-08-2021 17:00","14-08-2021 18:00" );
        assertEquals(expectation.toString(), otherFourthOperation.getCWorkingInterval().toString());

    }

    @Test
    public void choiceReverseFrontOfWork() {
        Operation firstOperation = new Operation();
        Operation secondOperation = new Operation();
        Operation thirdOperation = new Operation();
        Operation fourthOperation = new Operation();

        ArrayList operationsToCreate = new ArrayList();
        operationsToCreate.add(firstOperation);
        operationsToCreate.add(secondOperation);
        operationsToCreate.add(thirdOperation);
        operationsToCreate.add(fourthOperation);

        ArrayList correctFrontOfWork = new ArrayList();
        correctFrontOfWork.add(firstOperation);
        correctFrontOfWork.add(secondOperation);
        correctFrontOfWork.add(thirdOperation);
        correctFrontOfWork.add(fourthOperation);

        assertEquals(Main.choiceReverseFrontOfWork(operationsToCreate), correctFrontOfWork);

        firstOperation.addFollowingOperation(secondOperation);
        secondOperation.addFollowingOperation(fourthOperation);
        secondOperation.addFollowingOperation(thirdOperation);

        correctFrontOfWork = new ArrayList();
        correctFrontOfWork.add(thirdOperation);
        correctFrontOfWork.add(fourthOperation);

        assertEquals(Main.choiceReverseFrontOfWork(operationsToCreate),correctFrontOfWork);
    }

    //Тестирование работы фронта, который смотрит в будущее
    @Test
    public void minCEarlierStartTimeTest() {
        Operation first = new Operation();
        first.setCEarlierStartTime("10-08-2021 10:00");
        Operation second = new Operation();
        second.setCEarlierStartTime("10-08-2021 12:00");
        Operation third = new Operation();
        third.setCEarlierStartTime("12-08-2021 15:00");

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first);
        operations.add(second);
        operations.add(third);

        LocalDateTime minTime = Main.minCEarlierStartTime(operations);

        assertEquals("10-08-2021 10:00",minTime.format(WorkingHours.formatter));
    }

    @Test
    public void choiceFutureFrontOfWorkTest() {
        Operation first = new Operation();
        first.setCEarlierStartTime("10-08-2021 10:00");
        Operation second = new Operation();
        second.setCEarlierStartTime("10-08-2021 12:00");
        Operation third = new Operation();
        third.setCEarlierStartTime("12-08-2021 15:00");

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first);
        operations.add(second);
        operations.add(third);

        ArrayList<Operation> resultOfWork = Main.choiceFutureFrontOfWork(operations, Duration.ofHours(10));

        ArrayList<Operation> expectation = new ArrayList<>();
        expectation.add(first);
        expectation.add(second);
        assertEquals(expectation, resultOfWork);

        third.setCEarlierStartTime("10-08-2021 15:00");
        expectation.add(third);
        assertEquals(expectation, Main.choiceFutureFrontOfWork(operations, Duration.ofHours(10)));

        expectation = new ArrayList<>();
        expectation.add(first);
        assertEquals(1, Main.choiceFutureFrontOfWork(operations, Duration.ofHours(1)).size());
    }

    @Test
    public void findOperationWithMinTimeAndMinWindowSizeTest() {
        Operation first = new Operation();
        first.setCEarlierStartTime("10-08-2021 10:00");
        first.setDurationOfExecution(Duration.ofHours(5));
        Operation second = new Operation();
        second.setCEarlierStartTime("10-08-2021 10:00");
        second.setDurationOfExecution(Duration.ofHours(3));
        Operation third = new Operation();
        third.setCEarlierStartTime("10-08-2021 15:00");
        third.setDurationOfExecution(Duration.ofHours(5));

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first);
        operations.add(second);
        operations.add(third);

        ArrayList<Operation> futureFrontOfWork = Main.choiceFutureFrontOfWork(operations, Duration.ofHours(10));

        LocalDateTime minTime = Main.minCEarlierStartTime(futureFrontOfWork);

        Operation resultOfWork = Main.findOperationWithMinTimeAndMinWindowSize(futureFrontOfWork, minTime);

        assertEquals(resultOfWork, second);


        second.setCEarlierStartTime("10-08-2021 12:00");

        ArrayList<Operation> futureFrontOfWork2 = Main.choiceFutureFrontOfWork(operations, Duration.ofHours(10));

        assertEquals(first, Main.findOperationWithMinTimeAndMinWindowSize(futureFrontOfWork,
                Main.minCEarlierStartTime(futureFrontOfWork2)));
    }

    @Test
    public void findMinTactTimeTest() {
        Operation first = new Operation();
        first.setTactTime("10-08-2021 10:00");
        Operation second = new Operation();
        second.setTactTime("10-08-2021 12:00");
        Operation third = new Operation();
        third.setTactTime("10-08-2021 15:00");

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first);
        operations.add(second);
        operations.add(third);

        LocalDateTime minTime = Main.findMinTactTime(operations);

        assertEquals("10-08-2021 10:00", minTime.format(WorkingHours.formatter));
    }

    @Test
    public void choiceFutureFrontOfWork2Test(){
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        Recourse firstRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        WorkingHours workingHoursForSecond0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForSecond1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        Recourse secondRecourse = new Recourse("15-08-2021 09:00");
        secondRecourse.addSchedule(workingHoursForSecond0);
        secondRecourse.addSchedule(workingHoursForSecond1);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        Group justSecondRecourse = new Group();
        justSecondRecourse.addRecourseInTheGroup(secondRecourse);

        Operation first0 = new Operation();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        //Operation second0 = new OperationWithPrioritiesByHeirs();
        Operation second0 = new Operation();
        second0.setResourceGroup(justSecondRecourse);
        second0.setDurationOfExecution(Duration.ofHours(3));
        second0.setOperatingMode(0);


        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first0);
        operations.add(second0);

        Series firstSeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 10:00");
        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);

        Operation first1 = new Operation();
        first1.setResourceGroup(justSecondRecourse);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        ArrayList<Operation> operationsForSecondSeries = new ArrayList<>();
        operations.add(first1);

        Series secondSeries = new Series(operationsForSecondSeries, "17-08-2021 00:00", "18-08-2021 10:00");
        first1.setSerialAffiliation(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        ArrayList<Operation> operationsToInstall = new ArrayList<>();
        operationsToInstall.add(first0);
        operationsToInstall.add(second0);
        operationsToInstall.add(first1);

        ArrayList<Operation> result = Main.choiceFutureFrontOfWork2(operationsToInstall, Duration.ofDays(2));

        assertEquals(2, result.size());
    }

    @Test
    public void installOperationsWithFutureFrontOfWork2Test() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 18:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 18:00");

        Recourse firstRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        WorkingHours workingHoursForSecond0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 18:00");
        WorkingHours workingHoursForSecond1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 18:00");

        Recourse secondRecourse = new Recourse("15-08-2021 09:00");
        secondRecourse.addSchedule(workingHoursForSecond0);
        secondRecourse.addSchedule(workingHoursForSecond1);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        Group justSecondRecourse = new Group();
        justSecondRecourse.addRecourseInTheGroup(secondRecourse);

        Operation first0 = new Operation();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        //Operation second0 = new OperationWithPrioritiesByHeirs();
        Operation second0 = new Operation();
        second0.setResourceGroup(justSecondRecourse);
        second0.setDurationOfExecution(Duration.ofHours(3));
        second0.setOperatingMode(0);


        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first0);
        operations.add(second0);

        Series firstSeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 10:00");
        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);

        Operation first1 = new Operation();
        first1.setResourceGroup(justSecondRecourse);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        ArrayList<Operation> operationsForSecondSeries = new ArrayList<>();
        operations.add(first1);

        Series secondSeries = new Series(operationsForSecondSeries, "17-08-2021 00:00", "18-08-2021 10:00");
        first1.setSerialAffiliation(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        ArrayList<Operation> operationsToInstall = new ArrayList<>();
        operationsToInstall.add(first0);
        operationsToInstall.add(second0);
        operationsToInstall.add(first1);

        Main.installOperationsWithFutureFrontOfWork2(operationsToInstall, Duration.ofHours(2));

        WorkingHours expectation = new WorkingHours("15-08-2021 10:00", "15-08-2021 13:00");
        assertEquals(expectation.toString(), first0.getCWorkingInterval().toString());

        expectation = new WorkingHours("15-08-2021 10:00", "15-08-2021 13:00");
        assertEquals(expectation.toString(), second0.getCWorkingInterval().toString());
    }

    @Test
    public void installOperationsTest() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 19:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 19:00");

        Recourse firstRecourse = new Recourse("15-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        WorkingHours workingHoursForSecond0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 19:00");
        WorkingHours workingHoursForSecond1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 19:00");

        Recourse secondRecourse = new Recourse("15-08-2021 09:00");
        secondRecourse.addSchedule(workingHoursForSecond0);
        secondRecourse.addSchedule(workingHoursForSecond1);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        Group justSecondRecourse = new Group();
        justSecondRecourse.addRecourseInTheGroup(secondRecourse);


        Operation first0 = new Operation();
        first0.setResourceGroup(justSecondRecourse);
        first0.setDurationOfExecution(Duration.ofHours(5));
        first0.setOperatingMode(0);

        Operation second0 = new Operation();
        second0.setResourceGroup(justSecondRecourse);
        second0.setDurationOfExecution(Duration.ofHours(3));
        second0.setOperatingMode(0);

        first0.addFollowingOperation(second0);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first0);
        operations.add(second0);

        Series firstSeries = new Series(operations, "25-08-2021 00:00", "15-08-2021 10:00");
        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);



        Operation first1 = new Operation();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(7));
        first1.setOperatingMode(0);

        ArrayList<Operation> operationsForSecondSeries = new ArrayList<>();
        operationsForSecondSeries.add(first1);

        Series secondSeries = new Series(operationsForSecondSeries, "17-08-2021 00:00", "15-08-2021 12:00");
        first1.setSerialAffiliation(secondSeries);



        Operation first2 = new Operation();
        first2.setResourceGroup(justSecondRecourse);
        first2.setDurationOfExecution(Duration.ofHours(2));
        first2.setOperatingMode(0);

        ArrayList<Operation> operationsForThirdSeries = new ArrayList<>();
        operationsForThirdSeries.add(first2);

        Series thirdSeries = new Series(operationsForThirdSeries, "30-08-2021 00:00", "15-08-2021 11:00");
        first2.setSerialAffiliation(thirdSeries);



        firstRecourse.fillScheduleUsingPreviousData(thirdSeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(thirdSeries.getDeadlineForCompletion());

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);
        seriesToWork.add(thirdSeries);

        Main.takeSeriesToWorkExtendedWithFutureFrontOfWork(seriesToWork, Duration.ofHours(4), new ControlParameters(2,1,0));

        for(Series series: seriesToWork){
            for(Operation operation: series.getOperationsToCreate()){
                System.out.println(operation);
            }
        }
    }

}