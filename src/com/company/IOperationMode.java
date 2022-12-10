package com.company;

import java.time.Duration;
import java.util.Collection;

public interface IOperationMode {
    void setDurationOfExecution(Duration durationOfExecution);
    Duration getDurationOfExecution();
    boolean operationNotScheduled();
    ResultOfOperationSetting installOperation(IOperation operation, IResource currentResource);
    ResultOfOperationSetting reverseInstallOperation(IOperation operation, IResource currentResource);
    void setNewTactTime(IOperation operation);
    void setNewReverseTactTime(IOperation operation);
    boolean isResourcesCanToBorrow(Operation operation, IResource resource);
}
