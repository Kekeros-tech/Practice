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
		WorkingHours work2 = new WorkingHours("15-08-2021 10:00", "15-08-2021 18:00");
		WorkingHours work3 = new WorkingHours("17-08-2021 09:00", "17-08-2021 13:00");
		WorkingHours work4 = new WorkingHours("18-08-2021 10:00", "18-08-2021 18:00");
		//рабочий график каждый день
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

		//задаем 2 ресурса. 1 - график работы 2 через 2. 2 - работает каждый день
		Recourse recourse = new Recourse(arrayList, "13-08-2021 00:00", "13-08-2021 00:00");
		Recourse recourse1 = new Recourse(arrayList1, "13-08-2021 00:00", "13-08-2021 00:00");

		//задаем группу ресурсов
		ArrayList<Recourse> arrayOfRecourse = new ArrayList<>();
		arrayOfRecourse.add(recourse);
		arrayOfRecourse.add(recourse1);
		Group mygroup = new Group(arrayOfRecourse);

		//задаем 2 операции
		Operation myoper = new Operation();
		myoper.setResourceGroup(mygroup);
		myoper.setDurationOfExecution(Duration.ofHours(4));
		Operation myoper2 = new Operation();
		myoper2.setResourceGroup(mygroup);
		myoper2.setDurationOfExecution(Duration.ofHours(6));

		//задаем партию
		ArrayList<Operation> arrayOfOperations = new ArrayList<>();
		arrayOfOperations.add(myoper);
		arrayOfOperations.add(myoper2);
		Series myseries = new Series(arrayOfOperations, "30-08-2021 00:00", "13-08-2021 00:00");
		myoper.setSerialAffiliation(myseries);
		myoper2.setSerialAffiliation(myseries);

		myoper2.addFollowingOperation(myoper);

		LocalDateTime currentdate = LocalDateTime.of(2021, 8, 6, 00, 00);
		LocalDateTime previousDate = LocalDateTime.of(2021, 8, 6, 00,00);
		ArrayList<Operation> frontOfWork = new ArrayList<>();

		recourse.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());
		recourse1.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());
		Operation[] buffer;
		ArrayList<Recourse> recoursesAtWork = new ArrayList<>();
		ArrayList<Operation> scheduleOperation = new ArrayList<>();

		while (!myseries.getOperationsToCreate().isEmpty()) {
			currentdate = currentdate.plusDays(10);
			//ArrayList<Operation> frontOfWork = new ArrayList<>();
			for (int i = 0; i < frontOfWork.size(); i++) {
				for (int j = 0; j < recoursesAtWork.size(); j++) {
					if (recoursesAtWork.get(j).isWorkingTime(currentdate)) {
						myseries.removePreviousOperations(frontOfWork.get(i));
					}
				}
			}
			frontOfWork.clear();
			recoursesAtWork.clear();
			if (myseries.getArrivalTime().isBefore(currentdate)) {
				buffer = myseries.getOperationsToCreate().toArray(new Operation[myseries.getOperationsToCreate().size()]);
				for (Operation currentOperation : buffer) {
					if (currentOperation.getPreviousOperations().isEmpty() && currentOperation.hasRecourseForThisDate(currentdate)) {
						frontOfWork.add(currentOperation);
					}
				}
			}
			else {
				break;
			}

			buffer = frontOfWork.toArray(new Operation[frontOfWork.size()]);
			WHComparatorBasedOnDuration whcomp = new WHComparatorBasedOnDuration();
			//recourse.getSchedule().sort(whcomp);
			//recourse1.getSchedule().sort(whcomp);

			for (int i = 0; i < buffer.length; i++) {
				Recourse[] recoursesBuffer = buffer[i].getResourceGroup().getRecoursesInTheGroup().toArray(new Recourse[buffer[i].getResourceGroup().getRecoursesInTheGroup().size()]);
				for (int j = 0; j < buffer[i].getResourceGroup().getRecoursesInTheGroup().size(); j++) {
					if (recoursesBuffer[j].isWorkingTime(currentdate) && buffer[i].getDurationOfExecution().toNanos() != 0) {
						//int workingSize = buffer[i].getResourceGroup().get(j).getSchedule().size();
						WorkingHours[] workingHours = buffer[i].getResourceGroup().get(j).getSchedule().toArray(new WorkingHours[buffer[i].getResourceGroup().get(j).getSchedule().size()]);
						for (int k = 0; k < workingHours.length; k++) {
							//WorkingHours current = buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getSchedule().get(k);
							//Duration duration = Duration.between(workingHours[k].getStartTime(),workingHours[k].getEndTime());
							if (buffer[i].enoughTime(workingHours[k].getStartTime(), workingHours[k].getEndTime()) && workingHours[k].getStartTime().isBefore(currentdate) && workingHours[k].getEndTime().isAfter(previousDate)) {
								System.out.println("EEE");
								recoursesAtWork.add(buffer[i].scheduleAnOperation(j, k));
								System.out.println(recoursesAtWork);
								//System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup());
								//buffer[i].scheduleAnOperation(j, k);
								//scheduleOperation.add(buffer[i]);
								//System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup());
								System.out.println(buffer[i].getResourceGroup().get(j).getReleaseTime());
								break;
							}
						}
					}
					if (buffer[i].getDurationOfExecution().toNanos() == 0 || buffer[i].getOperatingMode() == OperatingMode.canBeInterrupted) {
						break;
					}
				}
			//System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getReleaseTime());
			}

		myseries.getOperationsToCreate().removeAll(frontOfWork);
		previousDate = currentdate;
		//frontOfWork.clear();
		}
		WHComparatorBasedOnDate whcompDate = new WHComparatorBasedOnDate();
		recourse.getSchedule().sort(whcompDate);
		recourse1.getSchedule().sort(whcompDate);

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
