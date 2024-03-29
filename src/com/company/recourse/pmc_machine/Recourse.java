package com.company.recourse.pmc_machine;

import com.company.results_of_algos.ResultOfRecourseBooking;
import com.company.Series;
import com.company.WorkingHours;
import com.company.operation.IOperation;
import com.company.recourse.IResource;
import com.company.recourse.IStructuralUnitOfResource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Recourse implements IResource, IStructuralUnitOfResource {
    private StringBuffer nameOfRecourse;
    private ArrayList<WorkingHours> schedule;
    private LocalDateTime arriveTime;

    private LocalDateTime releaseTime;

    public Recourse(Collection<WorkingHours> schedule, String releaseDate)
    {
        nameOfRecourse = Series.generateRandomHexString(8);
        this.schedule = new ArrayList<>(schedule);
        this.arriveTime = LocalDateTime.parse(releaseDate, WorkingHours.formatter);
        this.releaseTime = LocalDateTime.parse(releaseDate, WorkingHours.formatter);
    }

    Recourse() {};

    public Recourse(String releaseDate)
    {
        this(new ArrayList<>(), releaseDate);
    }

    public void addSchedule(WorkingHours currentWorkingHours){
        schedule.add(currentWorkingHours);
    }

    //переписать метод задания через правила
    public void fillScheduleUsingPreviousData(LocalDateTime requiredDate)
    {
        if (!schedule.isEmpty()){
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

    public void setSchedule(Collection<WorkingHours> schedule) { this.schedule = new ArrayList<>(schedule); }

    public void setReleaseTime(LocalDateTime releaseTime) { this.releaseTime = releaseTime; }

    @Override
    public void setReleaseTime(int count, WorkingHours workingHours) {
        this.releaseTime = workingHours.getEndTime();
    }

    @Override
    public int getResourceAmount(LocalDateTime tactTime) {
        return 1;
    }

    @Override
    public IResource getCoreResource() {
        return this;
    }

    public Duration takeResource(Duration currentDuration, LocalDateTime startTime, LocalDateTime endTime) {
        Duration resultDuration = Duration.between(startTime, endTime);
        resultDuration = currentDuration.minus(resultDuration);
        return resultDuration;
    }

    public ResultOfRecourseBooking putOperationOnResource(IOperation operation) {
        LocalDateTime tactDate = operation.getTactTime();
        Duration durationOfExecution = operation.getDurationOfExecution();

        for(WorkingHours currentWH: schedule) {
            if(currentWH.getStartTime().isAfter(tactDate)) break;
            if(currentWH.isWorkingTime(tactDate) && this.isFree(tactDate)) {
                Duration durationOfBooking = this.takeResource(durationOfExecution, tactDate, currentWH.getEndTime());
                ResultOfRecourseBooking resultOfRecourseBooking = new ResultOfRecourseBooking(durationOfBooking, this);
                return resultOfRecourseBooking;
            }
        }
        return null;
    }

    public ResultOfRecourseBooking putReverseOperationOnResource(IOperation operation) {
        LocalDateTime tactDate = operation.getTactTime();
        Duration durationOfExecution = operation.getDurationOfExecution();

        for(int i = schedule.size() - 1; i >= 0; i--) {
            if(schedule.get(i).getEndTime().isBefore(tactDate)) break;
            if(schedule.get(i).isWorkingTime(tactDate)) {
                Duration durationOfBooking = this.takeResource(durationOfExecution, schedule.get(i).getStartTime(), tactDate);
                ResultOfRecourseBooking resultOfRecourseBooking = new ResultOfRecourseBooking(durationOfBooking, this);
                return resultOfRecourseBooking;
            }
            //return this.takeResource(durationOfExecution, schedule.get(i).getStartTime(), tactDate);
        }
        return null;
    }

    @Override
    public LocalDateTime getStartDateAfterReleaseDate(LocalDateTime tackDate, IOperation operation) {
        for (WorkingHours currentWorkingHours: schedule) {
            if(currentWorkingHours.getStartTime().isAfter(tackDate) && this.isFree(currentWorkingHours.getStartTime())) {
                return currentWorkingHours.getStartTime();
            }
            else if(currentWorkingHours.isWorkingTime(releaseTime) && tackDate.isBefore(releaseTime)) {
                return releaseTime;
            }
        }
        return null;
    }

    @Override
    public LocalDateTime getReverseStartDateAfterTactTime(LocalDateTime tackTime, IOperation operation) {
        for(int i = schedule.size() - 1; i >= 0; i--) {
            if(schedule.get(i).getEndTime().isBefore(tackTime)) {
                return schedule.get(i).getEndTime();
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

    @Override
    public void clean() {
        releaseTime = arriveTime;
    }
}
