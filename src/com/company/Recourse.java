package com.company;
import java.util.ArrayList;

public class Recourse {
    private int schedule; // здесь надо ещё подумать, ведь у каждой машины не просто время входа в систему, а четкий график работы
    private int nowInWork; // к какому такту будет завершена работа
    // но это не логично, потому что тогда если мы такт зададим как месяц например, то он будт простаивать до новго месяца
    // может тогда как время

    public Recourse() {}

    public Recourse(int schedule, int nowInWork)
    {
        this.schedule = schedule;
        this.nowInWork = nowInWork;
    }

    public Recourse(int schedule)
    {
        this(schedule, 0);
    }

    public int getSchedule() { return this.schedule; }

    public int getNowInWork() { return this.nowInWork; }

    public void setSchedule(int schedule) { this.schedule = schedule; }

    public void setNowInWork(int nowInWork) { this.nowInWork = nowInWork; }
}
