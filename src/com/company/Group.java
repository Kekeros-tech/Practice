package com.company;

public class Group {
    private Recourse[] recourses_in_the_group;

    public Group() { //он ведь так по умолчанию формирует
        recourses_in_the_group = new Recourse[0];
    }
    public Group(Recourse[] input_recourses) { // если будем просто передавать, то это и будет являться структурой типо массив ресурсов.
        recourses_in_the_group = input_recourses;
    }
}
