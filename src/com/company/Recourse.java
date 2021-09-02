package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

public class Recourse {
    private ArrayList<WorkingHours> schedule;
    private LocalDateTime releaseDate;

    Recourse(Collection<WorkingHours> schedule, String releaseDate)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.schedule = new ArrayList<>(schedule);
        this.releaseDate = LocalDateTime.parse(releaseDate, formatter);
    }

    Recourse() {};

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

    public ArrayList<WorkingHours> getSchedule() { return this.schedule; }

    public LocalDateTime getReleaseTime() { return this.releaseDate; }


    public void setSchedule(Collection<WorkingHours> schedule) { this.schedule = new ArrayList<>(schedule); }

    public void setReleaseTime(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setReleaseTime(String releaseDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.releaseDate = LocalDateTime.parse(releaseDate, formatter);;
    }


    //Подумать ещё над реализацией
    public Duration takeRecourse(Duration currentDuration, int number, LocalDateTime tackDate) {
        Duration resultDuration = Duration.between(tackDate,schedule.get(number).getEndTime());
        resultDuration = currentDuration.minus(resultDuration);
        //if(resultDuration.toNanos() <= 0) {
        //    releaseDate = schedule.get(number).getEndTime().plusNanos(resultDuration.toNanos());
        //}
        return resultDuration;
    }

    public void takeWhichCanBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate) {
        int iteration = 0;
        while (iteration < schedule.size() && !schedule.get(iteration).getStartTime().isAfter(tackDate)) {
            if (schedule.get(iteration).isWorkingTime(tackDate) && this.isFree(tackDate)) {
                int numberOfNextWorkingInterval = iteration + 1;
                durationOfExecution = this.takeRecourse(durationOfExecution, iteration, tackDate);
                while (durationOfExecution.toNanos() > 0) {
                    durationOfExecution = this.takeRecourse(durationOfExecution, numberOfNextWorkingInterval, schedule.get(numberOfNextWorkingInterval).getStartTime());
                    numberOfNextWorkingInterval++;
                }
                releaseDate = schedule.get(numberOfNextWorkingInterval - 1).getEndTime().plusNanos(durationOfExecution.toNanos());
                break;
            }
            iteration++;
        }

    }

    public void tackWhichCanNotBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate) {
        int iteration = 0;
        while (iteration < schedule.size() && !schedule.get(iteration).getStartTime().isAfter(tackDate)) {
            if(schedule.get(iteration).isWorkingTime(tackDate) && this.isFree(tackDate)) {
                Duration resultDuration = this.takeRecourse(durationOfExecution, iteration, tackDate);
                if(resultDuration.toNanos() <= 0)
                {
                    releaseDate = tackDate.plusNanos(durationOfExecution.toNanos());
                    break;
                }
            }
            iteration++;
        }
    }




    public boolean isFree(LocalDateTime currentDate){
        if(currentDate.isAfter(releaseDate)) {
            return true;
        }
        else if(currentDate.isEqual(releaseDate)){
            return true;
        }
        return false;
    }

    //public boolean nowFree(LocalDateTime operationStart, LocalDateTime operationEnd){
    //    if(releaseDate.getStartTime().isEqual(operationStart) && releaseDate.getEndTime().isBefore(operationEnd)){
    //        return false;
    //    }
    //    return true;
    //}
}
