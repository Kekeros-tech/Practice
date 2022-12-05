package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class OperationsArrangementAlgorithmWithCPAndFutureFrontNew extends OperationsArrangementAlgorithmWithCPNew {
    Duration selectedDuration;

    public OperationsArrangementAlgorithmWithCPAndFutureFrontNew(Collection<Series> seriesForWork, ControlParameters controlParameters, Duration selectedDuration) {
        super(seriesForWork, controlParameters);
        this.selectedDuration = selectedDuration;
    }

    public OperationsArrangementAlgorithmWithCPAndFutureFrontNew(Duration currentDuration) {
        super();
        this.selectedDuration = currentDuration;
    }

    public void setSelectedDuration(Duration selectedDuration) {
        this.selectedDuration = selectedDuration;
    }

    @Override
    public void takeSeriesToWork() {
        Collection<Series> workingListOfSeries = prepareOperationsForInstallation();

        for(Series series: workingListOfSeries) {
            installOperations(series.getOperationsToCreate());
        }
    }

    @Override
    protected ArrayList<Operation> choiceFrontOfWork(Collection<Operation> operationsToCreate) {
        Collection<Operation> buffer = choiceCollectionOfOperationsWhichСanBePlacedInFront(operationsToCreate);

        LocalDateTime minTime = findMinTactTime(buffer);

        ArrayList<Operation> frontOfWork = new ArrayList<>();
        for(Operation operation: buffer){
            if(!operation.getTactTime().isAfter(minTime.plusNanos(selectedDuration.toNanos()))){
                frontOfWork.add(operation);
            }
        }

        return frontOfWork;
    }
}
