package com.company;

import java.text.CollationElementIterator;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

//Моделирует печь с несколькими ячейками, которые нельзя прерывать
public class R_ElectricFurnace implements IResource {
    private StringBuffer nameOfRecourse;
    private ArrayList<WorkingHours> schedule;
    private LocalDateTime arriveTime;
    private ArrayList<CellElectricFurnace> cellsOfElectricFurnace;

    private LocalDateTime releaseTime;


    public Collection<CellElectricFurnace> getCellsOfElectricFurnace() { return cellsOfElectricFurnace; }
    public ArrayList<WorkingHours> getSchedule() { return schedule; }
    public LocalDateTime getArriveTime() { return arriveTime; }
    public LocalDateTime getReleaseTime() { return  releaseTime; }
    public StringBuffer getNameOfRecourse() { return nameOfRecourse; }

    R_ElectricFurnace(Collection<WorkingHours>        schedule,
                      Collection<CellElectricFurnace> cellsOfElectricFurnace,
                      String                          releaseDate)
    {
        nameOfRecourse = Series.generateRandomHexString(8);
        this.schedule = new ArrayList<>(schedule);
        this.cellsOfElectricFurnace = new ArrayList<>(cellsOfElectricFurnace);
        this.arriveTime = LocalDateTime.parse(releaseDate, WorkingHours.formatter);
        this.releaseTime = LocalDateTime.parse(releaseDate, WorkingHours.formatter);
    }

    R_ElectricFurnace() {};

    R_ElectricFurnace(String releaseDate)
    {
        this(new ArrayList<>(), new ArrayList<>(), releaseDate);
    }

    public void setSchedule(Collection<WorkingHours> schedule) { this.schedule = new ArrayList<>(schedule); }
    public void addSchedule(WorkingHours currentWorkingHours){ schedule.add(currentWorkingHours); }
    public void addAllSchedule(Collection<WorkingHours> collectionWorkingHours) { schedule.addAll(collectionWorkingHours); }
    public void addCellElectricFurnace(CellElectricFurnace cell) { cellsOfElectricFurnace.add(cell); }
    public void addAllCellElectricFurnace(Collection<CellElectricFurnace> cellElectricFurnaceCollection) {cellsOfElectricFurnace.addAll(cellElectricFurnaceCollection); }
    public void setArriveTime(LocalDateTime releaseDate) { this.arriveTime = releaseDate; }
    public void setReleaseTime(LocalDateTime releaseTime){ this.releaseTime = releaseTime; }
    public void setReleaseTime(String releaseDate) { this.releaseTime = LocalDateTime.parse(releaseDate, WorkingHours.formatter); }

    //переписать метод задания через правила
    @Override
    public void fillScheduleUsingPreviousData(LocalDateTime requiredDate)
    {
        if (!schedule.isEmpty()) {
            int kol = 0;
            int maxSpan = 1;
            for(int i = 0; i < schedule.size(); i++) {
                int first = schedule.get(i).getStartTime().getDayOfMonth();
                for(int j = i + 1; j < schedule.size(); j++) {
                    int second = schedule.get(j).getStartTime().getDayOfMonth();
                    if(first == second) {
                        kol += 1;
                        break;
                    }
                }
                if(i != 0){
                    int second = schedule.get(i-1).getStartTime().getDayOfMonth();
                    if(first - second >= maxSpan) {
                        maxSpan = first - second;
                    }
                }
            }
            int lastDay = schedule.get(schedule.size()-1).getStartTime().getDayOfMonth();
            int firstDay = schedule.get(0).getStartTime().getDayOfMonth();
            int unique = lastDay + maxSpan - firstDay;

            int iteration = 0;
            for(int i = iteration; i < schedule.size(); i++){
                WorkingHours parameter = new WorkingHours(schedule.get(i).getStartTime().plusDays(unique),schedule.get(i).getEndTime().plusDays(unique));
                if(parameter.getEndTime().isBefore(requiredDate))
                {
                    //System.out.println(parameter.getStartTime()+" "+parameter.getEndTime());
                    schedule.add(parameter);
                }
                else { break; }
                iteration++;
            }
        }
    }

    public void fillScheduleUsingRules(LocalDateTime deadline) {

    }

    //Подумать ещё над реализацией
    public Duration takeRecourse(Duration currentDuration, int number, LocalDateTime tackDate) {
        Duration resultDuration = Duration.between(tackDate, schedule.get(number).getEndTime());
        resultDuration = currentDuration.minus(resultDuration);
        return resultDuration;
    }

    @Override
    public LocalDateTime takeWhichCanNotBeInterrupted(Operation operation) {
        int iteration = 0;
        LocalDateTime tactTime = operation.tactTime;
        double requiredTemperature = ((O_OperationWithPriorityAndQuantity) operation).getRequiredTemperature();
        double requiredVoltage = ((O_OperationWithPriorityAndQuantity) operation).getRequiredVoltage();

        while(iteration < schedule.size()) {
            if(schedule.get(iteration).isWorkingTime(tactTime)) {

                for (CellElectricFurnace currentCellOfFurnace: cellsOfElectricFurnace) {

                    if (requiredTemperature == currentCellOfFurnace.getTemperature()) {

                        for (CellWithVoltage currentCellWithVoltage : currentCellOfFurnace.getCellsWithVoltage()) {

                            if (requiredVoltage == currentCellWithVoltage.getVoltage() &&
                                                   currentCellWithVoltage.canFitOneItem() &&
                                                   currentCellWithVoltage.isFree(tactTime)) {
                                Duration resultDuration = takeRecourse(operation.durationOfExecution, iteration, tactTime);

                                if (resultDuration.toNanos() <= 0) {
                                    return tactTime.plusNanos(operation.getDurationOfExecution().toNanos());
                                }
                            }
                        }
                    }
                }
            }
            iteration++;
        }
        return null;
    }

    @Override
    public LocalDateTime takeWhichCanBeInterrupted(Operation operation) {
        int iteration = 0;
        LocalDateTime tactTime = operation.tactTime;
        double requiredTemperature = ((O_OperationWithPriorityAndQuantity) operation).getRequiredTemperature();
        double requiredVoltage = ((O_OperationWithPriorityAndQuantity) operation).getRequiredVoltage();
        Duration durationOfExecution = operation.getDurationOfExecution();

        while(iteration < schedule.size()) {
            if(schedule.get(iteration).isWorkingTime(tactTime)) {

                for (CellElectricFurnace currentCellOfFurnace: cellsOfElectricFurnace) {

                    if (requiredTemperature == currentCellOfFurnace.getTemperature()) {

                        for (CellWithVoltage currentCellWithVoltage : currentCellOfFurnace.getCellsWithVoltage()) {

                            if (requiredVoltage == currentCellWithVoltage.getVoltage() &&
                                    currentCellWithVoltage.canFitOneItem() &&
                                    currentCellWithVoltage.isFree(tactTime)) {

                                int numberOfNextWorkingInterval = iteration + 1;
                                durationOfExecution = this.takeRecourse(durationOfExecution, iteration, tactTime);

                                while (durationOfExecution.toNanos() > 0) {
                                    durationOfExecution = this.takeRecourse(durationOfExecution, numberOfNextWorkingInterval, schedule.get(numberOfNextWorkingInterval).getStartTime());
                                    numberOfNextWorkingInterval++;
                                }

                                return schedule.get(numberOfNextWorkingInterval - 1).getEndTime().plusNanos(durationOfExecution.toNanos());
                            }
                        }
                    }
                }
            }
            iteration++;
        }
        return null;
    }

    public boolean isTactDateWorkingTime(LocalDateTime tactTime, Operation operation) {
        double requiredTemperature = ((O_OperationWithPriorityAndQuantity) operation).getRequiredTemperature();
        double requiredVoltage = ((O_OperationWithPriorityAndQuantity) operation).getRequiredVoltage();
        for(WorkingHours currentWorkingHours: schedule) {
            if(currentWorkingHours.isWorkingTime(tactTime)) {
                for (CellElectricFurnace currentCellOfFurnace: cellsOfElectricFurnace) {
                    if (requiredTemperature == currentCellOfFurnace.getTemperature()) {
                        for (CellWithVoltage currentCellWithVoltage : currentCellOfFurnace.getCellsWithVoltage()) {

                            if (requiredVoltage == currentCellWithVoltage.getVoltage() &&
                                    currentCellWithVoltage.canFitOneItem() &&
                                    currentCellWithVoltage.isFree(tactTime)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean takeResWhichCanNotBeInterrupted(Operation operation) {
        double requiredTemperature = ((O_OperationWithQuantity) operation).getRequiredTemperature();
        double requiredVoltage = ((O_OperationWithQuantity) operation).getRequiredVoltage();
        int iteration = 0;
        boolean operationFit = false;
        while(iteration < schedule.size()) {
            if(schedule.get(iteration).isWorkingTime(operation.tactTime)) {
                for(CellElectricFurnace currentCellOfFurnace: cellsOfElectricFurnace) {
                    if(requiredTemperature == currentCellOfFurnace.getTemperature()) {
                        for(CellWithVoltage currentCellWithVoltage: currentCellOfFurnace.getCellsWithVoltage()) {
                            if(requiredVoltage == currentCellWithVoltage.getVoltage() &&
                                    currentCellWithVoltage.isFree(operation.tactTime)) {
                                Duration resultDuration = takeRecourse(operation.durationOfExecution, iteration, operation.tactTime);

                                if(resultDuration.toNanos() <= 0) {
                                    int placedDimension = currentCellWithVoltage.takeСell(((O_OperationWithQuantity) operation).getCountOfPartsToProcess());
                                    ((O_OperationWithQuantity) operation).handleDetails(placedDimension);
                                    currentCellWithVoltage.setReleaseTime(operation.tactTime.plusNanos(operation.getDurationOfExecution().toNanos()));
                                    operationFit = true;
                                }
                            }

                        }
                    }
                }
            }
            if(schedule.get(iteration).getStartTime().isAfter(operation.getTactTime())){
                break;
            }
            iteration++;
        }
        if(operationFit) {
            ((O_OperationWithQuantity) operation).addAssignedResource(this);
            WorkingHours workingHours = new WorkingHours(operation.tactTime, operation.tactTime.plusNanos(operation.getDurationOfExecution().toNanos()));
            ((O_OperationWithQuantity) operation).addWorkingInterval(workingHours);
            operation.serialAffiliation.setСNumberOfAssignedOperations(operation.serialAffiliation.getСNumberOfAssignedOperations() + 1);
            return true;
        }
        return false;
    }

    public boolean takeResWhichCanBeInterrupted(Operation operation) {
        return false;
    }

    @Override
    public LocalDateTime getStartDateAfterReleaseDate(LocalDateTime tactTime, Operation operation) {
        double requiredTemperature = ((O_OperationWithPriorityAndQuantity) operation).getRequiredTemperature();
        double requiredVoltage = ((O_OperationWithPriorityAndQuantity) operation).getRequiredVoltage();
        for (WorkingHours currentWorkingHours: schedule) {
            for(CellElectricFurnace currentCellOfFurnace: cellsOfElectricFurnace) {
                if(requiredTemperature == currentCellOfFurnace.getTemperature()) {
                    for(CellWithVoltage currentCellWithVoltage: currentCellOfFurnace.getCellsWithVoltage()) {
                        if(requiredVoltage == currentCellWithVoltage.getVoltage()) {
                            if(currentWorkingHours.getStartTime().isAfter(tactTime) && currentCellWithVoltage.isFree(currentWorkingHours.getStartTime())) {
                                return currentWorkingHours.getStartTime();
                            }
                            else if(currentWorkingHours.isWorkingTime(currentCellWithVoltage.getReleaseTime()) && tactTime.isBefore(releaseTime)) {
                                return currentCellWithVoltage.getReleaseTime();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean isFree(LocalDateTime tactTime) {
        if(tactTime.isBefore(releaseTime)) {
            return false;
        }
        return true;
    }



    public LocalDateTime takeReverseWhichCanBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate, LocalDateTime maxStartTime) {
        for( int i = schedule.size() - 1; i > 0; i--) {
            if(schedule.get(i).getStartTime().isAfter(maxStartTime) && schedule.get(i).getEndTime().isBefore(tackDate)) {
                Duration resultDuration = durationOfExecution.minus(Duration.between(schedule.get(i).getStartTime(), schedule.get(i).getEndTime()));
                int iteration = i - 1;
                while(resultDuration.toNanos() > 0) {
                    resultDuration = resultDuration.minus(Duration.between(schedule.get(iteration).getStartTime(),schedule.get(iteration).getEndTime()));
                    iteration--;
                }
                return schedule.get(iteration + 1).getStartTime().minusNanos(resultDuration.toNanos());
            }
            else if(schedule.get(i).isWorkingTime(tackDate)) {
                Duration resultDuration = durationOfExecution.minus(Duration.between(schedule.get(i).getStartTime(), tackDate));
                int iteration = i - 1;
                while(resultDuration.toNanos() > 0) {
                    resultDuration = resultDuration.minus(Duration.between(schedule.get(iteration).getStartTime(),schedule.get(iteration).getEndTime()));
                    iteration--;
                }
                return schedule.get(iteration + 1).getStartTime().minusNanos(resultDuration.toNanos());
            }
        }
        return null;
    }

    public WorkingHours getEndTimeBeforeTactDate( LocalDateTime tactDate, LocalDateTime maxStartTime){
        for(int i = schedule.size() - 1; i >= 0; i--) {

            if(!schedule.get(i).getStartTime().isBefore(maxStartTime) && !schedule.get(i).getEndTime().isAfter(tactDate)) {
                return schedule.get(i);
            }
            else if(schedule.get(i).isWorkingTime(tactDate)) {
                return new WorkingHours(schedule.get(i).getStartTime(), tactDate);
            }
        }
        return null;
    }

    public LocalDateTime tackReverseWhichCanNotBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate, LocalDateTime maxStartTime) {
        WorkingHours tactDate = getEndTimeBeforeTactDate(tackDate, maxStartTime);
        Duration resultDuration = Duration.between(tactDate.getStartTime(), tactDate.getEndTime());

        if(resultDuration.toNanos() >= durationOfExecution.toNanos()) {
            return tactDate.getEndTime().minusNanos(durationOfExecution.toNanos());
        }
        return null;
    }

    @Override
    public void clean() {
        for(CellElectricFurnace currentCellElectricFurnace: cellsOfElectricFurnace){
            for(CellWithVoltage currentCellWithVoltage: currentCellElectricFurnace.getCellsWithVoltage()){
                currentCellWithVoltage.setReleaseTime(arriveTime);
            }
        }
    }
}
