package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class Holiday extends AbstractTask {
    public Holiday(){
        super("", "", "", false, new Date(), "", "", "", "", "");
    }

    public Holiday(@NonNull String id, @NonNull String title, @NonNull String describe,
                   boolean allDayFlag, @NonNull Date dateStart, @NonNull String priority, @NonNull String reminders,
                   @NonNull String successFlag, @NonNull String repeat, @NonNull String category){
        super(id, title, describe,
                allDayFlag, dateStart,
                priority, reminders, successFlag, repeat, category);
    }
}
