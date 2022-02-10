package com.company;
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
}
