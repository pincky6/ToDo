package com.diplom.todoapp.utils;

import androidx.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarUtil {
    public static CalendarDay getCalendarDay(@NonNull Date date){
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String[] taskDayFormat = format.format(date).split("\\.");
        return CalendarDay.from(Integer.parseInt(taskDayFormat[2]),
                Integer.parseInt(taskDayFormat[1]),
                Integer.parseInt(taskDayFormat[0]));
    }
    public static boolean compareCalendarDays(CalendarDay lhs, CalendarDay rhs){
        if(lhs == null || rhs == null) return false;
        return lhs.getDay() == rhs.getDay() &&
                lhs.getMonth() == rhs.getMonth() &&
                lhs.getYear() == rhs.getYear();
    }
    public static boolean compareCalendarMonths(CalendarDay lhs, CalendarDay rhs){
        if(lhs == null || rhs == null) return false;
        return lhs.getMonth() == rhs.getMonth() &&
                lhs.getYear() == rhs.getYear();
    }
}
