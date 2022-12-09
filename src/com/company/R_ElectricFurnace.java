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


    public Collection<CellElectricFurnace> getCellsOfElectricFurnace() { return cellsOfElectricFurnace; }
    public ArrayList<WorkingHours> getSchedule() { return schedule; }
    public LocalDateTime getArriveTime() { return arriveTime; }
    public StringBuffer getNameOfRecourse() { return nameOfRecourse; }

    R_ElectricFurnace(Collection<WorkingHours>        schedule,
                      Collection<CellElectricFurnace> cellsOfElectricFurnace,
                      String                          arriveTime)
    {
        nameOfRecourse = Series.generateRandomHexString(8);
        this.schedule = new ArrayList<>(schedule);
        this.cellsOfElectricFurnace = new ArrayList<>(cellsOfElectricFurnace);
        this.arriveTime = LocalDateTime.parse(arriveTime, WorkingHours.formatter);
    }

    R_ElectricFurnace(String arriveTime)
    {
        this(new ArrayList<>(), new ArrayList<>(), arriveTime);
    }

    public void addSchedule(WorkingHours currentWorkingHours){ schedule.add(currentWorkingHours); }
    public void addCellElectricFurnace(CellElectricFurnace cell) { cellsOfElectricFurnace.add(cell); }

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

    //Подумать ещё над реализацией
    public Duration takeResource(Duration currentDuration, LocalDateTime startTime, LocalDateTime endTime) {
        Duration resultDuration = Duration.between(startTime, endTime);
        resultDuration = currentDuration.minus(resultDuration);
        return resultDuration;
    }

    public Duration putOperationOnResource(Operation operation) {
        double requiredTemperature = ((O_OperationWithTemperatureAndVoltage) operation).getTemperatureOfOperation();
        double requiredVoltage = ((O_OperationWithTemperatureAndVoltage) operation).getVoltageOfOperation();

        for(WorkingHours currentWH: schedule) {
            for (CellElectricFurnace currentCellOfElectricFurnace: c)
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
