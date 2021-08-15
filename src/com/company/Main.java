package com.company;

import java.nio.file.attribute.GroupPrincipal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	WorkingHours work = new WorkingHours("14-08-2021 09:00","14-08-2021 13:00");
	WorkingHours work2 = new WorkingHours("14-08-2021 14:00","14-08-2021 19:00");
	WorkingHours work3 = new WorkingHours("15-08-2021 10:00","15-08-2021 18:00");
		ArrayList<WorkingHours> arrayList = new ArrayList<>();
		arrayList.add(work);
		arrayList.add(work2);
		arrayList.add(work3);
	Recourse recourse = new Recourse(arrayList, "13-08-2021 19:00");
	ArrayList<Recourse> arrayOfRecourse = new ArrayList<>();
	arrayOfRecourse.add(recourse);
	Group mygroup = new Group(arrayOfRecourse);
	Operation myoper = new Operation();
	myoper.setResourceGroup(mygroup);
	myoper.setDurationOfExecution(Duration.ofHours(4));
	Operation myoper2 = new Operation();
	myoper2.setResourceGroup(mygroup);
	myoper2.setDurationOfExecution(Duration.ofHours(8));
	ArrayList<Operation> arrayOfOperations = new ArrayList<>();
	arrayOfOperations.add(myoper);
	arrayOfOperations.add(myoper2);
	Series myseries = new Series(arrayOfOperations,"20-08-2021 00:00","13-08-2021 00:00");


	myoper.setSerialAffiliation(myseries);
	myoper2.setSerialAffiliation(myseries);
	myoper2.addFollowingOperation(myoper);
	//myoper.addPreviousOperation(myoper2);
	LocalDateTime currentdate = LocalDateTime.of(2021,8,13,20,00);
	ArrayList<Operation> frontOfWork = new ArrayList<>();

	Operation[] buffer = myseries.getOperationsToCreate().toArray(new Operation[myseries.getOperationsToCreate().size()]);
	for(int i=0; i<buffer.length; i++)
	{
		if(buffer[i].getPreviousOperations().isEmpty() && buffer[i].getSerialAffiliation().getArrivalTime().isBefore(currentdate)){
			frontOfWork.add(buffer[i]);
		}
	}
	buffer = frontOfWork.toArray(new Operation[frontOfWork.size()]);
	recourse.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());
	WHComparatorBasedOnDuration whcomp = new WHComparatorBasedOnDuration();
	recourse.getSchedule().sort(whcomp);
	for(int i = 0; i < buffer.length; i++)
	{
		for(int j=0;j < buffer[i].getResourceGroup().getRecoursesInTheGroup().size();j++){
			if(buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getReleaseTime().isBefore(currentdate)){
				for(int k=0;k < buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getSchedule().size();k++){
					 WorkingHours current = buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getSchedule().get(k);
					 Duration duration = Duration.between(current.getStartTime(),current.getEndTime());
					 if(duration.toMillis()>=buffer[i].getDurationOfExecution().toMillis()){
					 	System.out.println("EEE");
						 buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).takeRecourse(buffer[i]);
						 System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getReleaseTime());
					 	break;
						// buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).takeRecourse(buffer[i]);
					 }
				}
			}
		}
		//System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getReleaseTime());
	}


	//recourse.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());
	//WHComparatorBasedOnDuration whcomp = new WHComparatorBasedOnDuration();
	recourse.getSchedule().sort(whcomp);
	System.out.println();
	for(int i = 0;i<recourse.getSchedule().size();i++)
	{
		System.out.println(recourse.getSchedule().get(i).getStartTime() + " " +recourse.getSchedule().get(i).getEndTime());
		//System.out.println(" "+ recourse.getSchedule().get(i).getEndTime());
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
