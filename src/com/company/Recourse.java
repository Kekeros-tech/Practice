package com.company;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Recourse {
    private ArrayList<WorkingHours> schedule;
    private LocalDateTime releaseDate;

    Recourse(Collection<WorkingHours> schedule, LocalDateTime releaseDate)
    {
        this.schedule = new ArrayList<>(schedule);
        this.releaseDate = releaseDate;
    }

    Recourse(Collection<WorkingHours> schedule)
    {
        this.schedule = new ArrayList<>(schedule);
        this.releaseDate = LocalDateTime.now();
    }

    public void fillScheduleUsingPreviousData(LocalDateTime requiredDate) {
        if (!schedule.isEmpty()){
            int kol = 0;
            for(int i = 0; i < schedule.size();i++) {
                for(int j = i + 1; j < schedule.size(); j++) {
                    int first = schedule.get(i).getStartTime().getDayOfMonth();
                    int second = schedule.get(j).getStartTime().getDayOfMonth();
                    if(first == second) {
                        kol += 1;
                        break;
                    }
                }
            }
            int unique = schedule.size() - kol;
            System.out.println(unique);

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

    public LocalDateTime getReleaseTime() { return this.releaseDate; }


    public void setSchedule(Collection<WorkingHours> schedule) { this.schedule = new ArrayList<>(schedule); }

    public void setReleaseTime(LocalDateTime releaseDate) { this.releaseDate = releaseDate; }

    public void takeRecourse(Operation operation) {
        releaseDate = releaseDate.plusNanos(operation.getDurationOfExecution().toNanos());
    }

}
