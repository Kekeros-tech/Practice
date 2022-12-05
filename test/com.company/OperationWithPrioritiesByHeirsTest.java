package com.company;

import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class OperationWithPrioritiesByHeirsTest {

    @Test
    public void testOfPriorities() {
        /*WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 15:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 15:00");

        Recourse firstRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        //Operation first0 = new OperationWithPrioritiesByHeirs();
        Operation first0 = new OperationWithPrioritiesByDuration();
        //Operation first0 = new Operation();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        //Operation second0 = new OperationWithPrioritiesByHeirs();
        Operation second0 = new OperationWithPrioritiesByDuration();
        //Operation second0 = new Operation();
        second0.setResourceGroup(justFirstRecourse);
        second0.setDurationOfExecution(Duration.ofHours(3));
        second0.setOperatingMode(1);

        //Operation third0 = new OperationWithPrioritiesByHeirs();
        Operation third0 = new OperationWithPrioritiesByDuration();
        //Operation third0 = new Operation();
        third0.setResourceGroup(justFirstRecourse);
        third0.setDurationOfExecution(Duration.ofHours(3));
        third0.setOperatingMode(1);

        //Operation fourth0 = new OperationWithPrioritiesByHeirs();
        Operation fourth0 = new OperationWithPrioritiesByDuration();
        //Operation fourth0 = new Operation();
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

        Series firstSeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 09:00");

        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);
        third0.setSerialAffiliation(firstSeries);
        fourth0.setSerialAffiliation(firstSeries);

        //Operation first1 = new OperationWithPrioritiesByHeirs();
        Operation first1 = new OperationWithPrioritiesByDuration();
        //Operation first1 = new Operation();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        //Operation second1 = new OperationWithPrioritiesByHeirs();
        Operation second1 = new OperationWithPrioritiesByDuration();
        //Operation second1 = new Operation();
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

        //Main.takeSeriesToWork(seriesToWork);

        Main.takeSeriesToWorkExtended(seriesToWork, new ControlParameters(1,1, 0));

        operations.addAll(operations1);

        OComparatorBasedOnWorkingInterval sorter = new OComparatorBasedOnWorkingInterval();
        operations.sort(sorter);

        for(Operation currentOperation: operations) {
            System.out.println(currentOperation);
        }*/
    }

    @Test
    public void TestOfNew() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 15:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 15:00");

        Recourse firstRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        //Operation first0 = new OperationWithPrioritiesByHeirs();
        Operation first0 = new OperationWithPriorityNew();
        //Operation first0 = new Operation();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        //Operation second0 = new OperationWithPrioritiesByHeirs();
        Operation second0 = new OperationWithPriorityNew();
        //Operation second0 = new Operation();
        second0.setResourceGroup(justFirstRecourse);
        second0.setDurationOfExecution(Duration.ofHours(3));
        second0.setOperatingMode(1);

        //Operation third0 = new OperationWithPrioritiesByHeirs();
        Operation third0 = new OperationWithPriorityNew();
        //Operation third0 = new Operation();
        third0.setResourceGroup(justFirstRecourse);
        third0.setDurationOfExecution(Duration.ofHours(3));
        third0.setOperatingMode(1);

        //Operation fourth0 = new OperationWithPrioritiesByHeirs();
        Operation fourth0 = new OperationWithPriorityNew();
        //Operation fourth0 = new Operation();
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

        Series firstSeries = new Series(operations, "30-08-2021 00:00", "15-08-2021 09:00");

        first0.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);
        third0.setSerialAffiliation(firstSeries);
        fourth0.setSerialAffiliation(firstSeries);

        //Operation first1 = new OperationWithPrioritiesByHeirs();
        Operation first1 = new OperationWithPriorityNew();
        //Operation first1 = new Operation();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        //Operation second1 = new OperationWithPrioritiesByHeirs();
        Operation second1 = new OperationWithPriorityNew();
        //Operation second1 = new Operation();
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

        //Main.takeSeriesToWork(seriesToWork);

        Main.testAlgo(seriesToWork, new ControlParameters(1,1, 0));

        operations.addAll(operations1);

        OComparatorBasedOnWorkingInterval sorter = new OComparatorBasedOnWorkingInterval();
        operations.sort(sorter);

        for(Operation currentOperation: operations) {
            System.out.println(currentOperation);
        }
    }

}