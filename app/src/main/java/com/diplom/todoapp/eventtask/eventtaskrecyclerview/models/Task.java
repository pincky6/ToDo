package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class Task extends AbstractTask {
    public  Task(@NonNull Date createDate, @NonNull String title, @NonNull String describe,
                         boolean allDayFlag, @NonNull Date dateStart, @NonNull Priority priority, @NonNull Reminders reminders){
        super(createDate, title, describe,
              allDayFlag, dateStart,
              priority, reminders);
    }
}
