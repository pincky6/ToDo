package com.diplom.todoapp.utils;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Reminders;

public class ReminderUtil {
    public static Reminders getReminderEnum(String reminder){
        switch (reminder){
            case "1 day before":
                return Reminders.DAY_1_BEFORE;
            case "1 hour before":
                return Reminders.HOUR_1_BEFORE;
            case "5 minutes before":
                return Reminders.MINUTES_5_BEFORE;
            case "Dont\'t remind":
                return Reminders.DONT_REMIND;
        }
        return Reminders.DONT_REMIND;
    }
    public static int getReminderIndex(Reminders reminder){
        switch (reminder){
            case DAY_1_BEFORE:
                return 0;
            case HOUR_1_BEFORE:
                return 1;
            case MINUTES_5_BEFORE:
                return 2;
            case DONT_REMIND:
                return 3;
        }
        return 1;
    }
}
