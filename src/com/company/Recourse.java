package com.company;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

public class Recourse {
    private ArrayList<WorkingHours> schedule;
    private WorkingHours releaseDate; // может сделать это как рабочие часы, чтобы был как промежуток работы между 2 временами, потому что иначе если
    // будет слишком много времени будет показываться, что станок занят, но задачу он выполнить ведь может

    Recourse(Collection<WorkingHours> schedule, String releaseDateStart, String releaseDateEnd)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.schedule = new ArrayList<>(schedule);
        this.releaseDate = new WorkingHours(releaseDateStart, releaseDateEnd);
    }

    Recourse(Collection<WorkingHours> schedule, String releaseDateEnd)
    {
        this.schedule = new ArrayList<>(schedule);
        this.releaseDate = new WorkingHours(releaseDateEnd);
    }

    public void fillScheduleUsingPreviousData(LocalDateTime requiredDate) //ещё подумать над реализацией
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
                    System.out.println(parameter.getStartTime()+" "+parameter.getEndTime());
                    schedule.add(parameter);
                }
                else { break; }
                iteration++;
           }
        }
    }

    public ArrayList<WorkingHours> getSchedule() { return this.schedule; }

    public WorkingHours getReleaseTime() { return this.releaseDate; }


    public void setSchedule(Collection<WorkingHours> schedule) { this.schedule = new ArrayList<>(schedule); }

    public void setReleaseTime(WorkingHours releaseDate) { this.releaseDate = releaseDate; }

    //Подумать ещё над реализацией
    public Duration takeRecourse(Duration currentDuration, int number) {
        releaseDate.setStartTime(schedule.get(number).getStartTime());
        releaseDate.setEndTime(schedule.get(number).getStartTime().plusNanos(currentDuration.toNanos()));
        Duration resultDuration = Duration.between(schedule.get(number).getStartTime(),schedule.get(number).getEndTime());
        resultDuration = currentDuration.minus(resultDuration);
        if(resultDuration.isNegative() || resultDuration.toNanos() == 0)
        {
            return Duration.ZERO;
        }
        return resultDuration;
        //releaseDate = schedule.get(number).getStartTime();
        //releaseDate = releaseDate.plusNanos(operation.getDurationOfExecution().toNanos());
    }

    public boolean isWorkingTime(LocalDateTime currentDate){
        if(currentDate.isAfter(releaseDate.getStartTime()) && currentDate.isBefore(releaseDate.getEndTime())){
            return false;
        }
        else{
            return true;
        }
    }
}
