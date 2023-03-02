package com.company.priority;

import com.company.operation.IOperation;

public interface IPriority {
    void setPriority(IOperation operationToSetPriority);
    long getPriority();
    void clean();
}




