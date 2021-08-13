package com.company;

import java.nio.file.attribute.GroupPrincipal;
import java.text.SimpleDateFormat;
import java.time.Duration;

public class Main {

    public static void main(String[] args) {
	System.out.println("Hello world");
	System.out.println("Here I wrote other line");
	System.out.println("well, it`s work");

	WorkingHours workingHours[] = new WorkingHours[7];
	WorkingHours work = new WorkingHours();
	System.out.println(work.getHours() + " " + work.getMinute());
	//Schedule schedule = new Schedule(workingHours);
	//Recourse recourse = new Recourse(schedule);
	Group group = new Group();
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
	System.out.println(buffer.toMinutes());
    }
}
