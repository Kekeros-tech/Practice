package com.company;

import org.junit.Test;

import javax.jnlp.FileContents;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        Operation first0 = new Operation();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        //Operation second0 = new OperationWithPrioritiesByHeirs();
        Operation second0 = new Operation();
        second0.setResourceGroup(justFirstRecourse);
        second0.setDurationOfExecution(Duration.ofHours(3));
        second0.setOperatingMode(0);

        //Operation third0 = new OperationWithPrioritiesByHeirs();
        Operation third0 = new Operation();
        third0.setResourceGroup(justSecondRecourse);
        third0.setDurationOfExecution(Duration.ofHours(3));
        third0.setOperatingMode(0);

        first0.addFollowingOperation(third0);
        second0.addFollowingOperation(third0);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first0);
        operations.add(second0);
        operations.add(third0);

        Series firstSeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 10:00");

        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);
        third0.setSerialAffiliation(firstSeries);

        //Operation first1 = new OperationWithPrioritiesByHeirs();
        Operation first1 = new Operation();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        //Operation second1 = new OperationWithPrioritiesByHeirs();
        Operation second1 = new Operation();
        second1.setResourceGroup(justFirstRecourse);
        second1.setDurationOfExecution(Duration.ofHours(3));
        second1.setOperatingMode(0);

        //Operation third1 = new OperationWithPrioritiesByHeirs();
        Operation third1 = new Operation();
        third1.setResourceGroup(justSecondRecourse);
        third1.setDurationOfExecution(Duration.ofHours(3));
        third1.setOperatingMode(0);

        first1.addFollowingOperation(second1);
        second1.addFollowingOperation(third1);

        ArrayList<Operation> operations1 = new ArrayList<>();
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

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Main.takeSeriesToWorkExtended(seriesToWork, new ControlParameters(2,1,0));

        operations.addAll(operations1);

        OComparatorBasedOnWorkingInterval sorter = new OComparatorBasedOnWorkingInterval();
        operations.sort(sorter);

        WorkingHours expectation = new WorkingHours("15-08-2021 10:00", "15-08-2021 13:00");
        assertEquals(expectation.toString(), first1.getCWorkingInterval().toString());

        expectation = new WorkingHours("16-08-2021 09:00", "16-08-2021 12:00");
        assertEquals(expectation.toString(), second1.getCWorkingInterval().toString());

        expectation = new WorkingHours("17-08-2021 09:00", "17-08-2021 12:00");
        assertEquals(expectation.toString(), third1.getCWorkingInterval().toString());

        expectation = new WorkingHours("17-08-2021 09:00", "17-08-2021 12:00");
        assertEquals(expectation.toString(), first0.getCWorkingInterval().toString());

        expectation = new WorkingHours("18-08-2021 09:00", "18-08-2021 12:00");
        assertEquals(expectation.toString(), second0.getCWorkingInterval().toString());

        expectation = new WorkingHours("19-08-2021 09:00", "19-08-2021 12:00");
        assertEquals(expectation.toString(), third0.getCWorkingInterval().toString());
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

        Operation first0 = new Operation();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(5));
        first0.setOperatingMode(0);

        Operation second0 = new Operation();
        second0.setResourceGroup(justFirstRecourse);
        second0.setDurationOfExecution(Duration.ofHours(5));
        second0.setOperatingMode(0);


        first0.addFollowingOperation(second0);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first0);
        operations.add(second0);

        Series firstSeries = new Series(operations, "15-09-2021 00:00", "13-08-2021 10:00");

        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);

        Operation first1 = new Operation();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(9));
        first1.setOperatingMode(0);

        ArrayList<Operation> operations1 = new ArrayList<>();
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



        Operation first0 = new Operation();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first0);

        Series firstSeries = new Series(operations, "25-08-2021 00:00", "15-08-2021 09:00");
        first0.setSerialAffiliation(firstSeries);



        Operation first1 = new Operation();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(7));
        first1.setOperatingMode(0);

        ArrayList<Operation> operationsForSecondSeries = new ArrayList<>();
        operationsForSecondSeries.add(first1);

        Series secondSeries = new Series(operationsForSecondSeries, "17-08-2021 00:00", "15-08-2021 12:00");
        first1.setSerialAffiliation(secondSeries);



        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        Main.takeSeriesToWorkExtendedWithFutureFrontOfWork(seriesToWork, Duration.ofHours(5), new ControlParameters(2,1,0));

        for(Series series: seriesToWork){
            for(Operation operation: series.getOperationsToCreate()){
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


        Operation first0 = new Operation();
        first0.setNameOfOperation("Первая операция, первой серии");
        first0.setResourceGroup(allRecourses);
        first0.setDurationOfExecution(Duration.ofHours(7));
        first0.setOperatingMode(0);

        Operation second0 = new Operation();
        second0.setNameOfOperation("Вторая операция, первой серии");
        second0.setResourceGroup(allRecourses);
        second0.setDurationOfExecution(Duration.ofHours(7));
        second0.setOperatingMode(0);

        Operation third0 = new Operation();
        third0.setNameOfOperation("Третья операция, первой серии");
        third0.setResourceGroup(justFirstRecourse);
        third0.setDurationOfExecution(Duration.ofHours(7));
        third0.setOperatingMode(0);

        Operation fourth0 = new Operation();
        fourth0.setNameOfOperation("Четвертая операция, первой серии");
        fourth0.setResourceGroup(justSecondRecourse);
        fourth0.setDurationOfExecution(Duration.ofHours(7));
        fourth0.setOperatingMode(0);

        first0.addFollowingOperation(fourth0);
        second0.addFollowingOperation(fourth0);
        third0.addFollowingOperation(fourth0);

        ArrayList<Operation> operationsOfFirstSeries = new ArrayList<>();
        operationsOfFirstSeries.add(first0);
        operationsOfFirstSeries.add(second0);
        operationsOfFirstSeries.add(third0);
        operationsOfFirstSeries.add(fourth0);

        Series firstSeries = new Series(operationsOfFirstSeries, "30-09-2021 00:00", "15-08-2021 10:00");
        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);
        third0.setSerialAffiliation(firstSeries);
        fourth0.setSerialAffiliation(firstSeries);



        Operation first1 = new Operation();
        first1.setNameOfOperation("Первая операция, второй серии");
        first1.setResourceGroup(allRecourses);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        Operation second1 = new Operation();
        second1.setNameOfOperation("Вторая операция, второй серии");
        second1.setResourceGroup(justSecondRecourse);
        second1.setDurationOfExecution(Duration.ofHours(3));
        second1.setOperatingMode(0);

        Operation third1 = new Operation();
        third1.setNameOfOperation("Третья операция, второй серии");
        third1.setResourceGroup(justFirstRecourse);
        third1.setDurationOfExecution(Duration.ofHours(3));
        third1.setOperatingMode(0);

        Operation fourth1 = new Operation();
        fourth1.setNameOfOperation("Четвертая операция, второй серии");
        fourth1.setResourceGroup(justSecondRecourse);
        fourth1.setDurationOfExecution(Duration.ofHours(3));
        fourth1.setOperatingMode(0);

        first1.addFollowingOperation(second1);
        first1.addFollowingOperation(third1);
        first1.addFollowingOperation(fourth1);


        ArrayList<Operation> operationsOfSecondSeries = new ArrayList<>();
        operationsOfSecondSeries.add(first1);
        operationsOfSecondSeries.add(second1);
        operationsOfSecondSeries.add(third1);
        operationsOfSecondSeries.add(fourth1);

        Series secondSeries = new Series(operationsOfSecondSeries, "01-09-2021 00:00", "15-08-2021 15:00");
        first1.setSerialAffiliation(secondSeries);
        second1.setSerialAffiliation(secondSeries);
        third1.setSerialAffiliation(secondSeries);
        fourth1.setSerialAffiliation(secondSeries);

        ArrayList<Operation> operationsForSecondSeries = new ArrayList<>();
        operationsForSecondSeries.add(first1);
        operationsForSecondSeries.add(second1);
        operationsForSecondSeries.add(third1);
        operationsForSecondSeries.add(fourth1);


        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Main.takeSeriesToWorkExtendedWithFutureFrontOfWork(seriesToWork, Duration.ofHours(10), new ControlParameters(2,1,0));

        for(Series series: seriesToWork){
            for(Operation operation: series.getOperationsToCreate()){
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

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        Group justSecondRecourse = new Group();
        justSecondRecourse.addRecourseInTheGroup(secondRecourse);

        Group allRecourses = new Group();
        allRecourses.addRecourseInTheGroup(firstRecourse);
        allRecourses.addRecourseInTheGroup(secondRecourse);


        Operation first0 = new OperationWithPriority();
        first0.setNameOfOperation("Первая операция, первой серии");
        first0.setResourceGroup(allRecourses);
        first0.setDurationOfExecution(Duration.ofHours(7));
        first0.setOperatingMode(0);

        Operation second0 = new OperationWithPriority();
        second0.setNameOfOperation("Вторая операция, первой серии");
        second0.setResourceGroup(allRecourses);
        second0.setDurationOfExecution(Duration.ofHours(3));
        second0.setOperatingMode(0);

        Operation third0 = new OperationWithPriority();
        third0.setNameOfOperation("Третья операция, первой серии");
        third0.setResourceGroup(justFirstRecourse);
        third0.setDurationOfExecution(Duration.ofHours(5));
        third0.setOperatingMode(0);

        Operation fourth0 = new OperationWithPriority();
        fourth0.setNameOfOperation("Четвертая операция, первой серии");
        fourth0.setResourceGroup(justSecondRecourse);
        fourth0.setDurationOfExecution(Duration.ofHours(4));
        fourth0.setOperatingMode(0);

        first0.addFollowingOperation(fourth0);
        second0.addFollowingOperation(fourth0);
        third0.addFollowingOperation(fourth0);

        ArrayList<Operation> operationsOfFirstSeries = new ArrayList<>();
        operationsOfFirstSeries.add(first0);
        operationsOfFirstSeries.add(second0);
        operationsOfFirstSeries.add(third0);
        operationsOfFirstSeries.add(fourth0);

        Series firstSeries = new Series(operationsOfFirstSeries, "10-09-2021 00:00", "15-08-2021 10:00");
        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);
        third0.setSerialAffiliation(firstSeries);
        fourth0.setSerialAffiliation(firstSeries);



        Operation first1 = new OperationWithPriority();
        first1.setNameOfOperation("Первая операция, второй серии");
        first1.setResourceGroup(allRecourses);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        Operation second1 = new OperationWithPriority();
        second1.setNameOfOperation("Вторая операция, второй серии");
        second1.setResourceGroup(justSecondRecourse);
        second1.setDurationOfExecution(Duration.ofHours(3));
        second1.setOperatingMode(0);

        Operation third1 = new OperationWithPriority();
        third1.setNameOfOperation("Третья операция, второй серии");
        third1.setResourceGroup(justFirstRecourse);
        third1.setDurationOfExecution(Duration.ofHours(3));
        third1.setOperatingMode(0);

        Operation fourth1 = new OperationWithPriority();
        fourth1.setNameOfOperation("Четвертая операция, второй серии");
        fourth1.setResourceGroup(justSecondRecourse);
        fourth1.setDurationOfExecution(Duration.ofHours(3));
        fourth1.setOperatingMode(0);

        first1.addFollowingOperation(second1);
        first1.addFollowingOperation(third1);
        first1.addFollowingOperation(fourth1);


        ArrayList<Operation> operationsOfSecondSeries = new ArrayList<>();
        operationsOfSecondSeries.add(first1);
        operationsOfSecondSeries.add(second1);
        operationsOfSecondSeries.add(third1);
        operationsOfSecondSeries.add(fourth1);

        Series secondSeries = new Series(operationsOfSecondSeries, "25-09-2021 00:00", "15-08-2021 15:00");
        first1.setSerialAffiliation(secondSeries);
        second1.setSerialAffiliation(secondSeries);
        third1.setSerialAffiliation(secondSeries);
        fourth1.setSerialAffiliation(secondSeries);

        ArrayList<Operation> operationsForSecondSeries = new ArrayList<>();
        operationsForSecondSeries.add(first1);
        operationsForSecondSeries.add(second1);
        operationsForSecondSeries.add(third1);
        operationsForSecondSeries.add(fourth1);


        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(secondSeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(secondSeries.getDeadlineForCompletion());

        for(int i = 0; i < 1; i++) {
            String[] arguments = new String[] {"python","D:\\My_EGO_realization\\hello.py"};
            try {
                // выполняем код Python
                Process process = Runtime.getRuntime().exec(arguments);
                // Возвращает, успешно ли выполнено, 0 означает успех, 1 означает сбой
                int re = process.waitFor();
                // вывод результата выполнения
                System.out.println(re);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileReader file = new FileReader("D:\\My_EGO_realization\\text.txt");
            Scanner scanner = new Scanner(file);
            String lastString = "";
            ArrayList<Integer> controlParameterValues = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lastString = scanner.nextLine();
                i++;
            }
            for (String current : lastString.split(" ")) {
                controlParameterValues.add(Integer.parseInt(current));
            }
            Main.takeSeriesToWorkExtendedWithFutureFrontOfWork(seriesToWork, Duration.ofSeconds(controlParameterValues.get(0)),
                    new ControlParameters(controlParameterValues.get(1), 1, 0));
            for(Series series: seriesToWork){
                for(Operation operation: series.getOperationsToCreate()) {
                    System.out.println(operation);
                }
            }
        }

        //Main.takeSeriesToWorkExtendedWithFutureFrontOfWork(seriesToWork, Duration.ofHours(9), new ControlParameters(0,1,0));


        //System.out.println(Duration.between(first0.getCWorkingInterval().getStartTime(), fourth1.getCWorkingInterval().getEndTime()));
        /*for(Series series: seriesToWork){
            for(Operation operation: series.getOperationsToCreate()) {
                System.out.println(operation);
            }
        }*/
    }
}