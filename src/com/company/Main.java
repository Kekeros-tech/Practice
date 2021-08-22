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
		myoper.setOperatingMode(0);
		Operation myoper2 = new Operation();
		myoper2.setResourceGroup(mygroup);
		myoper2.setDurationOfExecution(Duration.ofHours(6));
		myoper2.setOperatingMode(0);

		//задаем партию
		ArrayList<Operation> arrayOfOperations = new ArrayList<>();
		arrayOfOperations.add(myoper);
		arrayOfOperations.add(myoper2);
		Series myseries = new Series(arrayOfOperations, "30-08-2021 00:00", "13-08-2021 00:00");
		myoper.setSerialAffiliation(myseries);
		myoper2.setSerialAffiliation(myseries);

		myoper2.addFollowingOperation(myoper);

		LocalDateTime tactDate = LocalDateTime.of(2021, 8, 15, 00, 00);
		LocalDateTime previousDate = LocalDateTime.of(2021, 8, 15, 00,00);

		ArrayList<Operation> frontOfWork = new ArrayList<>();

		recourse.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());
		recourse1.fillScheduleUsingPreviousData(myseries.getDeadlineForCompletion());

		ArrayList<Recourse> recoursesAtWork = new ArrayList<>();
		ArrayList<Operation> scheduleOperation = new ArrayList<>();

		//int kol = myseries.getOperationsToCreate().size();

		while (!myseries.getOperationsToCreate().isEmpty()) {
			tactDate = tactDate.plusDays(2);
			for (int i = 0; i < frontOfWork.size(); i++) {
				for (int j = 0; j < recoursesAtWork.size(); j++) {
					if (!recoursesAtWork.get(j).isNotWorkingTime(tactDate)) {
						myseries.removePreviousOperations(frontOfWork.get(i));
					}
				}
			}
			frontOfWork.clear();
			recoursesAtWork.clear();

			if (myseries.getArrivalTime().isBefore(tactDate)) {
				for (Operation currentOperation : myseries.getOperationsToCreate()) {
					if (currentOperation.getPreviousOperations().isEmpty() && currentOperation.hasRecourseForThisDate(tactDate)) {
						frontOfWork.add(currentOperation);
					}
				}
			}
			else break;

			int sizeFrontOfWork = frontOfWork.size();

			for (int i = 0; i < sizeFrontOfWork; i++) {
				Recourse[] recoursesOfCurrentOperation = frontOfWork.get(i).getResourceGroup().getRecoursesInTheGroup().toArray(new Recourse[frontOfWork.get(i).getResourceGroup().getRecoursesInTheGroup().size()]);

				for (int j = 0; j < frontOfWork.get(i).getResourceGroup().getRecoursesInTheGroup().size(); j++) {
					if (!recoursesOfCurrentOperation[j].isNotWorkingTime(tactDate)) {
						WorkingHours[] scheduleOfCurrentRecourse = frontOfWork.get(i).getResourceGroup().get(j).getSchedule().toArray(new WorkingHours[frontOfWork.get(i).getResourceGroup().get(j).getSchedule().size()]);
						for (int k = 0; k < scheduleOfCurrentRecourse.length; k++) {
							//WorkingHours current = buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getSchedule().get(k);
							//Duration duration = Duration.between(scheduleOfCurrentRecourse[k].getStartTime(),scheduleOfCurrentRecourse[k].getEndTime());
							//LocalDateTime previousDate = tactDate.minusHours(4);
							if (frontOfWork.get(i).enoughTime(scheduleOfCurrentRecourse[k].getStartTime(), scheduleOfCurrentRecourse[k].getEndTime()) && recoursesOfCurrentOperation[j].nowFree(scheduleOfCurrentRecourse[k].getStartTime(),scheduleOfCurrentRecourse[k].getEndTime()) && scheduleOfCurrentRecourse[k].getStartTime().isBefore(tactDate) && scheduleOfCurrentRecourse[k].getStartTime().isAfter(previousDate)) {
								//System.out.println("EEE");
								if(frontOfWork.get(i).getOperatingMode() == OperatingMode.canNotBeInterrupted){
									recoursesAtWork.add(frontOfWork.get(i).scheduleAnOperation(j, k));
								}
								else{
									//доработать и разнести по методам
									//идея: разбивать операцию на ту, которую можно обработать и остаточную
									Duration duration = Duration.between(scheduleOfCurrentRecourse[k].getStartTime(),scheduleOfCurrentRecourse[k].getEndTime());
									Operation newOperation = new Operation();
									newOperation.setResourceGroup(frontOfWork.get(i).getResourceGroup());
									newOperation.setDurationOfExecution(frontOfWork.get(i).getDurationOfExecution().minus(duration));
									if(frontOfWork.get(i).getDurationOfExecution().minus(duration).isNegative()){
										frontOfWork.get(i).setOperatingMode(0);
									}
									else{
										Operation newOperation2 = new Operation();
										newOperation2.setResourceGroup(frontOfWork.get(i).getResourceGroup());
										newOperation2.setDurationOfExecution(duration);
										newOperation2.setOperatingMode(0);
										newOperation2.addFollowingOperation(newOperation);
										newOperation2.addFollowingOperation(frontOfWork.get(i).getFollowingOperations());
										newOperation.addFollowingOperation(frontOfWork.get(i).getFollowingOperations());
										newOperation.setOperatingMode(1);

										myseries.getOperationsToCreate().remove(frontOfWork.get(i));
										myseries.addOperationToCreate(newOperation);
										myseries.addOperationToCreate(newOperation2);
										frontOfWork.remove(i);
										frontOfWork.add(newOperation);
										frontOfWork.add(newOperation2);
									}
								}
								//recoursesAtWork.add(frontOfWork.get(i).scheduleAnOperation(j, k));
								//LocalDateTime futureDate = tactDate.plusHours(4);
								//kol--;
								//if(recoursesOfCurrentOperation[j].isWorkingTime(futureDate) && scheduleOfCurrentRecourse[k].getStartTime().isAfter(previousDate) && scheduleOfCurrentRecourse[k].getEndTime().isBefore(futureDate)) //вот это грамотная идея, надо развивать дальше, ведь такты времени одинаковые.
								//{
								//	myseries.removePreviousOperations(frontOfWork.get(i));
								//	kol--;
									//frontOfWork.remove(frontOfWork.get(i));
								//}
								//System.out.println(recoursesAtWork);
								//System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup());
								//buffer[i].scheduleAnOperation(j, k);
								//scheduleOperation.add(buffer[i]);
								//System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup());
								System.out.println(frontOfWork.get(i).getResourceGroup().get(j).getReleaseTime());
								break;
							}
						}
					}
					if (frontOfWork.get(i).getDurationOfExecution().toNanos() == 0 || frontOfWork.get(i).getOperatingMode() == OperatingMode.canBeInterrupted) {
						break;
					}
				}
			//System.out.println(buffer[i].getResourceGroup().getRecoursesInTheGroup().get(j).getReleaseTime());
			}
		myseries.getOperationsToCreate().removeAll(frontOfWork);
		previousDate = tactDate;
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
