package com.company;

public class Recourse {
    private int schedule; // здесь надо ещё подумать, ведь у каждой машины не просто время входа в систему, а четкий график работы
    private boolean nowInWork;

    public Recourse() {}

    public Recourse(int schedule, boolean nowInWork)
    {
        this.schedule = schedule;
        this.nowInWork = nowInWork;
    }

    public Recourse(int schedule)
    {
        this(schedule, false);
    }

    public int getSchedule() { return this.schedule; }

    public boolean getNowInWork() { return this.nowInWork; }

    public void setSchedule(int schedule) { this.schedule = schedule; }

    public void setNowInWork(boolean nowInWork) { this.nowInWork = nowInWork; }
}
