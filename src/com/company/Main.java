package com.company;

import java.nio.file.attribute.GroupPrincipal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
		//рабочий график 2 через 2
		WorkingHours work = new WorkingHours("14-08-2021 09:00", "14-08-2021 13:00");
		WorkingHours work2 = new WorkingHours("15-08-2021 10:00", "15-08-2021 16:00");
		WorkingHours work3 = new WorkingHours("15-08-2021 17:00", "15-08-2021 23:00");
		WorkingHours work4 = new WorkingHours("18-08-2021 10:00", "18-08-2021 18:00");
		//рабочий график каждый день
		WorkingHours work5 = new WorkingHours("15-08-2021 09:00", "15-08-2021 14:00");
		WorkingHours work6 = new WorkingHours("16-08-2021 09:00", "16-08-2021 15:00");


		ArrayList<WorkingHours> arrayList = new ArrayList<>();
		arrayList.add(work);
		arrayList.add(work2);
		arrayList.add(work3);
		arrayList.add(work4);

		ArrayList<WorkingHours> arrayList1 = new ArrayList<>();
		arrayList1.add(work5);
		arrayList1.add(work6);

		//задаем 2 ресурса. 1 - график работы 2 через 2. 2 - работает каждый день
		Recourse recourse = new Recourse(arrayList, "13-08-2021 00:00");
		Recourse recourse1 = new Recourse(arrayList1, "13-08-2021 00:00");

		//задаем группу ресурсов
		ArrayList<Recourse> arrayOfRecourse = new ArrayList<>();
		arrayOfRecourse.add(recourse);
		arrayOfRecourse.add(recourse1);
		Group mygroup = new Group(arrayOfRecourse);

		//задаем 2 операции
		Operation myoper = new Operation();
		myoper.setResourceGroup(mygroup);
		myoper.setDurationOfExecution(Duration.ofHours(2));
		myoper.setOperatingMode(0);
		Operation myoper2 = new Operation();
		myoper2.setResourceGroup(mygroup);
		myoper2.setDurationOfExecution(Duration.ofHours(10));
		myoper2.setOperatingMode(1);

		//задаем партию
		ArrayList<Operation> arrayOfOperations = new ArrayList<>();
		arrayOfOperations.add(myoper);
		arrayOfOperations.add(myoper2);
		Series myseries = new Series(arrayOfOperations, "30-08-2021 00:00", "13-08-2021 00:00");
		myoper.setSerialAffiliation(myseries);
		myoper2.setSerialAffiliation(myseries);

		myoper2.addFollowingOperation(myoper);

		LocalDateTime tactDate = LocalDateTime.of(2021, 8, 15, 10, 00);
		LocalDateTime previousDate = LocalDateTime.of(2021, 8, 15, 00,00);

		ArrayList<Operation> frontOfWork = new ArrayList<>();

		recourse.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());
		recourse1.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());

		//int kol = myseries.getOperationsToCreate().size();

		while (!myseries.allOperationsAssigned()) {

			//формируем фронт работы
			for(int i = 0; i < myseries.getOperationsToCreate().size(); i++){
				if(myseries.getOperationsToCreate().get(i).allPreviousAssigned() && myseries.getOperationsToCreate().get(i).hasRecourseForThisDate(tactDate) != null && myseries.getOperationsToCreate().get(i).getCNumberOfAssignedRecourse() == null)
				{
					frontOfWork.add(myseries.getOperationsToCreate().get(i));
				}
			}

			int iteration = 0;
			for(int i = 0; i < frontOfWork.size(); i++) {
				Recourse assignedRecourse = frontOfWork.get(i).hasRecourseForThisDate(tactDate);
				frontOfWork.get(i).installAnOperation(assignedRecourse, tactDate);
				if(frontOfWork.get(i).getDurationOfExecution().toNanos() >= frontOfWork.get(iteration).getDurationOfExecution().toNanos()) {
					iteration = i;
				}
				System.out.println(frontOfWork.get(i).getCWorkingInterval());
			}
			if(frontOfWork.isEmpty())
			{
				tactDate = tactDate.plusHours(12);
				//tactDate = tactDate.plusMinutes(1);
			}
			else{
				tactDate = frontOfWork.get(iteration).getCWorkingInterval().getEndTime();
			}
			System.out.println(tactDate);
			//tactDate = frontOfWork.get(iteration).getCWorkingInterval().getEndTime();
			frontOfWork.clear();
		}
		System.out.println("График работы 1 станка");
		for(int i = 0;i < recourse.getSchedule().size();i++)
		{
			System.out.println(recourse.getSchedule().get(i).getStartTime() + " " +recourse.getSchedule().get(i).getEndTime());
			//System.out.println(" "+ recourse.getSchedule().get(i).getEndTime());
		}
		System.out.println("График работы 2 станка");
		for(int i = 0;i < recourse1.getSchedule().size();i++)
		{
			System.out.println(recourse1.getSchedule().get(i).getStartTime() + " " +recourse1.getSchedule().get(i).getEndTime());
			//System.out.println(" "+ recourse.getSchedule().get(i).getEndTime());
		}
    }
}
