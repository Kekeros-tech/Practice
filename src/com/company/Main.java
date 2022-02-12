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

	//Вторая версия будущего фронта работы 2
	public static ArrayList<Operation> choiceFutureFrontOfWork2(ArrayList<Operation> operationsToCreate, Duration selectionInterval){
		ArrayList<Operation> frontOfWork = new ArrayList<>();

		for(Operation operation: operationsToCreate) {
			operation.getLatestEndTimeOfPrevious();
			if(operation.getTactTime() != null && operation.operationNotScheduled()){
				frontOfWork.add(operation);
			}
		}

		LocalDateTime minTime = findMinTactTime(frontOfWork);

		for(int i = 0; i < frontOfWork.size(); i++) {
			Operation currentOperation = frontOfWork.get(i);
			if(currentOperation.getTactTime().isAfter(minTime.plusNanos(selectionInterval.toNanos()))){
				frontOfWork.remove(currentOperation);
				i--;
			}
		}

		return frontOfWork;
	}

	//2
	public static LocalDateTime findMinTactTime(ArrayList<Operation> operationsToSelect){
		LocalDateTime minTime = LocalDateTime.MAX;
		for(Operation operation: operationsToSelect) {
			LocalDateTime tactTimeOfCurrentOperation = operation.getTactTime();
			if(tactTimeOfCurrentOperation != null && tactTimeOfCurrentOperation.isBefore(minTime)){
				minTime = tactTimeOfCurrentOperation;
			}
		}
		return minTime;
	}

	//2
	public static void installOperationsWithFutureFrontOfWork2(ArrayList<Operation> operations, Duration selectionDuration) {
		//ArrayList<Operation> frontOfWork = choiceFutureFrontOfWork2(operations, selectionDuration);

		HashMap<Operation, Recourse> installationSequence = MaximumFlowSolution.solveMaximumFlowProblem(operations);

		if(installationSequence != null){
			for(Map.Entry<Operation, Recourse> entry : installationSequence.entrySet()){
				entry.getKey().installOperationForSpecificResource(entry.getValue());
			}
		}

		for(Operation operation: operations){
			if(operation.operationNotScheduled()){
				operation.setNewTactTime();
			}
		}
	}

	public static void installOperations(ArrayList<Operation> operationsToInstall, Duration selectionDuration, ControlParameters controlParameters){
		while (!isAllOperationsInstall(operationsToInstall)) {

			ArrayList<Operation> frontOfWork = choiceFutureFrontOfWork2(operationsToInstall, selectionDuration);

			sortFrontOfWorkByControlParameters(frontOfWork, controlParameters.sortOperator);

			advancedSorting(frontOfWork, controlParameters.useAdvancedSorting);

			installOperationsWithFutureFrontOfWork2(frontOfWork, selectionDuration);
		}
	}

	//Выбираем операции, которые поместились в "просвет"
	public static ArrayList<Operation> choiceFutureFrontOfWork(ArrayList<Operation> operationsToCreate, Duration skylightWindowSize) {
		ArrayList<Operation> frontOfWork = new ArrayList<>();

		LocalDateTime minTime = minCEarlierStartTime(operationsToCreate);

		for(Operation currentOperation: operationsToCreate) {
			if(currentOperation.getCNumberOfAssignedRecourse() == null && !currentOperation.getCEarlierStartTime().isAfter(minTime.plusNanos(skylightWindowSize.toNanos()))) {
				frontOfWork.add(currentOperation);
			}
		}

		return frontOfWork;
	}

	public static LocalDateTime minCEarlierStartTime(ArrayList<Operation> operations) {
		LocalDateTime minTime = LocalDateTime.MAX;

		for(Operation currentOperation: operations){
			if(currentOperation.getCNumberOfAssignedRecourse() == null && currentOperation.getCEarlierStartTime().isBefore(minTime)){
				minTime = currentOperation.getCEarlierStartTime();
			}
		}
		return minTime;
	}

	public static Operation findOperationWithMinTimeAndMinWindowSize(ArrayList<Operation> operations, LocalDateTime minTime){
		LocalDateTime minLatestTime = LocalDateTime.MAX;
		Operation result = new Operation();
		for(Operation currentOperation: operations) {
			LocalDateTime latestTime = currentOperation.getEarliestStartTime().plusNanos(currentOperation.getDurationOfExecution().toNanos());
			if(currentOperation.getCNumberOfAssignedRecourse() == null &&
					currentOperation.getCEarlierStartTime().isEqual(minTime) && latestTime.isBefore(minLatestTime)) {
				result = currentOperation;
			}
		}
		return result;
	}

	public static void installOperationUsingFutureFrontOfWork(Series currentSeries){
		while(!isAllOperationsInstall(currentSeries.getOperationsToCreate())){
			ArrayList<Operation> frontOfWorkWithFuture = choiceFutureFrontOfWork(currentSeries.getOperationsToCreate(), Duration.ofHours(3));

			while (!isAllOperationsInstall(frontOfWorkWithFuture)) {

				Operation mainOperation = findOperationWithMinTimeAndMinWindowSize(frontOfWorkWithFuture,
						minCEarlierStartTime(currentSeries.getOperationsToCreate()));
				//нужно это завернуть в цикл, а для этого надо вычислять минимальное время

				LocalDateTime lateTime = mainOperation.getCEarlierStartTime().plusNanos(mainOperation.getDurationOfExecution().toNanos());

				ArrayList<Operation> newFrontOfWork = new ArrayList<>();
				for (Operation currentOperation : currentSeries.getOperationsToCreate()) {
					if (currentOperation.getCEarlierStartTime().isBefore(lateTime)) {
						newFrontOfWork.add(currentOperation);
					}
				}

				//тут применятеся сортировка получившегося фронта
				OComparatorBasedOnLateStartTime sorter = new OComparatorBasedOnLateStartTime();
				newFrontOfWork.sort(sorter);

				installOperationsAndReturnFutureDate(newFrontOfWork);
			}

		}
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


		if(installationSequence != null){
			for(Map.Entry<Operation, Recourse> entry : installationSequence.entrySet()){
				entry.getKey().installOperationForSpecificResource(entry.getValue());
			}
		}

		for(Operation operation: frontOfWork){
			if(operation.operationNotScheduled()){
				operation.setNewTactTime();
			}
		}

	}


	public static void installOperationsUntilDeadline(Series currentSeries) {
		//ArrayList<Operation> frontOfWork;

		while (!currentSeries.allOperationsAssigned()) {

			ArrayList <Operation> frontOfWork = choiceFrontOfWork(currentSeries.getOperationsToCreate());

			ArrayList<Operation> frontOfWorkByTime = choiceFrontOfWorkByWithTime(frontOfWork);

			installOperationsAndReturnFutureDate(frontOfWorkByTime);

			frontOfWork.clear();
		}
	}

	public static void installOperationsUntilDeadline2(ArrayList<Operation> operationsToInstall, ControlParameters controlParameters) {
		//ArrayList<Operation> frontOfWork;

		while (!isAllOperationsInstall(operationsToInstall)) {

			ArrayList <Operation> frontOfWork = choiceFrontOfWork(operationsToInstall);

			ArrayList<Operation> frontOfWorkByTime = choiceFrontOfWorkByWithTime(frontOfWork);

			sortFrontOfWorkByControlParameters(frontOfWorkByTime, controlParameters.sortOperator);

			advancedSorting(frontOfWorkByTime, controlParameters.useAdvancedSorting);

			installOperationsAndReturnFutureDate(frontOfWorkByTime);

			frontOfWork.clear();
		}
	}

	public static boolean isAllOperationsInstall(Collection<Operation> operationsToCreate){
		for(Operation currentOperation: operationsToCreate){
			if(currentOperation.operationNotScheduled()){
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
			//installOperationsUntilDeadline2(series.getOperationsToCreate(), controlParameters);
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
			installOperationsUntilDeadline2(series.getOperationsToCreate(), controlParameters);
			/*while(!isAllOperationsInstall(series.getOperationsToCreate()))
			{
				ArrayList<Operation> frontOfWork = choiceFrontOfWork(operationsToInstall);

				ArrayList<Operation> frontOfWorkByTime = choiceFrontOfWorkByWithTime(frontOfWork);

				sortFrontOfWorkByControlParameters(frontOfWorkByTime, controlParameters.sortOperator);

				advancedSorting(frontOfWorkByTime, controlParameters.useAdvancedSorting);

				installOperationsAndReturnFutureDate(frontOfWorkByTime);
			}*/
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
