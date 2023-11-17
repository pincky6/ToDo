package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class DateTask extends AbstractTask{
    public String place;
    public Date dateEnd;

    private void setTask(@NonNull Date createDate, @NonNull String title,@NonNull String place,
                  @NonNull String describe, boolean allDayFlag,
                  @NonNull Date dateStart, @NonNull Date dateEnd,
                  @NonNull Date dateBeforeStart, @NonNull Priority priority){
        super.setTask(createDate, title, describe, allDayFlag, dateStart, dateBeforeStart, priority);
        this.place = place;
        super.safeDateAfterInit(dateStart, dateEnd, this.dateEnd);
    }
}
