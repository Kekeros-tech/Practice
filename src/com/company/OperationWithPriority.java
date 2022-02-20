package com.company;

import java.time.Duration;

public class OperationWithPriority extends Operation{
    private int prioritiesByHeirs;
    private Duration prioritiesByDuration;

    public OperationWithPriority(Operation operation) {
        super(operation);
        prioritiesByHeirs = 0;
        prioritiesByDuration = Duration.ZERO;
    }

    public OperationWithPriority() {
        super();
        prioritiesByHeirs = 0;
        prioritiesByDuration = Duration.ZERO;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(super.toString());
        sb.append(" приоритет относительно количества последователей=").append(prioritiesByHeirs);
        sb.append(" приоритет, относительно времени последователей=").append(prioritiesByDuration);
        return sb.toString();
    }

    public Duration getPrioritiesByDuration() {
        return prioritiesByDuration;
    }

    public int getPrioritiesByHeirs() {
        return prioritiesByHeirs;
    }

    public void setPrioritiesByHeirs() {
        prioritiesByHeirs = 1;

        for(Operation followingOperation: super.getFollowingOperations()) {
            OperationWithPriority following = (OperationWithPriority) followingOperation;
            prioritiesByHeirs += following.prioritiesByHeirs;
        }
    }

    public void setPrioritiesByDuration() {
        prioritiesByDuration = super.getDurationOfExecution();

        for(Operation followingOperation: super.getFollowingOperations()) {
            prioritiesByDuration = prioritiesByDuration.plusNanos(((OperationWithPriority) followingOperation).getPrioritiesByDuration().toNanos());
        }
    }

    public void fullClean() {
        super.fullClean();
        prioritiesByHeirs = 0;
        prioritiesByDuration = Duration.ZERO;
    }
}
