package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class DateTask extends AbstractTask{
    public String place;
    public String category;
    public Date dateEnd;

    public DateTask(){
        super("", "", "", false, new Date(), "", "", "");
        this.place = "";
        this.dateEnd = new Date();
    }

    public  DateTask(@NonNull String id, @NonNull String title,@NonNull String place,
                     @NonNull String describe, boolean allDayFlag,
                     @NonNull Date dateStart, @NonNull Date dateEnd, @NonNull String priority, @NonNull String reminder,
                     @NonNull String succsessFlag, @NonNull String category){
        super(id, title, describe,
                allDayFlag, dateStart,
                priority, reminder, succsessFlag);
        this.place = place;
        this.dateEnd = super.safeDateAfterInit(dateEnd, dateStart);
        this.category = category;
    }
    public void setTask(@NonNull String id, @NonNull String title,@NonNull String place,
                  @NonNull String describe, boolean allDayFlag,
                  @NonNull Date dateStart, @NonNull Date dateEnd, @NonNull String priority, @NonNull String reminder,
                        @NonNull String succsessFlag, @NonNull String category){
        super.setTask(id, title, describe, allDayFlag, dateStart, priority, reminder, succsessFlag);
        this.place = place;
        this.dateEnd = super.safeDateAfterInit(dateEnd, dateStart);
        this.category = category;
    }
}
