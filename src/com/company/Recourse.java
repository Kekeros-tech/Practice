package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Recourse implements IResource{
    private StringBuffer nameOfRecourse;
    private ArrayList<WorkingHours> schedule;
    private LocalDateTime arriveTime;

    private LocalDateTime releaseTime;

    Recourse(Collection<WorkingHours> schedule, String releaseDate)
    {
        nameOfRecourse = Series.generateRandomHexString(8);
        this.schedule = new ArrayList<>(schedule);
        this.arriveTime = LocalDateTime.parse(releaseDate, WorkingHours.formatter);
        this.releaseTime = LocalDateTime.parse(releaseDate, WorkingHours.formatter);
    }

    Recourse() {};

    Recourse(String releaseDate)
    {
        this(new ArrayList<>(), releaseDate);
    }

    public void addSchedule(WorkingHours currentWorkingHours){
        schedule.add(currentWorkingHours);
    }

    public void addAllSchedule(Collection<WorkingHours> collectionWorkingHours) {
        schedule.addAll(collectionWorkingHours);
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

    public void fillScheduleUsingRules(LocalDateTime deadline) {

    }

    public ArrayList<WorkingHours> getSchedule() { return schedule; }

    public LocalDateTime getArriveTime() { return arriveTime; }

    public LocalDateTime getReleaseTime() { return  releaseTime; }

    public StringBuffer getNameOfRecourse() {
        return nameOfRecourse;
    }

    public void setSchedule(Collection<WorkingHours> schedule) { this.schedule = new ArrayList<>(schedule); }

    public void setArriveTime(LocalDateTime releaseDate) {
        this.arriveTime = releaseDate;
    }

    public void setReleaseTime(LocalDateTime releaseTime){
        this.releaseTime = releaseTime;
    }

    public void setReleaseTime(String releaseDate) {
        this.releaseTime = LocalDateTime.parse(releaseDate, WorkingHours.formatter);
    }



    //Подумать ещё над реализацией
    public Duration takeRecourse(Duration currentDuration, int number, LocalDateTime tackDate) {
        Duration resultDuration = Duration.between(tackDate,schedule.get(number).getEndTime());
        resultDuration = currentDuration.minus(resultDuration);
        return resultDuration;
    }

    public boolean takeResWhichCanNotBeInterrupted(Operation operation) {
        LocalDateTime futureRealiseTime = takeWhichCanNotBeInterrupted(operation);
        if(futureRealiseTime != null) {
            releaseTime = futureRealiseTime;
            return true;
        }
        return false;
    }

    public boolean takeResWhichCanBeInterrupted(Operation operation) {
        LocalDateTime futureRealiseTime = takeWhichCanBeInterrupted(operation);
        if(futureRealiseTime != null) {
            releaseTime = futureRealiseTime;
            return true;
        }
        return false;
    }

    public LocalDateTime takeWhichCanNotBeInterrupted(Operation currentOperation) {
        int iteration = 0;
        LocalDateTime tactDate = currentOperation.getTactTime();
        Duration durationOfExecution = currentOperation.getDurationOfExecution();

        while (iteration < schedule.size() && !schedule.get(iteration).getStartTime().isAfter(tactDate)) {
            if(schedule.get(iteration).isWorkingTime(tactDate) && this.isFree(tactDate)) {

                Duration resultDuration = this.takeRecourse(durationOfExecution, iteration, tactDate);

                if(resultDuration.toNanos() <= 0)
                {
                    return tactDate.plusNanos(durationOfExecution.toNanos());
                }
            }
            iteration++;
        }
        return null;
    }

    public LocalDateTime takeWhichCanBeInterrupted(Operation operation) {
        int iteration = 0;
        LocalDateTime tactDate = operation.getTactTime();
        Duration durationOfExecution = operation.getDurationOfExecution();

        while (iteration < schedule.size() && !schedule.get(iteration).getStartTime().isAfter(tactDate)) {
            if (schedule.get(iteration).isWorkingTime(tactDate) && this.isFree(tactDate)) {

                int numberOfNextWorkingInterval = iteration + 1;
                durationOfExecution = this.takeRecourse(durationOfExecution, iteration, tactDate);

                while (durationOfExecution.toNanos() > 0) {
                    durationOfExecution = this.takeRecourse(durationOfExecution, numberOfNextWorkingInterval, schedule.get(numberOfNextWorkingInterval).getStartTime());
                    numberOfNextWorkingInterval++;
                }

                return schedule.get(numberOfNextWorkingInterval - 1).getEndTime().plusNanos(durationOfExecution.toNanos());
            }
            iteration++;
        }
        return null;
    }

    @Override
    public boolean isTactDateWorkingTime(LocalDateTime tackDate, Operation operation) {
        for(WorkingHours currentWorkingHours: schedule) {
            if(currentWorkingHours.isWorkingTime(tackDate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public LocalDateTime getStartDateAfterReleaseDate(LocalDateTime tackDate, Operation operation) {
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
        releaseTime = arriveTime;
    }
}
