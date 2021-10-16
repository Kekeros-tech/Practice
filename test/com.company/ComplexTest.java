package com.company;

import org.junit.Test;

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

        Operation first0 = new OperationWithPrioritiesByHeirs();
        first0.setResourceGroup(justFirstRecourse);
        first0.setDurationOfExecution(Duration.ofHours(3));
        first0.setOperatingMode(0);

        Operation second0 = new OperationWithPrioritiesByHeirs();
        second0.setResourceGroup(justFirstRecourse);
        second0.setDurationOfExecution(Duration.ofHours(3));
        second0.setOperatingMode(0);

        Operation third0 = new OperationWithPrioritiesByHeirs();
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

        Operation first1 = new OperationWithPrioritiesByHeirs();
        first1.setResourceGroup(justFirstRecourse);
        first1.setDurationOfExecution(Duration.ofHours(3));
        first1.setOperatingMode(0);

        Operation second1 = new OperationWithPrioritiesByHeirs();
        second1.setResourceGroup(justFirstRecourse);
        second1.setDurationOfExecution(Duration.ofHours(3));
        second1.setOperatingMode(0);

        Operation third1 = new OperationWithPrioritiesByHeirs();
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
            System.out.println(currentOperation.getCLateStartTime());
            //System.out.println(currentOperation.getCNumberOfAssignedRecourse());
            System.out.println(currentOperation.getCWorkingInterval());
        }

    }

}