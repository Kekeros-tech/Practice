package com.company;

import java.time.Duration;
import java.util.Collection;

public interface IOptimizationAlgo {
    void calculateBestSolution(OperationsArrangementAlgorithmWithCPAndFutureFrontNew algoOfOperationPlacement);
    Duration getBestSolution();
}
