package com.company;

import java.time.Duration;
import java.util.Collection;

public interface IOperationMode {
    void setDurationOfExecution(Duration durationOfExecution);
    Duration getDurationOfExecution();
    boolean operationNotScheduled();
    boolean installOperation(Operation operation, IResource currentResource);
    boolean reverseInstallOperation(Operation operation, IResource currentResource);
    void setNewTactTime(Operation operation);
    void setNewReverseTactTime(OperationWithPriorityNew operation);
    boolean isResourcesCanToBorrow(Operation operation, IResource resource);
}
