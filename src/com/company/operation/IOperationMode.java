package com.company.operation;

import com.company.recourse.IResource;
import com.company.recourse.IStructuralUnitOfResource;
import com.company.results_of_algos.ResultOfOperationSetting;

import java.time.Duration;

public interface IOperationMode {
    void setDurationOfExecution(Duration durationOfExecution);
    Duration getDurationOfExecution();
    boolean operationNotScheduled();
    ResultOfOperationSetting installOperation(IOperation operation, IResource currentResource);
    ResultOfOperationSetting reverseInstallOperation(IOperation operation, IResource currentResource);
    void setNewTactTime(IOperation operation);
    void setNewReverseTactTime(IOperation operation);
    IStructuralUnitOfResource isResourcesCanToBorrow(O_Basic OBasic, IResource resource);
    IOperationMode clone();
}
