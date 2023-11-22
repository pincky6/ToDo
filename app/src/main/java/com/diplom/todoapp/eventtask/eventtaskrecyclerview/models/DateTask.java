package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class DateTask extends AbstractTask{
    public String place;
    public Date dateEnd;

    public DateTask(){
        super("", new Date(), "", "", false, new Date(), "", "");
        this.place = "";
        this.dateEnd = new Date();
    }

    public  DateTask(@NonNull String id, @NonNull Date createDate, @NonNull String title,@NonNull String place,
                     @NonNull String describe, boolean allDayFlag,
                     @NonNull Date dateStart, @NonNull Date dateEnd, @NonNull String priority, @NonNull String reminder){
        super(id, createDate, title, describe,
                allDayFlag, dateStart,
                priority, reminder);
        this.place = place;
        this.dateEnd = super.safeDateAfterInit(dateEnd, dateStart);
    }
    public void setTask(@NonNull String id, @NonNull Date createDate, @NonNull String title,@NonNull String place,
                  @NonNull String describe, boolean allDayFlag,
                  @NonNull Date dateStart, @NonNull Date dateEnd, @NonNull String priority, @NonNull String reminder){
        super.setTask(id, createDate, title, describe, allDayFlag, dateStart, priority, reminder);
        this.place = place;
        this.dateEnd = super.safeDateAfterInit(dateEnd, dateStart);
    }
}
