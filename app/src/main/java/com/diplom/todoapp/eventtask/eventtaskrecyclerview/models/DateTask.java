package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class DateTask extends AbstractTask{
    public String place;
    public Date dateEnd;
    public ArrayList<Subtask> subtasks;

    public DateTask(){
        super("", "", "", false, new Date(), "", "", "", "", "");
        this.place = "";
        this.dateEnd = new Date();
        subtasks = new ArrayList<>();
    }

    public  DateTask(@NonNull String id, @NonNull String title,@NonNull String place,
                     @NonNull String describe, boolean allDayFlag,
                     @NonNull Date dateStart, @NonNull Date dateEnd, @NonNull String priority, @NonNull String reminder,
                     @NonNull String succsessFlag, @NonNull String category, @NonNull String repeat){
        super(id, title, describe,
                allDayFlag, dateStart,
                priority, reminder, succsessFlag, repeat, category);
        this.place = place;
        this.dateEnd = super.safeDateAfterInit(dateEnd, dateStart);
        this.category = category;
    }
    public void setTask(@NonNull String id, @NonNull String title,@NonNull String place,
                  @NonNull String describe, boolean allDayFlag,
                  @NonNull Date dateStart, @NonNull Date dateEnd, @NonNull String priority, @NonNull String reminder,
                        @NonNull String succsessFlag, @NonNull String category, @NonNull String repeat){
        super.setTask(id, title, describe, allDayFlag, dateStart, priority, reminder, succsessFlag, repeat, category);
        this.place = place;
        this.dateEnd = super.safeDateAfterInit(dateEnd, dateStart);
        this.category = category;
    }
}
