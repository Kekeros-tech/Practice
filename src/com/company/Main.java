package com.company;

import com.company.arrangement_algo.Algo_Basic;
import com.company.arrangement_algo.Algo_WithCPAndFuture;
import com.company.arrangement_algo.Algo_WithCP;
import com.company.control_param.ControlParameters;

import java.time.Duration;
import java.util.*;

public class Main {
	public static void takeSeriesToWork(Collection<Series> seriesForWork) {

		Algo_Basic algo = new Algo_Basic(seriesForWork);

		algo.takeSeriesToWork();
	}

	public static void testFutureAlgo(Collection<Series> seriesForWork,Duration selectionDuration, ControlParameters controlParameters) {

		Algo_Basic algo = new Algo_WithCPAndFuture(seriesForWork, controlParameters, selectionDuration);

		algo.takeSeriesToWork();
	}

	public static void testAlgo(Collection<Series> seriesForWork, ControlParameters controlParameters) {

		Algo_Basic algo = new Algo_WithCP(seriesForWork, controlParameters);

		algo.takeSeriesToWork();
	}

    public static void main(String[] args) {
	}
}
