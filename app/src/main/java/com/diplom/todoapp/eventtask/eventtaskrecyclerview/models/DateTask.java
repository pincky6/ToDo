package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class DateTask extends AbstractTask{
    public String place;
    public Date dateEnd;

    public  DateTask(@NonNull Date createDate, @NonNull String title,@NonNull String place,
                     @NonNull String describe, boolean allDayFlag,
                     @NonNull Date dateStart, @NonNull Date dateEnd,
                     @NonNull Date dateBeforeStart, @NonNull Priority priority, @NonNull Reminders reminder){
        super(createDate, title, describe,
                allDayFlag, dateStart, dateBeforeStart,
                priority, reminder);
        this.place = place;
        this.dateEnd = super.safeDateAfterInit(dateStart, dateEnd, this.dateEnd);
    }
    public void setTask(@NonNull Date createDate, @NonNull String title,@NonNull String place,
                  @NonNull String describe, boolean allDayFlag,
                  @NonNull Date dateStart, @NonNull Date dateEnd,
                  @NonNull Date dateBeforeStart, @NonNull Priority priority, @NonNull Reminders reminder){
        super.setTask(createDate, title, describe, allDayFlag, dateStart, dateBeforeStart, priority, reminder);
        this.place = place;
        this.dateEnd = super.safeDateAfterInit(dateStart, dateEnd, this.dateEnd);
    }
}
