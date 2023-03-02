package com.company;

import java.time.Duration;
import java.util.*;

public class Main {
	public static void takeSeriesToWork(Collection<Series> seriesForWork) {

		OperationsArrangementAlgorithm algo = new OperationsArrangementAlgorithm(seriesForWork);

		algo.takeSeriesToWork();
	}

	public static void testFutureAlgo(Collection<Series> seriesForWork,Duration selectionDuration, ControlParameters controlParameters) {

		OperationsArrangementAlgorithm algo = new OperationsArrangementAlgorithmWithCPAndFutureFrontNew(seriesForWork, controlParameters, selectionDuration);

		algo.takeSeriesToWork();
	}

	public static void testAlgo(Collection<Series> seriesForWork, ControlParameters controlParameters) {

		OperationsArrangementAlgorithm algo = new OperationsArrangementAlgorithmWithCPNew(seriesForWork, controlParameters);

		algo.takeSeriesToWork();
	}

    public static void main(String[] args) {
	}
}
