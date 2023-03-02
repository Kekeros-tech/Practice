package com.company.results_of_algos;

import com.company.operation.IOperation;
import com.company.recourse.IResource;

public class ResultOfMaximumFlowSolution {
    private IOperation operationForInstalling;
    private IResource resourceForInstalling;
    private int amountForInstalling;

    public ResultOfMaximumFlowSolution(IOperation operationForInstalling, IResource resourceForInstalling, int amountForInstalling) {
        this.operationForInstalling = operationForInstalling;
        this.resourceForInstalling = resourceForInstalling;
        this.amountForInstalling = amountForInstalling;
    }

    public int getAmountForInstalling() {
        return amountForInstalling;
    }

    public IOperation getOperationForInstalling() {
        return operationForInstalling;
    }

    public IResource getResourceForInstalling() {
        return resourceForInstalling;
    }
}
