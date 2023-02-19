package com.company;

import java.time.Duration;

public interface IOperationMode {
    void setDurationOfExecution(Duration durationOfExecution);
    Duration getDurationOfExecution();
    boolean operationNotScheduled();
    ResultOfOperationSetting installOperation(IOperation operation, IResource currentResource);
    ResultOfOperationSetting reverseInstallOperation(IOperation operation, IResource currentResource);
    void setNewTactTime(IOperation operation);
    void setNewReverseTactTime(IOperation operation);
    IStructuralUnitOfResource isResourcesCanToBorrow(Operation operation, IResource resource);
    IOperationMode clone();
}
