package com.company;

import javax.security.auth.login.Configuration;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Main {


	//Reverse
	public static ArrayList<Operation> choiceReverseFrontOfWork(ArrayList<Operation> operationsToCreate) {
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

			//это надо будет вынести в отдельный блок
			if(frontOfWork.get(i) instanceof OperationWithPrioritiesByHeirs) {
				((OperationWithPrioritiesByHeirs) frontOfWork.get(i)).setPrioritiesByHeirs();
			}

			//это тоже
			if(frontOfWork.get(i) instanceof OperationWithPrioritiesByDuration) {
				((OperationWithPrioritiesByDuration) frontOfWork.get(i)).setPrioritiesByDuration();
			}

			frontOfWork.get(i).getLatestEndTimeOfFollowing();
			frontOfWork.get(i).installReverseOperation();
		}
	}


	public static ArrayList<Operation> choiceFrontOfWork(ArrayList<Operation> operationsToCreate) {
		ArrayList<Operation> frontOfWork = new ArrayList<>();

		for (Operation currentOperation: operationsToCreate) {
			if(currentOperation.getCNumberOfAssignedRecourse() == null && currentOperation.allPreviousAssigned())
			{
				frontOfWork.add(currentOperation);
			}
		}

		return frontOfWork;
	}

	public static ArrayList<Operation> choiceFrontOfWorkByWithTime(ArrayList<Operation> frontOfWorkByPrevious) {
		ArrayList<Operation> frontOfWorkByTime = new ArrayList<>();
		LocalDateTime minTime = LocalDateTime.MAX;

		for(Operation currentOperation: frontOfWorkByPrevious) {
			currentOperation.getLatestEndTimeOfPrevious();
			if(currentOperation.getTactTime().isBefore(minTime)) {
				minTime = currentOperation.getTactTime();
			}
		}

		for (Operation currentOperation: frontOfWorkByPrevious) {
			if(!currentOperation.getTactTime().isAfter(minTime)) {
				frontOfWorkByTime.add(currentOperation);
			}
		}

		return  frontOfWorkByTime;
	}

	public static void installOperationsAndReturnFutureDate (ArrayList<Operation> frontOfWork) {

		for(int i = 0; i < frontOfWork.size(); i++) {
			frontOfWork.get(i).getLatestEndTimeOfPrevious();
		}

		HashMap<Operation, Recourse> installationSequence = MaximumFlowSolution.solveMaximumFlowProblem(frontOfWork);

		if(installationSequence == null) {
			for(int i = 0; i < frontOfWork.size(); i++) {
				frontOfWork.get(i).setNewTactTime();
			}
		}
		else
		{
			for(Map.Entry<Operation, Recourse> entry : installationSequence.entrySet()){
				entry.getKey().installOperationForSpecificResource(entry.getValue());
			}
		}
	}


	public static void installOperationsUntilDeadline(Series currentSeries) {
		//ArrayList<Operation> frontOfWork;

		while (!currentSeries.allOperationsAssigned()) {

			ArrayList <Operation> frontOfWork = choiceFrontOfWork(currentSeries.getOperationsToCreate());

			//ArrayList<Operation> frontOfWorkByTime = choiceFrontOfWorkByWithTime(frontOfWork);

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

			installReverseOperationsUntilDeadline(currentSeries);
			currentSeries.clean();
		}

		while(!isAllOperationsInstall(operationsToInstall)) {

			ArrayList frontOfWork = choiceFrontOfWork(operationsToInstall);

			ArrayList frontOfWorkByTime = choiceFrontOfWorkByWithTime(frontOfWork);

			installOperationsAndReturnFutureDate(frontOfWorkByTime);
		}
	}

	public static void takeSeriesToWorkExtended(Collection<Series> seriesForWork, ControlParameters controlParameters) {
		ArrayList<Operation> operationsToInstall = new ArrayList<>();

/*		switch (controlParameters.sortOperator) {
			case sortByPrioritiesByHeirs:
				for(Series series: seriesForWork) {
					//ArrayList<Operation> newOperation = new ArrayList<>();
					for(Operation operation: series.getOperationsToCreate()){
						//Operation buffer = new OperationWithPrioritiesByHeirs(operation);
						//newOperation.add(buffer);
						operation = new OperationWithPrioritiesByHeirs(operation);
					}
					//series.setOperationsToCreate(newOperation);
				}
				break;
			case sortByEarlyAndLateStartDates:
				break;
		}*/

		for(Series series: seriesForWork) {
			installOperationsUntilDeadline(series);
			series.clean();

			installReverseOperationsUntilDeadline(series);
			series.clean();
		}

		if(controlParameters.sequenceOfOperations == SequenceOfOperations.together) {
			for(Series series : seriesForWork) {
				operationsToInstall.addAll(series.getOperationsToCreate());
			}
			Series newCompleteSeries = new Series(operationsToInstall, findEarliestArrivalTime(seriesForWork), findLatestDeadlineTime(seriesForWork));
			seriesForWork.clear();
			seriesForWork.add(newCompleteSeries);
		}

		for(Series series: seriesForWork) {
			while(!isAllOperationsInstall(series.getOperationsToCreate()))
			{
				ArrayList<Operation> frontOfWork = choiceFrontOfWork(operationsToInstall);

				ArrayList<Operation> frontOfWorkByTime = choiceFrontOfWorkByWithTime(frontOfWork);

				sortFrontOfWorkByControlParameters(frontOfWorkByTime, controlParameters.sortOperator);

				advancedSorting(frontOfWorkByTime, controlParameters.useAdvancedSorting);

				installOperationsAndReturnFutureDate(frontOfWorkByTime);
			}
		}
	}

	public static void sortFrontOfWorkByControlParameters(ArrayList<Operation> frontOfWork, SortOperator sortOperator){
		switch (sortOperator) {
			case sortByPrioritiesByHeirs:
				OComparatorBasedOnPrioritiesByHeirs sorter = new OComparatorBasedOnPrioritiesByHeirs();
				frontOfWork.sort(sorter);
				break;
			case sortByPrioritiesByDuration:
				OComparatorBasedOnPrioritiesByDuration sorter1 = new OComparatorBasedOnPrioritiesByDuration();
				frontOfWork.sort(sorter1);
				break;
			case sortByEarlyAndLateStartDates:
				OComparatorBasedOnLateStartTime sorter2 = new OComparatorBasedOnLateStartTime();
				frontOfWork.sort(sorter2);
				break;
		}
	}

	public static void advancedSorting(ArrayList<Operation> frontOfWork, UseAdvancedSorting sorting) {
		switch (sorting) {
			case doNotUse:
				break;
			case use:
				OExtendedComparator sorter = new OExtendedComparator();
				frontOfWork.sort(sorter);
				break;
		}
	}

	public static LocalDateTime findEarliestArrivalTime(Collection<Series> seriesForWork){
		LocalDateTime earliestTime = LocalDateTime.MAX;
		for(Series series: seriesForWork){
			if(series.getArrivalTime().isBefore(earliestTime)){
				earliestTime = series.getArrivalTime();
			}
		}
		return earliestTime;
	}

	public static LocalDateTime findLatestDeadlineTime(Collection<Series> seriesForWork) {
		LocalDateTime latestTime = LocalDateTime.MAX;
		for(Series series: seriesForWork){
			if(series.getArrivalTime().isAfter(latestTime)){
				latestTime = series.getArrivalTime();
			}
		}
		return latestTime;
	}


	public static void installReverseOperationsUntilDeadline(Series currentSeries){
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
