package com.company.recourse;

import com.company.WorkingHours;

import java.time.LocalDateTime;

public interface IStructuralUnitOfResource {
    void setReleaseTime(LocalDateTime releaseTime);
    void setReleaseTime(int count, WorkingHours workingHours);
    int getResourceAmount(LocalDateTime tactTime);
    IResource getCoreResource();
    void clean();
}
