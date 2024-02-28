package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import com.diplom.todoapp.utils.SuccsessFlagUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public abstract class AbstractTask implements Serializable{
    public String id;
    public String title;
    public String describe;
    public boolean allDayFlag;
    public Date dateStart;
    public String priority;
    public String reminder;
    public String succsessFlag;
    public String repeat;
    public String category;
    public  AbstractTask(@NonNull String id, @NonNull String title, @NonNull String describe,
                        boolean allDayFlag, @NonNull Date dateStart, @NonNull Priority priority, @NonNull Reminders reminder,
                         @NonNull SuccsessFlag succsessFlag, @NonNull String repeat, @NonNull String category){
        this.id = id;
        this.title = title;
        this.describe = describe;
        this.allDayFlag = allDayFlag;
        this.dateStart = dateStart;
        this.priority = priority.name();
        this.reminder = reminder.name();
        this.succsessFlag = SuccsessFlagUtil.getStringFromFlag(succsessFlag);
        this.repeat = repeat;
        this.category = category;
    }
    public  AbstractTask(@NonNull String id,  @NonNull String title, @NonNull String describe,
                         boolean allDayFlag, @NonNull Date dateStart, @NonNull String priority, @NonNull String reminder,
                         @NonNull String succsessFlag, @NonNull String repeat, @NonNull String category){
        this.id = id;
        this.title = title;
        this.describe = describe;
        this.allDayFlag = allDayFlag;
        this.dateStart = dateStart;
        this.priority = priority;
        this.reminder = reminder;
        this.succsessFlag = succsessFlag;
        this.repeat = repeat;
        this.category = category;
    }
    public void setTask(@NonNull String id, @NonNull String title, @NonNull String describe,
                        boolean allDayFlag, @NonNull Date dateStart,  @NonNull String priority, @NonNull String reminder,
                        @NonNull String succsessFlag, @NonNull String repeat, @NonNull String category){
        this.id = id;
        this.title = title;
        this.describe = describe;
        this.allDayFlag = allDayFlag;
        this.dateStart = dateStart;
        this.priority = priority;
        this.reminder = reminder;
        this.succsessFlag = succsessFlag;
        this.repeat = repeat;
        this.category = category;
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
