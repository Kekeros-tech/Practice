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

    public void setReleaseTime(LocalDateTime releaseDate) { this.releaseDate = releaseDate; }

    //Подумать ещё над реализацией
    public Duration takeRecourse(Duration currentDuration, int number) {
        //releaseDate = schedule.get(number).getStartTime().plusNanos(currentDuration.toNanos());
        Duration resultDuration = Duration.between(schedule.get(number).getStartTime(),schedule.get(number).getEndTime());
        resultDuration = currentDuration.minus(resultDuration);
        if(resultDuration.toNanos() > 0){
            releaseDate = schedule.get(number).getStartTime().plusNanos(resultDuration.toNanos());
        }
        else
        {
            releaseDate = schedule.get(number).getEndTime().plusNanos(resultDuration.toNanos());
        }
        //if(resultDuration.isNegative() || resultDuration.toNanos() == 0)
        //{
        //    return Duration.ZERO;
        //}
        return resultDuration;
    }

    public boolean isFree(LocalDateTime currentDate){
        if(currentDate.isAfter(releaseDate)) {
            return true;
        }
        else{
            return false;
        }
    }

    //public boolean nowFree(LocalDateTime operationStart, LocalDateTime operationEnd){
    //    if(releaseDate.getStartTime().isEqual(operationStart) && releaseDate.getEndTime().isBefore(operationEnd)){
    //        return false;
    //    }
    //    return true;
    //}
}
