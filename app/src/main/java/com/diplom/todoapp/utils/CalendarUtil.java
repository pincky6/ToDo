package com.diplom.todoapp.utils;

import androidx.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
    public static String getMonthStringFromString(@NonNull Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (calendar.get(Calendar.MONTH)) {
            case 0:
                return "Январь";
            case 1:
                return "Февраль";
            case 2:
                return "Март";
            case 3:
                return "Апрель";
            case 4:
                return "Май";
            case 5:
                return "Июнь";
            case 6:
                return "Июль";
            case 7:
                return "Август";
            case 8:
                return "Сентябрь";
            case 9:
                return "Октябрь";
            case 10:
                return "Ноябрь";
            case 11:
                return "Декабрь";

        }
        return "Январь";
    }
    public static CalendarDay getCalendarDay(@NonNull Date date){
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String[] taskDayFormat = format.format(date).split("\\.");
        return CalendarDay.from(Integer.parseInt(taskDayFormat[2]),
                Integer.parseInt(taskDayFormat[1]),
                Integer.parseInt(taskDayFormat[0]));
    }

    public static Calendar getCalendar(@NonNull Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        return calendar;
    }
    public static Date getDate(@NonNull CalendarDay date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth() - 1, date.getDay());
        Date res = new Date();
        res.setTime(calendar.getTimeInMillis());
        return res;
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
