package com.diplom.todoapp.eventtask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Priority;
import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

enum TASK_MASK{
    TASK(1),
    DATE_TASK(2),
    LOW_PRIORITY(4),
    MIDDLE_PRIORITY(8),
    HIGH_PRIORITY(16);
    private final int i;
    TASK_MASK(int i) {
        this.i = i;
    }
    public int get(){
        return i;
    }
}

public class TaskFilter {
    private int mask = 0;
    public TaskFilter(int mask){
        this.mask = mask;
    }
    public void setMask(int mask){
        this.mask = mask;
    }
    public int getMask(){
        return mask;
    }
    public ArrayList<AbstractTask> filter(@NonNull ArrayList<AbstractTask> list, @Nullable CalendarDay date, @NonNull int calendarMonth){
        if(mask == 0 && date == null) return list;
        return (ArrayList<AbstractTask>) list.stream().filter(task -> {
            String type = task.id.split("-")[0];
            if(date != null && !CalendarSingletone
                    .compareCalendarDays(date, CalendarSingletone.getCalendarDay(task.dateStart))){
                return false;
            }
            if(date != null && mask == 0 &&
               CalendarSingletone.compareCalendarDays(date, CalendarSingletone.getCalendarDay(task.dateStart))){
                return true;
            }
            return getTypeRes(type) && getPriorityRes(task.priority) &&
                   (CalendarSingletone.getCalendarDay(task.dateStart).getMonth() == calendarMonth);
        }).collect(Collectors.toList());
    }

    public int getTypeMask(@NonNull String type){
        if(type.equals("DateTask")) return TASK_MASK.DATE_TASK.get();
        return TASK_MASK.TASK.get();
    }
    public boolean getTypeRes(@NonNull String type){
        int typeMask = (mask & TASK_MASK.TASK.get()) |
                       (mask & TASK_MASK.DATE_TASK.get());
        if(typeMask == 0 || typeMask == 3){
            return true;
        }
        return !((typeMask & getTypeMask(type)) == 0);
    }
    public boolean getPriorityRes(@NonNull String priority){
        int typeMask = (mask & TASK_MASK.LOW_PRIORITY.get()) |
                       (mask & TASK_MASK.MIDDLE_PRIORITY.get()) |
                       (mask & TASK_MASK.HIGH_PRIORITY.get());
        if(typeMask == 0 || typeMask == 28) return true;
        return (mask & PriorityUtil.getPriorityEnum(priority).getPriority()) != 0;
    }
    public ArrayList<AbstractTask> filterByDate(@NonNull ArrayList<AbstractTask> list, @Nullable Date date){
        if (date == null) return list;
       return (ArrayList<AbstractTask>) list.stream().filter(task -> {
            CalendarDay searchDay = CalendarSingletone.getCalendarDay(date);
            CalendarDay taskDay = CalendarSingletone.getCalendarDay(task.dateStart);
            return CalendarSingletone.compareCalendarDays(searchDay, taskDay);
        }).collect(Collectors.toList());
    }
    public ArrayList<AbstractTask> filterByTitle(@NonNull ArrayList<AbstractTask> list, @Nullable String title){
        if(title == null) return list;
        return (ArrayList<AbstractTask>) list.stream().filter(task -> task.title.contains(title))
                .collect(Collectors.toList());
    }
}
