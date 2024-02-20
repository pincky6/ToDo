package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class Task extends AbstractTask {
    public Task(){
        super("", "", "", false, new Date(), "", "", "", "");
    }

    public  Task(@NonNull String id, @NonNull String title, @NonNull String describe,
                 boolean allDayFlag, @NonNull Date dateStart, @NonNull String priority, @NonNull String reminders,
                 @NonNull String successFlag, @NonNull String repeat){
        super(id, title, describe,
                allDayFlag, dateStart,
                priority, reminders, successFlag, repeat);
    }
}
