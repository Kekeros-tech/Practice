package com.company;

public class Recourse {
    private int schedule; // здесь надо ещё подумать, ведь у каждой машины не просто время входа в систему, а четкий график работы
    private boolean now_in_work;

    public Recourse(int entrance_schedule, boolean entrance_now_in_work)
    {
        schedule = entrance_schedule;
        now_in_work = entrance_now_in_work;
    }

    public Recourse(int entrance_schedule)
    {
        this( entrance_schedule, false);
    }
}
