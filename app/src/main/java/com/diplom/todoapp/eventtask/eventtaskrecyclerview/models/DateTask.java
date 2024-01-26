package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class DateTask extends AbstractTask{
    public String place;
    public Date dateEnd;

    public DateTask(){
        super("", "", "", false, new Date(), "", "", "");
        this.place = "";
        this.dateEnd = new Date();
    }

    public  DateTask(@NonNull String id, @NonNull String title,@NonNull String place,
                     @NonNull String describe, boolean allDayFlag,
                     @NonNull Date dateStart, @NonNull Date dateEnd, @NonNull String priority, @NonNull String reminder,
                     @NonNull String succsessFlag){
        super(id, title, describe,
                allDayFlag, dateStart,
                priority, reminder, succsessFlag);
        this.place = place;
        this.dateEnd = super.safeDateAfterInit(dateEnd, dateStart);
    }
    public void setTask(@NonNull String id, @NonNull String title,@NonNull String place,
                  @NonNull String describe, boolean allDayFlag,
                  @NonNull Date dateStart, @NonNull Date dateEnd, @NonNull String priority, @NonNull String reminder,
                        @NonNull String succsessFlag){
        super.setTask(id, title, describe, allDayFlag, dateStart, priority, reminder, succsessFlag);
        this.place = place;
        this.dateEnd = super.safeDateAfterInit(dateEnd, dateStart);
    }
}
