package com.company;

import java.nio.file.attribute.GroupPrincipal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	System.out.println("Hello world");
	System.out.println("Here I wrote other line");
	System.out.println("well, it`s work");

	//WorkingHours workingHours[] = new WorkingHours[7];
	//WorkingHours work = new WorkingHours();
		LocalDateTime firstStart = LocalDateTime.of(2021,8,14,10,00);
		LocalDateTime firstEnd = LocalDateTime.of(2021,8,14,18,00);
		LocalDateTime secondStart = LocalDateTime.of(2021,8,14,19,00);
		LocalDateTime secondEnd = LocalDateTime.of(2021,8,14,21,00);
		LocalDateTime thirdStart = LocalDateTime.of(2021,8,15,22,00);
		LocalDateTime thirdEnd = LocalDateTime.of(2021,8,15,23,00);
		//secondEnd = secondEnd.plusNanos(Duration.between(firstStart,firstEnd).toNanos());
		//System.out.println(secondEnd);
	WorkingHours work = new WorkingHours(firstStart,firstEnd);
	WorkingHours work2 = new WorkingHours(secondStart,secondEnd);
	WorkingHours work3 = new WorkingHours(thirdStart,thirdEnd);
		ArrayList<WorkingHours> arrayList = new ArrayList<>();
		arrayList.add(work);
		arrayList.add(work2);
		arrayList.add(work3);
	Recourse recourse = new Recourse(arrayList);
	LocalDateTime future = LocalDateTime.of(2021,8,20,00,00);
	recourse.fillScheduleUsingPreviousData(future);
	for(int i = 0;i<recourse.getSchedule().size();i++)
	{
		System.out.println(recourse.getSchedule().get(i).getStartTime());
		System.out.println(recourse.getSchedule().get(i).getEndTime());
		System.out.println();
	}
	//System.out.println(work.getHours() + " " + work.getMinute());
	//Schedule schedule = new Schedule(workingHours);
	//Recourse recourse = new Recourse(schedule);
	/*Group group = new Group();
	//group.addRecourseInTheGroup(recourse);
	Operation operation = new Operation();
	operation.setResourceGroup(group);
	//System.out.println(operation.getResourceGroup().getRecoursesInTheGroup().get(0).getReleaseTime());
	System.out.println(operation.getOperatingMode());
	operation.setOperatingMode(0);
	System.out.println(operation.getOperatingMode());
	//operation.setDurationOfExecution(new WorkingHours(15,20));
	//System.out.println(operation.getDurationOfExecution().getHours() +" "+operation.getDurationOfExecution().getMinute());
	//recourse.takeToRecourse(operation);
	//System.out.println(recourse.getReleaseTime());
	Date myDate = new Date();
	Date newDate = new Date(30,9,2001,00,00);
	System.out.println(myDate.toString());
	System.out.println(newDate.toString());
	WorkingHours simple;
	//simple = myDate.getWorkingHoursBetweenToDates(newDate);
	//System.out.println(simple.getHours()+" часов "+simple.getMinute()+" минут");
	Duration buffer = myDate.getDurationBetweenToDates(newDate);
		//long result = myDate.subtractingDate(newDate);
		//java.util.Date someDate = new java.util.Date(result);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	System.out.println(buffer.toMinutes());*/
    }
}
