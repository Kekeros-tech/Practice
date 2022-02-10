package com.company;

import java.time.Duration;

public class OperationWithPrioritiesByDuration extends Operation{
    private Duration prioritiesByDuration;

    public OperationWithPrioritiesByDuration() {
        super();
        prioritiesByDuration = Duration.ZERO;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(super.toString());
        sb.append(" приоритет, относительно времени последователей=").append(prioritiesByDuration);
        return sb.toString();
    }

    public Duration getPrioritiesByDuration() {
        return prioritiesByDuration;
    }

    public void setPrioritiesByDuration() {
        prioritiesByDuration = super.getDurationOfExecution();

        for(Operation followingOperation: super.getFollowingOperations()) {
            prioritiesByDuration = prioritiesByDuration.plusNanos(((OperationWithPrioritiesByDuration) followingOperation).getPrioritiesByDuration().toNanos());
        }
    }
}
