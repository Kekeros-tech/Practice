package com.company.arrangement_algo;

import com.company.control_param.ControlParameters;
import com.company.Series;
import com.company.operation.IOperation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Algo_WithCPAndFuture extends Algo_WithCP {
    public Duration selectedDuration;

    public Algo_WithCPAndFuture(Collection<Series> seriesForWork, ControlParameters controlParameters, Duration selectedDuration) {
        super(seriesForWork, controlParameters);
        this.selectedDuration = selectedDuration;
    }

    public Algo_WithCPAndFuture(Collection<Series> seriesForWork) {

    }

    public Algo_WithCPAndFuture(Duration currentDuration) {
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
    protected ArrayList<IOperation> choiceFrontOfWork(Collection<IOperation> operationsToCreate) {
        Collection<IOperation> buffer = choiceCollectionOfOperationsWhichСanBePlacedInFront(operationsToCreate);

        LocalDateTime minTime = findMinTactTime(buffer);

        ArrayList<IOperation> frontOfWork = new ArrayList<>();
        for(IOperation operation: buffer){
            if(!operation.getTactTime().isAfter(minTime.plusNanos(selectedDuration.toNanos()))){
                frontOfWork.add(operation);
            }
        }

        return frontOfWork;
    }
}
