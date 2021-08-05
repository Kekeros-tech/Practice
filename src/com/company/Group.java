package com.company;
import java.util.ArrayList; // Либо HeshSet, если оборудование уникальное

public class Group {
    private ArrayList<Recourse> recoursesInTheGroup;

    public Group() { //он ведь так по умолчанию формирует
        recoursesInTheGroup = new ArrayList<Recourse>();
    }
    public Group(ArrayList<Recourse> recoursesInTheGroup) { // если будем просто передавать, то это и будет являться структурой типо массив ресурсов.
        this.recoursesInTheGroup = new ArrayList<Recourse>(recoursesInTheGroup);
    }

    public void setRecoursesInTheGroup(ArrayList<Recourse> recoursesInTheGroup) { this.recoursesInTheGroup = recoursesInTheGroup; }

    public void addRecourseInTheGroup(Recourse recourse) { recoursesInTheGroup.add(recourse); }

    public ArrayList<Recourse>  getRecoursesInTheGroup() { return this.recoursesInTheGroup; }
}
