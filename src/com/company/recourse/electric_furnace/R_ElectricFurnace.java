package com.company.recourse.electric_furnace;

import com.company.*;
import com.company.operation.IOperation;
import com.company.operation.O_TemperatureAndVoltage;
import com.company.recourse.IResource;
import com.company.results_of_algos.ResultOfRecourseBooking;

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


    public Collection<CellElectricFurnace> getCellsOfElectricFurnace() { return cellsOfElectricFurnace; }
    public ArrayList<WorkingHours> getSchedule() { return schedule; }
    public LocalDateTime getArriveTime() { return arriveTime; }
    public StringBuffer getNameOfRecourse() { return nameOfRecourse; }

/*    R_ElectricFurnace(Collection<WorkingHours>        schedule,
                      Collection<CellElectricFurnace> cellsOfElectricFurnace,
                      String                          arriveTime)
    {
        nameOfRecourse = Series.generateRandomHexString(8);
        this.schedule = new ArrayList<>(schedule);
        this.cellsOfElectricFurnace = new ArrayList<>(cellsOfElectricFurnace);
        this.arriveTime = LocalDateTime.parse(arriveTime, WorkingHours.formatter);
    }*/

     public R_ElectricFurnace(Collection<WorkingHours> schedule,
                              Collection<S_CellElectricFurnace> cellElectricFurnaces,
                              String arriveTime) {
        nameOfRecourse = Series.generateRandomHexString(8);
        this.arriveTime = LocalDateTime.parse(arriveTime, WorkingHours.formatter);
        this.schedule = new ArrayList<>(schedule);
        this.cellsOfElectricFurnace = new ArrayList<>();
        for(S_CellElectricFurnace currentSettings: cellElectricFurnaces) {
            ArrayList<CellWithVoltage> cellsWithVoltage = new ArrayList<>();
            for(S_CellWithVoltage currentSettingsForCellWithVoltage: currentSettings.getSettingsForCellWithVoltage()) {
                CellWithVoltage cellWithVoltage = new CellWithVoltage(currentSettingsForCellWithVoltage, this.arriveTime, this);
                cellsWithVoltage.add(cellWithVoltage);
            }
            cellsOfElectricFurnace.add(new CellElectricFurnace(currentSettings.getTemperature(), cellsWithVoltage));
        }
    }

    public R_ElectricFurnace(String arriveTime)
    {
        this(new ArrayList<>(), new ArrayList<>(), arriveTime);
    }

    public void addSchedule(WorkingHours currentWorkingHours){ schedule.add(currentWorkingHours); }
    public void addCellElectricFurnace(CellElectricFurnace cell) { cellsOfElectricFurnace.add(cell); }

    public Duration takeResource(Duration currentDuration, LocalDateTime startTime, LocalDateTime endTime) {
        Duration resultDuration = Duration.between(startTime, endTime);
        resultDuration = currentDuration.minus(resultDuration);
        return resultDuration;
    }

    @Override
    public ResultOfRecourseBooking putOperationOnResource(IOperation operation) {
        double requiredTemperature = ((O_TemperatureAndVoltage) operation).getTemperatureOfOperation();
        double requiredVoltage = ((O_TemperatureAndVoltage) operation).getVoltageOfOperation();
        Duration durationOfExecution = operation.getDurationOfExecution();
        LocalDateTime tactTime = operation.getTactTime();

        for(WorkingHours currentWH: schedule) {
            if(currentWH.getStartTime().isAfter(operation.getTactTime())) break;
            if(currentWH.isWorkingTime(operation.getTactTime())) {
                for (CellElectricFurnace currentCellElectricFurnace: cellsOfElectricFurnace) {
                    if (requiredTemperature == currentCellElectricFurnace.getTemperature()) {
                        for (CellWithVoltage currentCellWithVoltage: currentCellElectricFurnace.getCellsWithVoltage()) {
                            if (requiredVoltage == currentCellWithVoltage.getVoltage()
                                    && currentCellWithVoltage.canFitOneItem(tactTime)) {
                                Duration durationOfBooking = takeResource(durationOfExecution, tactTime, currentWH.getEndTime());
                                ResultOfRecourseBooking resultOfRecourseBooking = new ResultOfRecourseBooking(durationOfBooking, currentCellWithVoltage);
                                return resultOfRecourseBooking;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ResultOfRecourseBooking putReverseOperationOnResource(IOperation operation) {
        double requiredTemperature = ((O_TemperatureAndVoltage) operation).getTemperatureOfOperation();
        double requiredVoltage = ((O_TemperatureAndVoltage) operation).getVoltageOfOperation();
        LocalDateTime tactDate = operation.getTactTime();
        Duration durationOfExecution = operation.getDurationOfExecution();

        for(int i = schedule.size() - 1; i >= 0; i--) {
            if(schedule.get(i).getEndTime().isBefore(tactDate)) break;
            if(schedule.get(i).isWorkingTime(tactDate)) {
                for(CellElectricFurnace currentCellElectricFurnace: cellsOfElectricFurnace) {
                    if(requiredTemperature == currentCellElectricFurnace.getTemperature()) {
                        for(CellWithVoltage currentCellWithVoltage: currentCellElectricFurnace.getCellsWithVoltage()) {
                            if(requiredVoltage == currentCellWithVoltage.getVoltage()) {
                                Duration durationOfBooking = this.takeResource(durationOfExecution, schedule.get(i).getStartTime(), tactDate);
                                ResultOfRecourseBooking resultOfRecourseBooking = new ResultOfRecourseBooking(durationOfBooking, currentCellWithVoltage);
                                return resultOfRecourseBooking;
                            }
                        }
                    }
                }
            }
            //return this.takeResource(durationOfExecution, schedule.get(i).getStartTime(), tactDate);
        }
        return null;
    }

    @Override
    public LocalDateTime getStartDateAfterReleaseDate(LocalDateTime tactTime, IOperation operation) {
        double requiredTemperature = ((O_TemperatureAndVoltage) operation).getTemperatureOfOperation();
        double requiredVoltage = ((O_TemperatureAndVoltage) operation).getVoltageOfOperation();
        for (WorkingHours currentWorkingHours: schedule) {
            //System.out.println(currentWorkingHours);
            for(CellElectricFurnace currentCellOfElectricFurnace: cellsOfElectricFurnace) {
                if(requiredTemperature == currentCellOfElectricFurnace.getTemperature()) {
                    for(CellWithVoltage currentCellWithVoltage: currentCellOfElectricFurnace.getCellsWithVoltage()) {
                        if(requiredVoltage == currentCellWithVoltage.getVoltage()) {
                            LocalDateTime releaseTime = currentCellWithVoltage.getReleaseTime(tactTime);
                            //System.out.println(releaseTime.format(WorkingHours.formatter));
                            if(currentWorkingHours.getStartTime().isAfter(tactTime) && currentCellWithVoltage.canFitOneItem(currentWorkingHours.getStartTime())) {
                                return currentWorkingHours.getStartTime();
                            }
                            else if(currentWorkingHours.isWorkingTime(releaseTime) &&
                                currentCellWithVoltage.canFitOneItem(releaseTime) && tactTime.isBefore(releaseTime)) {
                                return releaseTime;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public LocalDateTime getReverseStartDateAfterTactTime(LocalDateTime tackTime, IOperation operation) {
        double requiredTemperature = ((O_TemperatureAndVoltage) operation).getTemperatureOfOperation();
        double requiredVoltage = ((O_TemperatureAndVoltage) operation).getVoltageOfOperation();
        for(int i = schedule.size() - 1; i >= 0; i--) {
            if(schedule.get(i).getEndTime().isBefore(tackTime)) {
                for(CellElectricFurnace currentCellOfElectricFurnace: cellsOfElectricFurnace) {
                    if(requiredTemperature == currentCellOfElectricFurnace.getTemperature()) {
                        for(CellWithVoltage currentCellWithVoltage: currentCellOfElectricFurnace.getCellsWithVoltage()) {
                            if(requiredVoltage == currentCellWithVoltage.getVoltage()){
                                return schedule.get(i).getEndTime();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

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

    @Override
    public void clean() {
        for(CellElectricFurnace currentCellElectricFurnace: cellsOfElectricFurnace){
            for(CellWithVoltage currentCellWithVoltage: currentCellElectricFurnace.getCellsWithVoltage()){
                currentCellWithVoltage.setReleaseTime(arriveTime);
            }
        }
    }
}
