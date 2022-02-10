package com.company;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    //доработать
    public void fillScheduleUsingRules(LocalDateTime deadline, Schedule rules) {
        if(rules.getTypeOfWork() == TypeOfWorkByDay.fiveByTwo){
            switch (arriveTime.getDayOfWeek()){
                case SATURDAY: {
                    arriveTime = arriveTime.plusDays(2);
                    break;
                }
                case SUNDAY: {
                    arriveTime = arriveTime.plusDays(1);
                    break;
                }
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        ArrayList<LocalTime> startDates = new ArrayList<>();
        LocalTime startTime = LocalTime.parse(rules.getStartTime(),formatter);
        startDates.add(startTime);
        //startDates.add(LocalDateTime.parse(rules.getStartTimeOfBreaks()[0], formatter));
        int k = 0;
        for(int i = 0; i < rules.getDurationOfBreaks().length; i++) {
            LocalTime firstDate = startDates.get(i+k);
            LocalTime secondDate = LocalTime.parse(rules.getStartTimeOfBreaks()[i], formatter);
            if(!secondDate.isAfter(firstDate)){
                startDates.add(LocalTime.parse(rules.getEndTime(),formatter));
                startDates.add(LocalTime.parse(rules.getStartTime(), formatter));
                i--;
                k+=2;
            }
            else{
                startDates.add(secondDate);
                startDates.add(secondDate.plusNanos(rules.getDurationOfBreaks()[i].toNanos()));
            }
        }
        int iteration = 0;
        int count = 0;
        WorkingHours lastRecord = new WorkingHours(arriveTime, startDates.get(0).format(formatter), startDates.get(1).format(formatter));
        LocalDateTime currentDate = arriveTime;
        switch (rules.getTypeOfWork()){
            case fiveByTwo:{
                while (!lastRecord.getEndTime().isAfter(deadline)){
                    while(currentDate.getDayOfWeek()!=DayOfWeek.FRIDAY) {
                        while (lastRecord.getEndTime().format(formatter) != rules.getEndTime()){
                            lastRecord = new WorkingHours(currentDate, startDates.get(iteration).format(formatter),
                                    startDates.get(iteration+1).format(formatter));
                            iteration += 2;
                        }
                        currentDate = lastRecord.getEndTime().plusDays(1);
                    }
                    currentDate = lastRecord.getEndTime();
                    currentDate = currentDate.plusDays(2);
                }
                break;
            }
            case twoByTwo: {
                break;
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

    public void setReleaseTime(LocalDateTime releaseTime){
        this.releaseTime = releaseTime;
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

    public LocalDateTime takeWhichCanNotBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate) {
        int iteration = 0;
        while (iteration < schedule.size() && !schedule.get(iteration).getStartTime().isAfter(tackDate)) {
            if(schedule.get(iteration).isWorkingTime(tackDate) && this.isFree(tackDate)) {

                Duration resultDuration = this.takeRecourse(durationOfExecution, iteration, tackDate);

                if(resultDuration.toNanos() <= 0)
                {
                    return tackDate.plusNanos(durationOfExecution.toNanos());
                }
            }
            iteration++;
        }
        return null;
    }

    public LocalDateTime takeWhichCanBeInterrupted(Duration durationOfExecution, LocalDateTime tackDate) {
        int iteration = 0;
        while (iteration < schedule.size() && !schedule.get(iteration).getStartTime().isAfter(tackDate)) {
            if (schedule.get(iteration).isWorkingTime(tackDate) && this.isFree(tackDate)) {

                int numberOfNextWorkingInterval = iteration + 1;
                durationOfExecution = this.takeRecourse(durationOfExecution, iteration, tackDate);

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

    public boolean isTactDateWorkingTime(LocalDateTime tackDate) {
        for(WorkingHours currentWorkingHours: schedule) {
            if(currentWorkingHours.isWorkingTime(tackDate)) {
                return true;
            }
        }
        return false;
    }

    public LocalDateTime getStartDateAfterReleaseDate(LocalDateTime tackDate) {
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

    public void clean() {
        releaseTime = arriveTime;
    }
}
