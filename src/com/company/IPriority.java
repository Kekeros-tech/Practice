package com.company;

public interface IPriority {
    public void setPriority(Operation operationToSetPriority);
    long getPriority();
    void clean();
}
