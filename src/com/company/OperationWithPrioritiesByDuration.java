package com.company;

import java.time.Duration;

public class OperationWithPrioritiesByDuration extends Operation{
    private Duration prioritiesByDuration;

    public OperationWithPrioritiesByDuration() {
        super();
        prioritiesByDuration = Duration.ZERO;
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
