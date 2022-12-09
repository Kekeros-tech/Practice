package com.company;

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
            priorityByHeirs += ((OperationWithPriorityNew) followingOperation).getPriority();
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
