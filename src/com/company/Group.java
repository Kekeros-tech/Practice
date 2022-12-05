package com.company;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Group {
    private StringBuffer nameOfGroup;
    private Collection<Recourse> recoursesInTheGroup;

    public Group() { //он ведь так по умолчанию формирует
        nameOfGroup = Series.generateRandomHexString(8);
        recoursesInTheGroup = new ArrayList<>();
    }

    public Group(Collection<Recourse> recoursesInTheGroup) {
        this.recoursesInTheGroup = new ArrayList<>(recoursesInTheGroup);
    }

    public void setRecoursesInTheGroup(Collection<Recourse> recoursesInTheGroup) { this.recoursesInTheGroup = new ArrayList<>(recoursesInTheGroup); }

    public void setRecoursesInTheGroup(Recourse recourse) {
        recoursesInTheGroup.clear();
        recoursesInTheGroup.add(recourse);
    }

    public void addRecourseInTheGroup(Recourse recourse) { recoursesInTheGroup.add(recourse); }

    public void addRecourseCollectionInTheGroup(Collection<Recourse> recoursesInTheGroup) {
        this.recoursesInTheGroup.addAll(recoursesInTheGroup);
    }

    public Collection<Recourse>  getRecoursesInTheGroup() { return recoursesInTheGroup; }

    public Recourse get(int number){
        Recourse[] recoursesArray = recoursesInTheGroup.toArray(new Recourse[recoursesInTheGroup.size()]);
        return recoursesArray[number];
    }

    public StringBuffer getNameOfGroup() {
        return nameOfGroup;
    }

    public LocalDateTime findTactTimeOfOperationAfterCurrentDate(LocalDateTime currentDate, Operation operation) {
        LocalDateTime tactTime = LocalDateTime.MAX;
        for(Recourse currentRecourse: recoursesInTheGroup) {
            if(currentRecourse.isTactDateWorkingTime(currentDate, operation)) {
                return currentDate;
            }
            else {
                LocalDateTime startDateAfterArrivalTime = currentRecourse.getStartDateAfterReleaseDate(currentDate, operation);
                if(startDateAfterArrivalTime.isBefore(tactTime)) {
                    tactTime =  startDateAfterArrivalTime;
                }
            }
        }
        return tactTime;
    }
}
