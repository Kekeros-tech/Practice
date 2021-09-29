package com.company;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class RecourseTest {
    Recourse currentRecourse = new Recourse();


    @Before
    public void setUp() throws Exception {
        WorkingHours work1 = new WorkingHours("14-08-2021 09:00", "14-08-2021 13:00");
        WorkingHours work2 = new WorkingHours("15-08-2021 09:00", "15-08-2021 13:00");
        ArrayList<WorkingHours> arrayList = new ArrayList<>();
        arrayList.add(work1);
        arrayList.add(work2);
        currentRecourse.setSchedule(arrayList);
        currentRecourse.setReleaseTime("13-08-2021 15:00");
        //Recourse currentRecourse = new Recourse(arrayList, "13-08-2021 15:00");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void fillScheduleUsingPreviousData() {
    }

    @Test
    public void takeRecourse() {
        LocalDateTime tackDate = LocalDateTime.of(2021,8,14,9,00);
        Duration resultDuration = currentRecourse.takeRecourse(Duration.ofHours(4),0, tackDate);
        assertEquals(Duration.ZERO, resultDuration);
    }

    @Test
    public void isFree() {
        LocalDateTime tackDateBefore = LocalDateTime.of(2021,8,12,20,00);
        LocalDateTime tackDateAfter = LocalDateTime.of(2021,8,15,20,00);
        LocalDateTime tackDateEqual = LocalDateTime.of(2021,8,13,15,00);
        assertEquals(currentRecourse.isFree(tackDateBefore), false);
        assertEquals(currentRecourse.isFree(tackDateAfter), true);
        assertEquals(currentRecourse.isFree(tackDateEqual),true);
    }

    @Test
    public void takeWhichCanBeInterrupted() {
    }

    @Test
    public void tackWhichCanNotBeInterrupted() {
    }

    @Test
    public void getStartDateAfterReleaseDate() {
        WorkingHours work0 = new WorkingHours("14-08-2021 09:00", "14-08-2021 15:00");
        WorkingHours work1 = new WorkingHours("15-08-2021 09:00", "15-08-2021 15:00");
        ArrayList<WorkingHours> schedule = new ArrayList<>();
        schedule.add(work0);
        schedule.add(work1);
        Recourse recourse = new Recourse(schedule,"16-08-2021 06:00");
        LocalDateTime time = LocalDateTime.of(2021,8,20,12,00);
        LocalDateTime tackDate = LocalDateTime.of(2021, 8, 12, 12,00);
        assertEquals(recourse.getStartDateAfterReleaseDate(tackDate), null);
        recourse.fillScheduleUsingPreviousData(time);
        assertEquals(recourse.getStartDateAfterReleaseDate(tackDate), LocalDateTime.of(2021, 8,16,9,00));

    }
}