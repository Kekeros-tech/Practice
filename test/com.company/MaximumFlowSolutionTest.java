package com.company;

import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class MaximumFlowSolutionTest {

    @Test
    public void firstTestForMaximumFlow() {
        WorkingHours workingHoursForFirst0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 15:00");
        WorkingHours workingHoursForFirst1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 15:00");

        Recourse firstRecourse = new Recourse("14-08-2021 09:00");
        firstRecourse.addSchedule(workingHoursForFirst0);
        firstRecourse.addSchedule(workingHoursForFirst1);

        Recourse secondRecourse = new Recourse("14-08-2021 09:00");
        secondRecourse.addSchedule(workingHoursForFirst0);
        secondRecourse.addSchedule(workingHoursForFirst1);

        Recourse thirdRecourse = new Recourse("14-08-2021 09:00");
        thirdRecourse.addSchedule(workingHoursForFirst0);
        thirdRecourse.addSchedule(workingHoursForFirst1);


        Group justFirstRecourse = new Group();
        justFirstRecourse.addRecourseInTheGroup(firstRecourse);

        Group firstAndSecondRecourse = new Group();
        firstAndSecondRecourse.addRecourseInTheGroup(firstRecourse);
        firstAndSecondRecourse.addRecourseInTheGroup(secondRecourse);

        Group secondAndThird = new Group();
        secondAndThird.addRecourseInTheGroup(secondRecourse);
        secondAndThird.addRecourseInTheGroup(thirdRecourse);



        Operation first = new Operation();
        first.setResourceGroup(firstAndSecondRecourse);
        first.setDurationOfExecution(Duration.ofHours(5));
        first.setOperatingMode(0);

        Operation second0 = new Operation();
        second0.setResourceGroup(justFirstRecourse);
        second0.setDurationOfExecution(Duration.ofHours(5));
        second0.setOperatingMode(0);

        Operation third0 = new Operation();
        third0.setResourceGroup(secondAndThird);
        third0.setDurationOfExecution(Duration.ofHours(5));
        third0.setOperatingMode(0);

        Operation fourth0 = new Operation();
        fourth0.setResourceGroup(secondAndThird);
        fourth0.setDurationOfExecution(Duration.ofHours(5));
        fourth0.setOperatingMode(0);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(first);
        operations.add(second0);
        operations.add(third0);
        operations.add(fourth0);

        Series firstSeries = new Series(operations, "15-09-2021 00:00", "13-08-2021 10:00");

        first.setSerialAffiliation(firstSeries);
        second0.setSerialAffiliation(firstSeries);
        third0.setSerialAffiliation(firstSeries);
        fourth0.setSerialAffiliation(firstSeries);

        ArrayList<Series> seriesToWork = new ArrayList<>();
        seriesToWork.add(firstSeries);

        firstRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        secondRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());
        thirdRecourse.fillScheduleUsingPreviousData(firstSeries.getDeadlineForCompletion());

        Main.takeSeriesToWork(seriesToWork);

        assertEquals(new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00").toString(),first.getCWorkingInterval().toString());
        assertEquals(secondRecourse, first.getCNumberOfAssignedRecourse());

        assertEquals(new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00").toString(), second0.getCWorkingInterval().toString());
        assertEquals(firstRecourse, second0.getCNumberOfAssignedRecourse());

        assertEquals(new WorkingHours("14-08-2021 09:00", "14-08-2021 14:00").toString(), third0.getCWorkingInterval().toString());
        assertEquals(thirdRecourse, third0.getCNumberOfAssignedRecourse());
    }
}