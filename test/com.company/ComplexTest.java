package com.company;

import com.company.arrangement_algo.Algo_WithCPAndFuture;
import com.company.comparator.OComparatorBasedOnWorkingInterval;
import com.company.control_param.ControlParameters;
import com.company.operation.IOperation;
import com.company.operation.O_Basic;
import com.company.operation.O_Priority;
import com.company.param_selection_algo.PSA_byEGO;
import com.company.recourse.pmc_machine.Recourse;
import org.junit.Test;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testOfApplicants() {
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

        //Operation first0 = new OperationWithPrioritiesByHeirs();
        IOperation first0 = new O_Priority();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        //Operation second0 = new OperationWithPrioritiesByHeirs();
        IOperation second0 = new O_Priority();
        second0.setResourceGroup(justFirstRecourse);
        second0.setDurationOfExecution(Duration.ofHours(3));
        second0.setOperatingMode(1);

        //Operation third0 = new OperationWithPrioritiesByHeirs();
        IOperation third0 = new O_Priority();
        third0.setResourceGroup(justSecondRecourse);
        third0.setDurationOfExecution(Duration.ofHours(3));
        third0.setOperatingMode(1);

        first0.addFollowingOperation(third0);
        second0.addFollowingOperation(third0);

        ArrayList<IOperation> operations = new ArrayList<>();
        operations.add(first0);
        operations.add(second0);
        operations.add(third0);

        Series firstSeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 10:00");

        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);
        third0.setSerialAffiliation(firstSeries);

        //Operation first1 = new OperationWithPrioritiesByHeirs();
        IOperation first1 = new O_Priority();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        //Operation second1 = new OperationWithPrioritiesByHeirs();
        IOperation second1 = new O_Priority();
        second1.setResourceGroup(justFirstRecourse);
        second1.setDurationOfExecution(Duration.ofHours(3));
        second1.setOperatingMode(0);

        //Operation third1 = new OperationWithPrioritiesByHeirs();
        IOperation third1 = new O_Priority();
        third1.setResourceGroup(justSecondRecourse);
        third1.setDurationOfExecution(Duration.ofHours(3));
        third1.setOperatingMode(0);

        first1.addFollowingOperation(second1);
        second1.addFollowingOperation(third1);

        ArrayList<IOperation> operations1 = new ArrayList<>();
        operations1.add(first1);
        operations1.add(second1);
        operations1.add(third1);

        Series secondSeries = new Series(operations1, "19-08-2021 00:00", "15-08-2021 10:00");

        first1.setSerialAffiliation(secondSeries);
        second1.setSerialAffiliation(secondSeries);
        third1.setSerialAffiliation(secondSeries);

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion().plusDays(2));
        secondRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion().plusDays(2));

        Main.testAlgo(seriesToWork, new ControlParameters(2, 1, 0));
        //Main.takeSeriesToWorkExtended(seriesToWork, new ControlParameters(2,1,0));

        operations.addAll(operations1);

        for(Series series: seriesToWork){
            for(IOperation operation: series.getOperationsToCreate()){
                System.out.println(operation.getCWorkingInterval());
            }
        }

        OComparatorBasedOnWorkingInterval sorter = new OComparatorBasedOnWorkingInterval();
        operations.sort(sorter);

        WorkingHours expectation = new WorkingHours("15-08-2021 10:00", "15-08-2021 13:00");
        assertEquals(expectation.toString(), first1.getCWorkingInterval().get(0).toString());

        expectation = new WorkingHours("16-08-2021 09:00", "16-08-2021 12:00");
        assertEquals(expectation.toString(), second1.getCWorkingInterval().get(0).toString());

        expectation = new WorkingHours("17-08-2021 09:00", "17-08-2021 12:00");
        assertEquals(expectation.toString(), third1.getCWorkingInterval().get(0).toString());

        expectation = new WorkingHours("17-08-2021 09:00", "17-08-2021 12:00");
        assertEquals(expectation.toString(), first0.getCWorkingInterval().get(0).toString());

        expectation = new WorkingHours("18-08-2021 09:00", "18-08-2021 12:00");
        assertEquals(expectation.toString(), second0.getCWorkingInterval().get(0).toString());

        expectation = new WorkingHours("19-08-2021 09:00", "19-08-2021 12:00");
        assertEquals(expectation.toString(), third0.getCWorkingInterval().get(0).toString());
    }

    @Test
    public void resourceOccupancyTesting() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 15:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 15:00");
        WorkingHours workingHoursForFirst2 = new WorkingHours("20-08-2021 09:00", "20-08-2021 18:00");

        Recourse firstRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);
        firstRecourse.addSchedule(workingHoursForFirst2);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        O_Basic first0 = new O_Basic();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(5));
        first0.setOperatingMode(0);

        O_Basic second0 = new O_Basic();
        second0.setResourceGroup(justFirstRecourse);
        second0.setDurationOfExecution(Duration.ofHours(5));
        second0.setOperatingMode(0);


        first0.addFollowingOperation(second0);

        ArrayList<IOperation> operations = new ArrayList<>();
        operations.add(first0);
        operations.add(second0);

        Series firstSeries = new Series(operations, "15-09-2021 00:00", "13-08-2021 10:00");

        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);

        O_Basic first1 = new O_Basic();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(9));
        first1.setOperatingMode(0);

        ArrayList<IOperation> operations1 = new ArrayList<>();
        operations1.add(first1);

        Series secondSeries = new Series(operations1, "25-08-2021 00:00", "13-08-2021 10:00");
        first1.setSerialAffiliation(secondSeries);

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Main.takeSeriesToWork(seriesToWork);

        operations.addAll(operations1);

        OComparatorBasedOnWorkingInterval sorter = new OComparatorBasedOnWorkingInterval();
        operations.sort(sorter);

        WorkingHours expectation = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        assertEquals(expectation.toString(), first0.getCWorkingInterval().toString());

        expectation = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");
        assertEquals(expectation.toString(), second0.getCWorkingInterval().toString());

        expectation = new WorkingHours("20-08-2021 09:00", "20-08-2021 18:00");
        assertEquals(expectation.toString(), first1.getCWorkingInterval().toString());
    }

    @Test
    public void fillingFutureFrontOfWorkByPriorityOperations() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 19:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 19:00");

        Recourse firstRecourse = new Recourse("15-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);



        IOperation first0 = new O_Priority();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        ArrayList<IOperation> operations = new ArrayList<>();
        operations.add(first0);

        Series firstSeries = new Series(operations, "25-08-2021 00:00", "15-08-2021 09:00");
        first0.setSerialAffiliation(firstSeries);



        IOperation first1 = new O_Priority();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(7));
        first1.setOperatingMode(0);

        ArrayList<IOperation> operationsForSecondSeries = new ArrayList<>();
        operationsForSecondSeries.add(first1);

        Series secondSeries = new Series(operationsForSecondSeries, "17-08-2021 00:00", "15-08-2021 12:00");
        first1.setSerialAffiliation(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        Main.testFutureAlgo(seriesToWork, Duration.ofHours(5), new ControlParameters(2, 1, 0));
        //Main.takeSeriesToWorkExtendedWithFutureFrontOfWork(seriesToWork, Duration.ofHours(5), new ControlParameters(2,1,0));

        for(Series series: seriesToWork){
            for(IOperation operation: series.getOperationsToCreate()){
                System.out.println(operation);
            }
        }
    }

    @Test
    //Use early and late start dates
    public void asymmetricSeriesWithDifferentArrivalTimesAndDeadlines() {
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

        Group allRecourses = new Group();
        allRecourses.addRecourseInTheGroup(firstRecourse);
        allRecourses.addRecourseInTheGroup(secondRecourse);


        IOperation first0 = new O_Priority();
        first0.setNameOfOperation("Первая операция, первой серии");
        first0.setResourceGroup(allRecourses);
        first0.setDurationOfExecution(Duration.ofHours(7));
        first0.setOperatingMode(0);

        IOperation second0 = new O_Priority();
        second0.setNameOfOperation("Вторая операция, первой серии");
        second0.setResourceGroup(allRecourses);
        second0.setDurationOfExecution(Duration.ofHours(7));
        second0.setOperatingMode(0);

        IOperation third0 = new O_Priority();
        third0.setNameOfOperation("Третья операция, первой серии");
        third0.setResourceGroup(justFirstRecourse);
        third0.setDurationOfExecution(Duration.ofHours(7));
        third0.setOperatingMode(0);

        IOperation fourth0 = new O_Priority();
        fourth0.setNameOfOperation("Четвертая операция, первой серии");
        fourth0.setResourceGroup(justSecondRecourse);
        fourth0.setDurationOfExecution(Duration.ofHours(7));
        fourth0.setOperatingMode(0);

        first0.addFollowingOperation(fourth0);
        second0.addFollowingOperation(fourth0);
        third0.addFollowingOperation(fourth0);

        ArrayList<IOperation> operationsOfFirstSeries = new ArrayList<>();
        operationsOfFirstSeries.add(first0);
        operationsOfFirstSeries.add(second0);
        operationsOfFirstSeries.add(third0);
        operationsOfFirstSeries.add(fourth0);

        Series firstSeries = new Series(operationsOfFirstSeries, "30-09-2021 00:00", "15-08-2021 10:00");
        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);
        third0.setSerialAffiliation(firstSeries);
        fourth0.setSerialAffiliation(firstSeries);



        IOperation first1 = new O_Priority();
        first1.setNameOfOperation("Первая операция, второй серии");
        first1.setResourceGroup(allRecourses);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        IOperation second1 = new O_Priority();
        second1.setNameOfOperation("Вторая операция, второй серии");
        second1.setResourceGroup(justSecondRecourse);
        second1.setDurationOfExecution(Duration.ofHours(3));
        second1.setOperatingMode(0);

        IOperation third1 = new O_Priority();
        third1.setNameOfOperation("Третья операция, второй серии");
        third1.setResourceGroup(justFirstRecourse);
        third1.setDurationOfExecution(Duration.ofHours(3));
        third1.setOperatingMode(0);

        IOperation fourth1 = new O_Priority();
        fourth1.setNameOfOperation("Четвертая операция, второй серии");
        fourth1.setResourceGroup(justSecondRecourse);
        fourth1.setDurationOfExecution(Duration.ofHours(3));
        fourth1.setOperatingMode(0);

        first1.addFollowingOperation(second1);
        first1.addFollowingOperation(third1);
        first1.addFollowingOperation(fourth1);


        ArrayList<IOperation> operationsOfSecondSeries = new ArrayList<>();
        operationsOfSecondSeries.add(first1);
        operationsOfSecondSeries.add(second1);
        operationsOfSecondSeries.add(third1);
        operationsOfSecondSeries.add(fourth1);

        Series secondSeries = new Series(operationsOfSecondSeries, "01-09-2021 00:00", "15-08-2021 15:00");
        first1.setSerialAffiliation(secondSeries);
        second1.setSerialAffiliation(secondSeries);
        third1.setSerialAffiliation(secondSeries);
        fourth1.setSerialAffiliation(secondSeries);

        ArrayList<IOperation> operationsForSecondSeries = new ArrayList<>();
        operationsForSecondSeries.add(first1);
        operationsForSecondSeries.add(second1);
        operationsForSecondSeries.add(third1);
        operationsForSecondSeries.add(fourth1);


        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Main.testFutureAlgo(seriesToWork, Duration.ofHours(10), new ControlParameters(0, 1, 0));
        //Main.takeSeriesToWorkExtendedWithFutureFrontOfWork(seriesToWork, Duration.ofHours(10), new ControlParameters(2,1,0));

        for(Series series: seriesToWork){
            for(IOperation operation: series.getOperationsToCreate()) {
                System.out.println(operation);
            }
        }
    }

    @Test
    //Use early and late start dates
    public void asymmetricSeriesWithDifferentArrivalTimesAndDeadlinesSecond() throws IOException, InterruptedException {
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

        WorkingHours workingHoursForThird0 = new WorkingHours("15-08-2021 11:00", "15-08-2021 17:00");
        WorkingHours workingHoursForThird1 = new WorkingHours("16-08-2021 11:00", "16-08-2021 17:00");

        Recourse thirdRecourse = new Recourse("15-08-2021 12:00");
        thirdRecourse.addSchedule(workingHoursForThird0);
        thirdRecourse.addSchedule(workingHoursForThird1);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        Group justSecondRecourse = new Group();
        justSecondRecourse.addRecourseInTheGroup(secondRecourse);

        Group allRecourses = new Group();
        allRecourses.addRecourseInTheGroup(firstRecourse);
        allRecourses.addRecourseInTheGroup(secondRecourse);

        Group justThirdRecourse = new Group();
        justThirdRecourse.addRecourseInTheGroup(thirdRecourse);

        Group secondAndThird = new Group();
        secondAndThird.addRecourseInTheGroup(secondRecourse);
        secondAndThird.addRecourseInTheGroup(thirdRecourse);

        Group firstAndSecond = new Group();
        firstAndSecond.addRecourseInTheGroup(firstRecourse);
        firstAndSecond.addRecourseInTheGroup(secondRecourse);

        Group firstAndThird = new Group();
        firstAndThird.addRecourseInTheGroup(firstRecourse);
        firstAndThird.addRecourseInTheGroup(thirdRecourse);


        IOperation first0 = new O_Priority();
        first0.setNameOfOperation("Первая операция, первой серии");
        first0.setResourceGroup(secondAndThird);
        first0.setDurationOfExecution(Duration.ofHours(5));
        first0.setOperatingMode(0);

        IOperation second0 = new O_Priority();
        second0.setNameOfOperation("Вторая операция, первой серии");
        second0.setResourceGroup(firstAndThird);
        second0.setDurationOfExecution(Duration.ofHours(6));
        second0.setOperatingMode(0);

        IOperation third0 = new O_Priority();
        third0.setNameOfOperation("Третья операция, первой серии");
        third0.setResourceGroup(firstAndSecond);
        third0.setDurationOfExecution(Duration.ofHours(7));
        third0.setOperatingMode(0);

        IOperation fourth0 = new O_Priority();
        fourth0.setNameOfOperation("Четвертая операция, первой серии");
        fourth0.setResourceGroup(allRecourses);
        fourth0.setDurationOfExecution(Duration.ofHours(5));
        fourth0.setOperatingMode(0);

        IOperation fifth0 = new O_Priority();
        fifth0.setNameOfOperation("Пятая операция, первой серии");
        fifth0.setResourceGroup(allRecourses);
        fifth0.setDurationOfExecution(Duration.ofHours(4));
        fifth0.setOperatingMode(0);

        IOperation sixth0 = new O_Priority();
        sixth0.setNameOfOperation("Шестая операция, первой серии");
        sixth0.setResourceGroup(firstAndSecond);
        sixth0.setDurationOfExecution(Duration.ofHours(3));
        sixth0.setOperatingMode(0);

        first0.addFollowingOperation(fourth0);
        second0.addFollowingOperation(fourth0);
        third0.addFollowingOperation(fifth0);
        fourth0.addFollowingOperation(sixth0);
        fifth0.addFollowingOperation(sixth0);

        ArrayList<IOperation> operationsOfFirstSeries = new ArrayList<>();
        operationsOfFirstSeries.add(first0);
        operationsOfFirstSeries.add(second0);
        operationsOfFirstSeries.add(third0);
        operationsOfFirstSeries.add(fourth0);
        operationsOfFirstSeries.add(fifth0);
        operationsOfFirstSeries.add(sixth0);

        Series firstSeries = new Series(operationsOfFirstSeries, "01-09-2021 00:00", "15-08-2021 10:00");
        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);
        third0.setSerialAffiliation(firstSeries);
        fourth0.setSerialAffiliation(firstSeries);
        fifth0.setSerialAffiliation(firstSeries);
        sixth0.setSerialAffiliation(firstSeries);



        IOperation first1 = new O_Priority();
        first1.setNameOfOperation("Первая операция, второй серии");
        first1.setResourceGroup(allRecourses);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        IOperation second1 = new O_Priority();
        second1.setNameOfOperation("Вторая операция, второй серии");
        second1.setResourceGroup(justFirstRecourse);
        second1.setDurationOfExecution(Duration.ofHours(3));
        second1.setOperatingMode(0);

        IOperation third1 = new O_Priority();
        third1.setNameOfOperation("Третья операция, второй серии");
        third1.setResourceGroup(justSecondRecourse);
        third1.setDurationOfExecution(Duration.ofHours(3));
        third1.setOperatingMode(0);

        IOperation fourth1 = new O_Priority();
        fourth1.setNameOfOperation("Четвертая операция, второй серии");
        fourth1.setResourceGroup(justThirdRecourse);
        fourth1.setDurationOfExecution(Duration.ofHours(3));
        fourth1.setOperatingMode(0);

        IOperation fifth1 = new O_Priority();
        fifth1.setNameOfOperation("Пятая операция, второй сериии");
        fifth1.setResourceGroup(justFirstRecourse);
        fifth1.setDurationOfExecution(Duration.ofHours(3));
        fifth1.setOperatingMode(0);

        IOperation sixth1 = new O_Priority();
        sixth1.setNameOfOperation("Шестая операция, второй серии");
        sixth1.setResourceGroup(justSecondRecourse);
        sixth1.setDurationOfExecution(Duration.ofHours(3));
        sixth1.setOperatingMode(0);

        first1.addFollowingOperation(second1);
        first1.addFollowingOperation(third1);
        first1.addFollowingOperation(fourth1);
        first1.addFollowingOperation(fifth1);
        first1.addFollowingOperation(sixth1);


        ArrayList<IOperation> operationsOfSecondSeries = new ArrayList<>();
        operationsOfSecondSeries.add(first1);
        operationsOfSecondSeries.add(second1);
        operationsOfSecondSeries.add(third1);
        operationsOfSecondSeries.add(fourth1);
        operationsOfSecondSeries.add(fifth1);
        operationsOfSecondSeries.add(sixth1);

        Series secondSeries = new Series(operationsOfSecondSeries, "25-09-2021 00:00", "15-08-2021 13:00");
        first1.setSerialAffiliation(secondSeries);
        second1.setSerialAffiliation(secondSeries);
        third1.setSerialAffiliation(secondSeries);
        fourth1.setSerialAffiliation(secondSeries);
        fifth1.setSerialAffiliation(secondSeries);
        sixth1.setSerialAffiliation(secondSeries);



        IOperation first2 = new O_Priority();
        first2.setNameOfOperation("Первая операция, третья серия");
        first2.setResourceGroup(allRecourses);
        first2.setDurationOfExecution(Duration.ofHours(2));
        first2.setOperatingMode(0);

        IOperation second2 = new O_Priority();
        second2.setNameOfOperation("Вторая операция, третья серия");
        second2.setResourceGroup(justFirstRecourse);
        second2.setDurationOfExecution(Duration.ofHours(9));
        second2.setOperatingMode(0);

        IOperation third2 = new O_Priority();
        third2.setNameOfOperation("Третья операция, третья серия");
        third2.setResourceGroup(justSecondRecourse);
        third2.setDurationOfExecution(Duration.ofHours(9));
        third2.setOperatingMode(0);

        first2.addFollowingOperation(second2);
        first2.addFollowingOperation(third2);

        ArrayList<IOperation> operationsForThirdSeries = new ArrayList<>();
        operationsForThirdSeries.add(first2);
        operationsForThirdSeries.add(second2);
        operationsForThirdSeries.add(third2);

        Series thirdSeries = new Series(operationsForThirdSeries, "10-09-2021 00:00", "15-08-2021 15:00");
        first2.setSerialAffiliation(thirdSeries);
        second2.setSerialAffiliation(thirdSeries);
        third2.setSerialAffiliation(thirdSeries);


        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);
        seriesToWork.add(thirdSeries);

        firstRecourse.fillScheduleUsingPreviousData(secondSeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(secondSeries.getDeadlineForCompletion());
        thirdRecourse.fillScheduleUsingPreviousData(secondSeries.getDeadlineForCompletion());

        Algo_WithCPAndFuture algo =
                new Algo_WithCPAndFuture(seriesToWork,
                        new ControlParameters(1, 1, 1),
                        Duration.ofHours(1));

        PSA_byEGO ego = new PSA_byEGO("D:\\My_EGO_realization\\hello.py", 10,
                "D:\\My_EGO_realization\\text.txt");

        ego.calculateBestSolution(algo);
        System.out.println("----");
        System.out.println(ego.getBestSolution());
        //ego.resetResult();
    }

    @Test
    //Use early and late start dates
    public void testWhereSecondSortingReturnBestResult() throws IOException, InterruptedException {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 15:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 15:00");

        Recourse firstRecourse = new Recourse("15-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        WorkingHours workingHoursForSecond0 = new WorkingHours("18-08-2021 09:00", "18-08-2021 16:00");
        WorkingHours workingHoursForSecond1 = new WorkingHours("19-08-2021 09:00", "19-08-2021 16:00");

        Recourse secondRecourse = new Recourse("16-08-2021 09:00");
        secondRecourse.addSchedule(workingHoursForSecond0);
        secondRecourse.addSchedule(workingHoursForSecond1);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        Group allRecourses = new Group();
        allRecourses.addRecourseInTheGroup(firstRecourse);
        allRecourses.addRecourseInTheGroup(secondRecourse);

        IOperation first0 = new O_Priority();
        first0.setNameOfOperation("Первая операция, первой серии");
        first0.setResourceGroup(allRecourses);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        IOperation second0 = new O_Priority();
        second0.setNameOfOperation("Вторая операция, первой серии");
        second0.setResourceGroup(allRecourses);
        second0.setDurationOfExecution(Duration.ofHours(4));
        second0.setOperatingMode(0);


        ArrayList<IOperation> operationsOfFirstSeries = new ArrayList<>();
        operationsOfFirstSeries.add(first0);
        operationsOfFirstSeries.add(second0);

        Series firstSeries = new Series(operationsOfFirstSeries, "01-09-2021 00:00", "15-08-2021 09:00");
        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);


        IOperation first1 = new O_Priority();
        first1.setNameOfOperation("Первая операция, второй серии");
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(4));
        first1.setOperatingMode(0);

        IOperation second1 = new O_Priority();
        second1.setNameOfOperation("Вторая операция, второй серии");
        second1.setResourceGroup(justFirstRecourse);
        second1.setDurationOfExecution(Duration.ofHours(6));
        second1.setOperatingMode(0);

        IOperation third1 = new O_Priority();
        third1.setNameOfOperation("Третья операция, второй серии");
        third1.setResourceGroup(justFirstRecourse);
        third1.setDurationOfExecution(Duration.ofHours(6));
        third1.setOperatingMode(0);

        IOperation fourth1 = new O_Priority();
        fourth1.setNameOfOperation("Четвертая операция, второй серии");
        fourth1.setResourceGroup(justFirstRecourse);
        fourth1.setDurationOfExecution(Duration.ofHours(6));
        fourth1.setOperatingMode(0);

        IOperation fifth1 = new O_Priority();
        fifth1.setNameOfOperation("Пятая операция, второй серии");
        fifth1.setResourceGroup(justFirstRecourse);
        fifth1.setDurationOfExecution(Duration.ofHours(6));
        fifth1.setOperatingMode(0);

        IOperation sixth1 = new O_Priority();
        sixth1.setNameOfOperation("Шестая операция, второй серии");
        sixth1.setResourceGroup(justFirstRecourse);
        sixth1.setDurationOfExecution(Duration.ofHours(6));
        sixth1.setOperatingMode(0);

        first1.addFollowingOperation(second1);
        first1.addFollowingOperation(third1);
        first1.addFollowingOperation(fourth1);
        first1.addFollowingOperation(fifth1);
        first1.addFollowingOperation(sixth1);


        ArrayList<IOperation> operationsOfSecondSeries = new ArrayList<>();
        operationsOfSecondSeries.add(first1);
        operationsOfSecondSeries.add(second1);
        operationsOfSecondSeries.add(third1);
        operationsOfSecondSeries.add(fourth1);
        operationsOfSecondSeries.add(fifth1);
        operationsOfSecondSeries.add(sixth1);

        Series secondSeries = new Series(operationsOfSecondSeries, "25-09-2021 00:00", "15-08-2021 11:00");
        first1.setSerialAffiliation(secondSeries);
        second1.setSerialAffiliation(secondSeries);
        third1.setSerialAffiliation(secondSeries);
        fourth1.setSerialAffiliation(secondSeries);
        fifth1.setSerialAffiliation(secondSeries);
        sixth1.setSerialAffiliation(secondSeries);

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(secondSeries.getDeadlineForCompletion());

        Algo_WithCPAndFuture algo =
                new Algo_WithCPAndFuture(seriesToWork,
                        new ControlParameters(1,1,1),
                        Duration.ofHours(2));

        PSA_byEGO ego = new PSA_byEGO("D:\\My_EGO_realization\\hello.py", 10,
                "D:\\My_EGO_realization\\testWhereSecondSortingReturnBestResult.txt");
        ego.calculateBestSolution(algo);
        System.out.println(ego.getBestSolution());
        ego.resetResult();
    }
}
