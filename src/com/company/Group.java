package com.company;
import java.util.ArrayList; // Либо HashSet, если оборудование уникальное
import java.util.Collection;

public class Group {
    private ArrayList<Recourse> recoursesInTheGroup;

    public Group() { //он ведь так по умолчанию формирует
        recoursesInTheGroup = new ArrayList<>();
    }

    public Group(Collection<Recourse> recoursesInTheGroup) {
        this.recoursesInTheGroup = new ArrayList<>(recoursesInTheGroup);
    }

    public void setRecoursesInTheGroup(Collection<Recourse> recoursesInTheGroup) { this.recoursesInTheGroup = new ArrayList<>(recoursesInTheGroup); }

    public void addRecourseInTheGroup(Recourse recourse) { recoursesInTheGroup.add(recourse); }

    public void addRecourseCollectionInTheGroup(Collection<Recourse> recoursesInTheGroup) {
        this.recoursesInTheGroup.addAll(recoursesInTheGroup);
    }

    public ArrayList<Recourse>  getRecoursesInTheGroup() { return recoursesInTheGroup; }

}
