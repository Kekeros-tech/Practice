package com.company;

public interface IPriority {
    void setPriority(IOperation operationToSetPriority);
    long getPriority();
    void clean();
}
