package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.Date;

public abstract class AbstractTask {
    public Date createDate;
    public String title;
    public String describe;
    public boolean allDayFlag;
    public Date dateStart;
    public Date dateBeforeStart;
    public Priority priority;
    public void setTask(@NonNull Date createDate, @NonNull String title, @NonNull String describe,
                        boolean allDayFlag, @NonNull Date dateStart,
                        @NonNull Date dateBeforeStart, @NonNull Priority priority){
        safeDateAfterInit(dateStart, createDate, this.createDate);
        this.title = title;
        this.describe = describe;
        this.allDayFlag = allDayFlag;
        this.dateStart = dateStart;
        safeDateAfterInit(dateStart, dateBeforeStart, this.dateBeforeStart);
        this.priority = priority;
    }
    protected void safeDateAfterInit(Date dateStart, Date initDate, Date destinationDate){
        if(dateStart.equals(initDate) || dateStart.after(initDate))
            destinationDate = initDate;
        else
            throw new IllegalArgumentException("wrong date input");
    }
}
