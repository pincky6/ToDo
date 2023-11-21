package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public abstract class AbstractTask implements Serializable{
    public String id;
    public Date createDate;
    public String title;
    public String describe;
    public boolean allDayFlag;
    public Date dateStart;
    public String priority;
    public String reminder;
    public  AbstractTask(@NonNull String id, @NonNull Date createDate, @NonNull String title, @NonNull String describe,
                        boolean allDayFlag, @NonNull Date dateStart, @NonNull Priority priority, @NonNull Reminders reminder){
        this.id = id;
        this.createDate = createDate;
        this.title = title;
        this.describe = describe;
        this.allDayFlag = allDayFlag;
        this.dateStart = safeDateAfterInit(dateStart, createDate);
        this.priority = priority.name();
        this.reminder = reminder.name();
    }
    public  AbstractTask(@NonNull String id, @NonNull Date createDate, @NonNull String title, @NonNull String describe,
                         boolean allDayFlag, @NonNull Date dateStart, @NonNull String priority, @NonNull String reminder){
        this.id = id;
        this.createDate = createDate;
        this.title = title;
        this.describe = describe;
        this.allDayFlag = allDayFlag;
        this.dateStart = safeDateAfterInit(dateStart, createDate);
        this.priority = priority;
        this.reminder = reminder;
    }
    public void setTask(@NonNull String id, @NonNull Date createDate, @NonNull String title, @NonNull String describe,
                        boolean allDayFlag, @NonNull Date dateStart,  @NonNull String priority, @NonNull String reminder){
        this.id = id;
        this.createDate = createDate;
        this.title = title;
        this.describe = describe;
        this.allDayFlag = allDayFlag;
        this.dateStart = safeDateAfterInit(dateStart, createDate);
        this.priority = priority;
        this.reminder = reminder;
    }
    protected Date safeDateAfterInit(Date initDate, Date stateDate){
        if(stateDate.compareTo(initDate) <= 0){
            return initDate;
        }
        else {
            throw new IllegalArgumentException("wrong date input");
        }
    }
    private void writeObject(ObjectOutputStream out) throws IOException{
        out.defaultWriteObject();
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
    }



}
