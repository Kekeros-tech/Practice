package com.company;

import java.nio.file.attribute.GroupPrincipal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

	WorkingHours work = new WorkingHours("14-08-2021 09:00","14-08-2021 13:00");
	WorkingHours work2 = new WorkingHours("15-08-2021 10:00","15-08-2021 18:00");
	WorkingHours work3 = new WorkingHours("17-08-2021 09:00","17-08-2021 13:00");
	WorkingHours work4 = new WorkingHours("18-08-2021 10:00", "18-08-2021 18:00");

	WorkingHours work5 = new WorkingHours("15-08-2021 09:00", "15-08-2021 12:00");
	WorkingHours work6 = new WorkingHours("16-08-2021 09:00", "16-08-2021 15:00");


	ArrayList<WorkingHours> arrayList = new ArrayList<>();
	arrayList.add(work);
	arrayList.add(work2);
	arrayList.add(work3);
	arrayList.add(work4);

	ArrayList<WorkingHours> arrayList1 = new ArrayList<>();
	arrayList1.add(work5);
	arrayList1.add(work6);


	Recourse recourse = new Recourse(arrayList, "13-08-2021 00:00","13-08-2021 00:00");
	Recourse recourse1 = new Recourse(arrayList1,"13-08-2021 00:00", "13-08-2021 00:00");

	ArrayList<Recourse> arrayOfRecourse = new ArrayList<>();
	arrayOfRecourse.add(recourse);
	arrayOfRecourse.add(recourse1);
	Group mygroup = new Group(arrayOfRecourse);

	Operation myoper = new Operation();
	myoper.setResourceGroup(mygroup);
	myoper.setDurationOfExecution(Duration.ofHours(4));

	Operation myoper2 = new Operation();
	myoper2.setResourceGroup(mygroup);
	myoper2.setDurationOfExecution(Duration.ofHours(6));

	ArrayList<Operation> arrayOfOperations = new ArrayList<>();
	arrayOfOperations.add(myoper);
	arrayOfOperations.add(myoper2);
	Series myseries = new Series(arrayOfOperations,"30-08-2021 00:00","13-08-2021 00:00");


	myoper.setSerialAffiliation(myseries);
	myoper2.setSerialAffiliation(myseries);
	myoper2.addFollowingOperation(myoper);

	LocalDateTime currentdate = LocalDateTime.of(2021,8,15,11,00);
	ArrayList<Operation> frontOfWork = new ArrayList<>();

	recourse.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());
	recourse1.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());
	Operation[] buffer;
	if(myseries.getArrivalTime().isBefore(currentdate))
	{
		buffer = myseries.getOperationsToCreate().toArray(new Operation[myseries.getOperationsToCreate().size()]);
		for(Operation currentOperation: buffer)
		{
			if(currentOperation.getPreviousOperations().isEmpty() && currentOperation.hasRecourseForThisDate(currentdate)) {
				frontOfWork.add(currentOperation);
			}
		}
	}
	else{
		//break;
	}

	buffer = frontOfWork.toArray(new Operation[frontOfWork.size()]);
	WHComparatorBasedOnDuration whcomp = new WHComparatorBasedOnDuration();
	recourse.getSchedule().sort(whcomp);
	recourse1.getSchedule().sort(whcomp);

	for(int i = 0; i < buffer.length; i++)
	{
		Recourse[] recoursesBuffer = buffer[i].getResourceGroup().getRecoursesInTheGroup().toArray(new Recourse[buffer[i].getResourceGroup().getRecoursesInTheGroup().size()]);
		for(int j = 0; j < buffer[i].getResourceGroup().getRecoursesInTheGroup().size(); j++) {
			if(recoursesBuffer[j].isWorkingTime(currentdate)) {
				//int workingSize = buffer[i].getResourceGroup().get(j).getSchedule().size();
				WorkingHours[] workingHours = buffer[i].getResourceGroup().get(j).getSchedule().toArray(new WorkingHours[buffer[i].getResourceGroup().get(j).getSchedule().size()]);
				for(int k = 0; k < workingHours.length; k++) {
					//WorkingHours current = buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getSchedule().get(k);
					//Duration duration = Duration.between(workingHours[k].getStartTime(),workingHours[k].getEndTime());
					if(buffer[i].enoughTime(workingHours[k].getStartTime(), workingHours[k].getEndTime())){
						System.out.println("EEE");
					 	System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup());
					 	buffer[i].scheduleAnOperation(j, k);
					 	System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup());
					 	System.out.println(buffer[i].getResourceGroup().get(j).getReleaseTime());
					 	break;
					}
				}
			}
		}
	//System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getReleaseTime());
	}


	//recourse.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());
	//WHComparatorBasedOnDuration whcomp = new WHComparatorBasedOnDuration();
	//recourse.getSchedule().sort(whcomp);
	System.out.println();
	for(int i = 0;i < recourse.getSchedule().size();i++)
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
