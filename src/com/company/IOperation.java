package com.company;

import java.text.CollationElementIterator;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public interface IOperation {
    boolean isСanBePlacedInFront();
    void setTactTime();
    void setTactTime(LocalDateTime tactTime);
    Series getSerialAffiliation();
    IOperationMode getCurrentOperatingMode();
    Duration getInitDurationOfExecution();
    boolean allFollowingAssigned();
    Group getResourceGroup();
    void setNameOfOperation(String nameOfOperation);
    LocalDateTime getCLateStartTime();
    LocalDateTime getCEarlierStartTime();
    ArrayList<IOperation> getFollowingOperations();
    ArrayList<IOperation> getPreviousOperations();
    void setPreviousOperation(ArrayList<IOperation> previousOperation);
    int getNumberOfOperationMode();
    ArrayList<WorkingHours> getCWorkingInterval();
    LocalDateTime getTactTime();
    Duration getDurationOfExecution();
    boolean operationNotScheduled();
    void setResourceGroup(Group resourceGroup);
    void setSerialAffiliation(Series serialAffiliation);
    void setDurationOfExecution(Duration durationOfExecution);
    void setOperatingMode(int currentOperatingMode);
    void addCNumberOfAssignedRecourse(IStructuralUnitOfResource cNumberOfAssignedRecourse);
    void addCWorkingInterval(WorkingHours cWorkingInterval);
    void addFollowingOperation(IOperation followingOperation);
    void setNewTactTime();
    ArrayList<IResource> getResourcesToBorrow();
    void installOperation(); // должно быть
    void installOperationForSpecificResource(IResource currentRecourse); // должно быть
    LocalDateTime installReverseOperation();
    void setTactTimeByEndTimeOfPrevious();
    boolean isCanBePlacedInReverseFront();
    void getLatestEndTimeOfFollowing();
    void clean();
    void fullClean();
}
