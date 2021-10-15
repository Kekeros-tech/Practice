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
	public static void installReverseOperationsAndReturnFutureDate (ArrayList<Operation> frontOfWork) {

		for(int i = 0; i < frontOfWork.size(); i++) {

			frontOfWork.get(i).getLatestEndTimeOfFollowing();
			frontOfWork.get(i).installReverseOperation();

		}
	}


	public static ArrayList<Operation> choiceFrontOfWork(ArrayList<Operation> operationsToCreate) {
		ArrayList<Operation> frontOfWork = new ArrayList<>();

		for(int i = 0; i < operationsToCreate.size(); i++) {
			if(operationsToCreate.get(i).allPreviousAssigned() && operationsToCreate.get(i).getCNumberOfAssignedRecourse() == null)
			{
				frontOfWork.add(operationsToCreate.get(i));
			}
		}
		return frontOfWork;
	}



	public static void installOperationsAndReturnFutureDate (ArrayList<Operation> frontOfWork) {

		for(int i = 0; i < frontOfWork.size(); i++) {

			frontOfWork.get(i).getLatestEndTimeOfPrevious();
			frontOfWork.get(i).installOperation();

		}
	}

	public static void installOperationsUntilDeadline(Series currentSeries) {
		ArrayList<Operation> frontOfWork;

		while (!currentSeries.allOperationsAssigned()) {

			frontOfWork = choiceFrontOfWork(currentSeries.getOperationsToCreate());

			installOperationsAndReturnFutureDate(frontOfWork);

			frontOfWork.clear();
		}
	}

	public static boolean isAllOperationsInstall(Collection<Operation> operationsToCreate){
		for(Operation currentOperation: operationsToCreate){
			if(currentOperation.getCNumberOfAssignedRecourse() == null){
				return false;
			}
		}
		return true;
	}

	public static void takeSeriesToWork(Collection<Series> seriesForWork) {
		ArrayList<Operation> operationsToInstall = new ArrayList<>();

		for(Series currentSeries: seriesForWork) {
			operationsToInstall.addAll(currentSeries.getOperationsToCreate());
			installOperationsUntilDeadline(currentSeries);
			currentSeries.clean();

			installReverseOperationsUntilDeadline(currentSeries, currentSeries.getDeadlineForCompletion());
			currentSeries.clean();

		}


		while(!isAllOperationsInstall(operationsToInstall)) {

			ArrayList frontOfWork = choiceFrontOfWork(operationsToInstall);

			OComparatorBasedOnLateStartTime sorter = new OComparatorBasedOnLateStartTime();
			frontOfWork.sort(sorter);

			installOperationsAndReturnFutureDate(frontOfWork);
		}

	}

	public static void installReverseOperationsUntilDeadline(Series currentSeries, LocalDateTime tactDate){
		ArrayList<Operation> frontOfWork;

		while (!currentSeries.allOperationsAssigned()) {

			frontOfWork = choiceReverseFrontOfWork(currentSeries.getOperationsToCreate());

			installReverseOperationsAndReturnFutureDate(frontOfWork);

			frontOfWork.clear();
		}
	}


    public static void main(String[] args) {

    }
}
