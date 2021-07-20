package com.company;

public class Recourse {
    private int schedule; // здесь надо ещё подумать, ведь у каждой машины не просто время входа в систему, а четкий график работы
    private boolean nowInWork;

    public Recourse(){}
    public Recourse(int iSchedule, boolean bNowInWork)
    {
        schedule = iSchedule;
        nowInWork = bNowInWork;
    }

    public Recourse(int iSchedule)
    {
        this(iSchedule, false);
    }

    public int getSchedule(){ return schedule; }

    public boolean getNowInWork(){ return nowInWork; }

    public void setSchedule(int iSchedule) { schedule = iSchedule; }

    public void setNowInWork(boolean bNowInWork) { nowInWork = bNowInWork; }
}
