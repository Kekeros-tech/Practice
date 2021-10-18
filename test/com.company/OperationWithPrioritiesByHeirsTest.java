package com.company;

import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class OperationWithPrioritiesByHeirsTest {

    @Test
    public void testOfPriorities() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");

        Recourse firstRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        Operation first0 = new OperationWithPrioritiesByHeirs();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        Operation second0 = new OperationWithPrioritiesByHeirs();
        second0.setResourceGroup(justFirstRecourse);
        second0.setDurationOfExecution(Duration.ofHours(3));
        second0.setOperatingMode(0);

        Operation third0 = new OperationWithPrioritiesByHeirs();
        third0.setResourceGroup(justFirstRecourse);
        third0.setDurationOfExecution(Duration.ofHours(3));
        third0.setOperatingMode(0);

        Operation fourth0 = new OperationWithPrioritiesByHeirs();
        fourth0.setResourceGroup(justFirstRecourse);
        fourth0.setDurationOfExecution(Duration.ofHours(3));
        fourth0.setOperatingMode(0);

        first0.addFollowingOperation(second0);
        first0.addFollowingOperation(third0);
        second0.addFollowingOperation(fourth0);
        third0.addFollowingOperation(fourth0);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first0);
        operations.add(second0);
        operations.add(third0);
        operations.add(fourth0);

        Series firstSeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 10:00");

        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);
        third0.setSerialAffiliation(firstSeries);
        fourth0.setSerialAffiliation(firstSeries);

        Operation first1 = new OperationWithPrioritiesByHeirs();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        Operation second1 = new OperationWithPrioritiesByHeirs();
        second1.setResourceGroup(justFirstRecourse);
        second1.setDurationOfExecution(Duration.ofHours(3));
        second1.setOperatingMode(0);

        first1.addFollowingOperation(second1);

        ArrayList<Operation> operations1 = new ArrayList<>();
        operations1.add(first1);
        operations1.add(second1);

        Series secondSeries = new Series(operations1, "19-08-2021 00:00", "15-08-2021 10:00");

        first1.setSerialAffiliation(secondSeries);
        second1.setSerialAffiliation(secondSeries);

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);
        seriesToWork.add(secondSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Main.takeSeriesToWork(seriesToWork);

        operations.addAll(operations1);

        OComparatorBasedOnWorkingInterval sorter = new OComparatorBasedOnWorkingInterval();
        operations.sort(sorter);

        for(Operation currentOperation: operations) {
            if(currentOperation.getSerialAffiliation() == firstSeries){
                System.out.println("Операция из 1 серии");
            }
            else {
                System.out.println("Операция из 2 серии");
            }
            System.out.println(((OperationWithPrioritiesByHeirs) currentOperation).getPrioritiesByHeirs());
            System.out.println(currentOperation.getCLateStartTime());
            System.out.println(currentOperation.getCWorkingInterval());
            System.out.println();
        }
    }

}