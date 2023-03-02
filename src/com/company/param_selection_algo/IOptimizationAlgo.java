package com.company.param_selection_algo;

import com.company.arrangement_algo.Algo_WithCPAndFuture;

import java.time.Duration;

public interface IOptimizationAlgo {
    void calculateBestSolution(Algo_WithCPAndFuture algoOfOperationPlacement);
    Duration getBestSolution();
}
