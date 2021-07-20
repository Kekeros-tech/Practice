package com.company;

public class Group {
    private Recourse[] recoursesInTheGroup;

    public Group() { //он ведь так по умолчанию формирует
        recoursesInTheGroup = new Recourse[0];
    }
    public Group(Recourse[] recoursesInTheGroup) { // если будем просто передавать, то это и будет являться структурой типо массив ресурсов.
        this.recoursesInTheGroup = recoursesInTheGroup;
    }

    public void setRecoursesInTheGroup(Recourse[] recoursesInTheGroup) { this.recoursesInTheGroup = recoursesInTheGroup; }

    public Recourse[] getRecoursesInTheGroup() { return this.recoursesInTheGroup; }
}
