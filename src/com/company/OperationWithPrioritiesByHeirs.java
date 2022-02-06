package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

public class OperationWithPrioritiesByHeirs extends Operation{

    private int prioritiesByHeirs;

    public OperationWithPrioritiesByHeirs(Operation operation) {
        super(operation);
        prioritiesByHeirs = 0;
    }

    public OperationWithPrioritiesByHeirs() {
        super();
        prioritiesByHeirs = 0;
    }

    public int getPrioritiesByHeirs() {
        return prioritiesByHeirs;
    }

    public void setPrioritiesByHeirs() {
        if(super.getFollowingOperations().isEmpty()) {
            prioritiesByHeirs = 0;
        }
        else
        {
            prioritiesByHeirs = 1;
        }

        for(Operation followingOperation: super.getFollowingOperations()) {
            OperationWithPrioritiesByHeirs following = (OperationWithPrioritiesByHeirs) followingOperation;
            prioritiesByHeirs += following.prioritiesByHeirs;
        }
    }
}

