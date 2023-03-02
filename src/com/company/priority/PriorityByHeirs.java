package com.company.priority;

import com.company.operation.IOperation;
import com.company.operation.O_Priority;

public class PriorityByHeirs implements IPriority {
    private long priorityByHeirs;

    public PriorityByHeirs() {
        priorityByHeirs = 0;
    }

    @Override
    public void setPriority(IOperation operationToSetPriority) {
        if(priorityByHeirs != 0) {
            return;
        }
        priorityByHeirs = 1;

        for(IOperation followingOperation: operationToSetPriority.getFollowingOperations()) {
            priorityByHeirs += ((O_Priority) followingOperation).getPriority();
        }
    }

    @Override
    public long getPriority() {
        return priorityByHeirs;
    }

    @Override
    public void clean() {
        priorityByHeirs = 0;
    }
}
