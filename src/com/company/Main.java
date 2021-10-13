package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Main {

	//Reverse
	public static ArrayList<Operation> choiceReverseFrontOfWork( ArrayList<Operation> operationsToCreate) {
		ArrayList<Operation> frontOfWork = new ArrayList<>();
		for(int i = 0; i < operationsToCreate.size(); i++) {
			if(operationsToCreate.get(i).allPreviousAssignedReverse() && operationsToCreate.get(i).allFollowingAssignedReverse() && operationsToCreate.get(i).getCLateStartTime() == null ) {
				frontOfWork.add(operationsToCreate.get(i));
			}
		}
		return frontOfWork;
	}

	//Reverse
	public static LocalDateTime installReverseOperationsAndReturnFutureDate (ArrayList<Operation> frontOfWork, LocalDateTime tactDate) {
		LocalDateTime futureDate = LocalDateTime.MIN;

		for(int i = 0; i < frontOfWork.size(); i++) {

			futureDate = frontOfWork.get(i).installReverseOperation();

			System.out.println(frontOfWork.get(i).getCLateStartTime());
		}
		return futureDate;
	}


	public static ArrayList<Operation> choiceFrontOfWork(ArrayList<Operation> operationsToCreate) {
		ArrayList<Operation> frontOfWork = new ArrayList<>();

		for(int i = 0; i < operationsToCreate.size(); i++) {
			if(operationsToCreate.get(i).getCNumberOfAssignedRecourse() == null && operationsToCreate.get(i).allPreviousAssigned())
			{
				frontOfWork.add(operationsToCreate.get(i));
			}
		}
		return frontOfWork;
	}



	public static LocalDateTime installOperationsAndReturnFutureDate (ArrayList<Operation> frontOfWork, LocalDateTime tactDate) {
		LocalDateTime currentDate = LocalDateTime.MAX;

		for(int i = 0; i < frontOfWork.size(); i++) {

			LocalDateTime futureDate = frontOfWork.get(i).installOperation(tactDate);

			if(futureDate.isBefore(currentDate)) {
				currentDate = futureDate;
			}

			System.out.println(frontOfWork.get(i).getCWorkingInterval());
		}
		return currentDate;
	}

	public static void installOperationsUntilDeadline(Series currentSeries, LocalDateTime tactDate) {
		ArrayList<Operation> frontOfWork;
		LocalDateTime futureDate;

		while (!currentSeries.allOperationsAssigned()) {

			frontOfWork = choiceFrontOfWork(currentSeries.getOperationsToCreate());

			futureDate = installOperationsAndReturnFutureDate(frontOfWork, tactDate);

			tactDate = futureDate;

			frontOfWork.clear();
		}
	}

	public static void takeSeriesToWork(Collection<Series> seriesForWork, LocalDateTime tactDate) {
		ArrayList<Operation> operationsToInstall = new ArrayList<>();

		//Temporarily
		LocalDateTime maxDeadline = LocalDateTime.MIN;

		for(Series currentSeries: seriesForWork) {

			operationsToInstall.addAll(currentSeries.getOperationsToCreate());

			installOperationsUntilDeadline(currentSeries, tactDate);

			currentSeries.setСNumberOfAssignedOperations(0);
			installReverseOperationsUntilDeadline(currentSeries, currentSeries.getDeadlineForCompletion());
			currentSeries.clean();

			//5
			if(currentSeries.getDeadlineForCompletion().isAfter(maxDeadline)){
				maxDeadline = currentSeries.getDeadlineForCompletion();
			}
		}

		while(tactDate.isBefore(maxDeadline)) {
			ArrayList frontOfWork = choiceFrontOfWork(operationsToInstall);
			OComparatorBasedOnLateStartTime sorter = new OComparatorBasedOnLateStartTime();
			frontOfWork.sort(sorter);

			LocalDateTime futureDate = installOperationsAndReturnFutureDate(frontOfWork, tactDate);

			tactDate = futureDate;
		}
	}

	public static void installReverseOperationsUntilDeadline(Series currentSeries, LocalDateTime tactDate){

		while (!currentSeries.allOperationsAssigned()) {

			 ArrayList<Operation> frontOfWork = choiceReverseFrontOfWork(currentSeries.getOperationsToCreate());

			installReverseOperationsAndReturnFutureDate(frontOfWork, tactDate);
		}
	}


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
		Series mySeries = new Series(arrayOfOperations, "30-08-2021 00:00", "13-08-2021 00:00");
		myoper.setSerialAffiliation(mySeries);
		myoper2.setSerialAffiliation(mySeries);

		myoper2.addFollowingOperation(myoper);

		LocalDateTime tactDate = LocalDateTime.of(2021, 8, 15, 10, 00);
		LocalDateTime futureDate;

		ArrayList<Operation> frontOfWork = new ArrayList<>();

		recourse.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());
		recourse1.fillScheduleUsingPreviousData(mySeries.getDeadlineForCompletion());

		installOperationsUntilDeadline(mySeries, tactDate);

    }
}
