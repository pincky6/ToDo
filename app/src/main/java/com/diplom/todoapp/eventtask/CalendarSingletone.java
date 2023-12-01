package com.diplom.todoapp.eventtask;

import androidx.annotation.NonNull;

import com.diplom.todoapp.eventtask.decorator.Decorators;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class CalendarSingletone {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private HashMap<CalendarDay, HashSet<Integer>> dayTaskCalendarColors = new HashMap<>();
    private static CalendarSingletone calendarSingletone;
    private Decorators decorators;
    public Decorators getDecorators(){
        return decorators;
    }

    private CalendarSingletone(ArrayList<AbstractTask> tasks){
        decorators = new Decorators();
        for(AbstractTask task: tasks) {
            HashSet<Integer> prioritiesColor = new HashSet<>();
            CalendarDay day = getCalendarDay(task.dateStart);
            if(dayTaskCalendarColors.containsKey(day)) continue;
            for(AbstractTask taskPr: tasks){
                CalendarDay taskPrDay= getCalendarDay(taskPr.dateStart);
                if(taskPrDay.getYear() == day.getYear() &&
                        taskPrDay.getMonth() == day.getMonth() &&
                        taskPrDay.getDay() == day.getDay()) {
                    int color = PriorityUtil.getPriorityColor(PriorityUtil.getPriorityEnum(taskPr.priority));
                    prioritiesColor.add(color);
                }
            }
            dayTaskCalendarColors.put(day, prioritiesColor);
        }
    }

    public static CalendarSingletone initialize(@NonNull ArrayList<AbstractTask> tasks){
        if(calendarSingletone == null){
            calendarSingletone = new CalendarSingletone(tasks);
        }
        return calendarSingletone;
    }
    public static CalendarDay getCalendarDay(@NonNull Date date){
        String[] taskDayFormat = format.format(date).split("\\.");
        return CalendarDay.from(Integer.parseInt(taskDayFormat[2]),
                Integer.parseInt(taskDayFormat[1]),
                Integer.parseInt(taskDayFormat[0]));
    }
    public static boolean compareCalendarDays(CalendarDay lhs, CalendarDay rhs){
        return lhs.getDay() == rhs.getDay() &&
                lhs.getMonth() == rhs.getMonth() &&
                lhs.getYear() == rhs.getYear();
    }
    public HashMap<CalendarDay, HashSet<Integer>> getDayTaskCalendarColors(){
        return dayTaskCalendarColors;
    }
    public void removeFromDateTaskCalendarColors(CalendarDay day, int color){
        if(!dayTaskCalendarColors.containsKey(day)) return;

        if(dayTaskCalendarColors.get(day).size() == 1) {
            dayTaskCalendarColors.remove(day);
        }else {
            dayTaskCalendarColors.get(day).removeIf((dayColor)-> color == dayColor);
        }
    }
    public  boolean containsKey(CalendarDay day){
        return dayTaskCalendarColors.containsKey(day);
    }
    public HashSet<Integer> get(CalendarDay day){
        return dayTaskCalendarColors.get(day);
    }
    public void put(CalendarDay day, HashSet<Integer> colors){
        dayTaskCalendarColors.put(day, colors);
    }

    public SimpleDateFormat getFormat(){
        return format;
    }
}
