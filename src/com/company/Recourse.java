package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

public class Recourse {
    private ArrayList<WorkingHours> schedule;
    private LocalDateTime arriveTime;

    private LocalDateTime releaseTime;

    Recourse(Collection<WorkingHours> schedule, String releaseDate)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.schedule = new ArrayList<>(schedule);
        this.arriveTime = LocalDateTime.parse(releaseDate, formatter);
        this.releaseTime = LocalDateTime.parse(releaseDate, formatter);
    }

    Recourse() {};

    Recourse(String releaseDate)
    {
        schedule = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.arriveTime = LocalDateTime.parse(releaseDate, formatter);
        this.releaseTime = LocalDateTime.parse(releaseDate, formatter);
    }

    public void addSchedule(WorkingHours currentWorkingHours){
        schedule.add(currentWorkingHours);
    }

    public void addAllSchedule(Collection<WorkingHours> collectionWorkingHours) {
        schedule.addAll(collectionWorkingHours);
    }

    //задавать через правила, набор характеристик, как должен работать станок
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

    public ArrayList<WorkingHours> getSchedule() { return schedule; }

    public LocalDateTime getArriveTime() { return arriveTime; }

    public LocalDateTime getReleaseTime() { return  releaseTime; }



    public void setSchedule(Collection<WorkingHours> schedule) { this.schedule = new ArrayList<>(schedule); }

    public void setArriveTime(LocalDateTime releaseDate) {
        this.arriveTime = releaseDate;
    }

    public void setReleaseTime(String releaseDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.releaseTime = LocalDateTime.parse(releaseDate, formatter);
    }



    //Подумать ещё над реализацией
    public Duration takeRecourse(Duration currentDuration, int number, LocalDateTime tackDate) {
        Duration resultDuration = Duration.between(tackDate,schedule.get(number).getEndTime());
        resultDuration = currentDuration.minus(resultDuration);
        return resultDuration;
    }

    public Recourse takeWhichCanBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate) {
        int iteration = 0;
        while (iteration < schedule.size() && !schedule.get(iteration).getStartTime().isAfter(tackDate)) {
            if (schedule.get(iteration).isWorkingTime(tackDate) && this.isFree(tackDate)) {
                System.out.println("Ресурс" + this);
                int numberOfNextWorkingInterval = iteration + 1;
                durationOfExecution = this.takeRecourse(durationOfExecution, iteration, tackDate);
                while (durationOfExecution.toNanos() > 0) {
                    durationOfExecution = this.takeRecourse(durationOfExecution, numberOfNextWorkingInterval, schedule.get(numberOfNextWorkingInterval).getStartTime());
                    numberOfNextWorkingInterval++;
                }
                releaseTime = schedule.get(numberOfNextWorkingInterval - 1).getEndTime().plusNanos(durationOfExecution.toNanos());
                return this;
            }
            iteration++;
        }
        return null;
    }

    public Recourse tackWhichCanNotBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate) {
        int iteration = 0;
        while (iteration < schedule.size() && !schedule.get(iteration).getStartTime().isAfter(tackDate)) {
            if(schedule.get(iteration).isWorkingTime(tackDate) && this.isFree(tackDate)) {
                //System.out.println("Ресурс" + this);
                Duration resultDuration = this.takeRecourse(durationOfExecution, iteration, tackDate);
                if(resultDuration.toNanos() <= 0)
                {
                    System.out.println("Ресурс" + this);
                    releaseTime = tackDate.plusNanos(durationOfExecution.toNanos());
                    return this;
                }
            }
            iteration++;
        }
        return null;
    }

    public LocalDateTime getStartDateAfterReleaseDate(LocalDateTime tackDate) {
        for (WorkingHours currentWorkingHours: schedule) {
            if(currentWorkingHours.getStartTime().isAfter(releaseTime) && currentWorkingHours.getStartTime().isAfter(tackDate)) {
                return currentWorkingHours.getStartTime();
            }
            else if(currentWorkingHours.isWorkingTime(releaseTime)) {
                if(tackDate.isBefore(releaseTime))
                {
                    return releaseTime;
                }
            }
        }
        return null;
    }


    public LocalDateTime getCLateStartTimeBeforeReleaseDate(LocalDateTime tackDate, LocalDateTime StartDate) {
        for ( int i = schedule.size() - 1; i > 0; i-- ) {
            if(schedule.get(i).getStartTime().isAfter(StartDate) && schedule.get(i).getEndTime().isBefore(tackDate)) {
                return schedule.get(i).getStartTime();
            }
        }
        return null;
    }


    public boolean isFree(LocalDateTime currentDate) {
        if(currentDate.isBefore(releaseTime)) {
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

    public LocalDateTime tackReverseWhichCanNotBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate, LocalDateTime maxStartTime) {
        for(int i = schedule.size() - 1; i > 0; i--) {
            if(schedule.get(i).getStartTime().isAfter(maxStartTime) && schedule.get(i).getEndTime().isBefore(tackDate)){
                Duration resultDuration = Duration.between(schedule.get(i).getStartTime(), schedule.get(i).getEndTime());
                if(resultDuration.toNanos() >= durationOfExecution.toNanos()) {
                    return schedule.get(i).getEndTime().minusNanos(durationOfExecution.toNanos());
                }
            }
            else if(schedule.get(i).isWorkingTime(tackDate)) {
                Duration resultDuration = Duration.between(schedule.get(i).getStartTime(), tackDate);
                if(resultDuration.toNanos() >= durationOfExecution.toNanos()) {
                    return tackDate.minusNanos(durationOfExecution.toNanos());
                }
            }
        }
        return null;
    }

    public void clean() {
        releaseTime = arriveTime;
    }
}
