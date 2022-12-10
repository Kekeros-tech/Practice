package com.company;

import java.time.LocalDateTime;

public interface IStructuralUnitOfResource {
    void setReleaseTime(LocalDateTime releaseTime);
    void clean();
}
