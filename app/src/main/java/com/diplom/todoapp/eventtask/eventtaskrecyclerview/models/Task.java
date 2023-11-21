package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class Task extends AbstractTask {
    public Task(){
        super("", new Date(), "", "", false, new Date(), "", "");
    }
    public  Task(@NonNull String id, @NonNull Date createDate, @NonNull String title, @NonNull String describe,
                         boolean allDayFlag, @NonNull Date dateStart, @NonNull Priority priority, @NonNull Reminders reminders){
        super(id, createDate, title, describe,
              allDayFlag, dateStart,
              priority, reminders);
    }
    public  Task(@NonNull String id, @NonNull Date createDate, @NonNull String title, @NonNull String describe,
                 boolean allDayFlag, @NonNull Date dateStart, @NonNull String priority, @NonNull String reminders){
        super(id, createDate, title, describe,
                allDayFlag, dateStart,
                priority, reminders);
    }
}
