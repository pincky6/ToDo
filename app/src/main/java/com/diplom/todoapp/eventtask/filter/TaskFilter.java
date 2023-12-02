package com.diplom.todoapp.eventtask.filter;

import androidx.annotation.NonNull;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.utils.CalendarUtil;
import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class TaskFilter {
    private int mask = 0;
    private CalendarDay selectedDay = null;
    private int selectedMonth = CalendarUtil.getCalendarDay(new Date()).getMonth();
    public TaskFilter(int mask){
        this.mask = mask;
    }
    public int getMask(){
        return mask;
    }
    public void setMask(int mask){
        this.mask = mask;
    }
    public CalendarDay getSelectedDay(){
        return selectedDay;
    }
    public void setSelectedDay(CalendarDay selectedDay){
        this.selectedDay = selectedDay;
    }
    public int getSelectedMonth(){
        return selectedMonth;
    }
    public void setSelectedMonth(int selectedMonth){
        this.selectedMonth = selectedMonth;
    }
    public ArrayList<AbstractTask> filter(@NonNull ArrayList<AbstractTask> list){
        int currentMonth = CalendarUtil.getCalendarDay(new Date()).getMonth();
        if(mask == 0 && selectedDay == null && selectedMonth == currentMonth){
            return list;
        }
        return (ArrayList<AbstractTask>) list.stream().filter(task -> {
            if(selectedDay != null && !CalendarUtil
                    .compareCalendarDays(selectedDay, CalendarUtil.getCalendarDay(task.dateStart))){
                return false;
            }
            if(selectedDay != null && mask == 0 &&
                    CalendarUtil.compareCalendarDays(selectedDay, CalendarUtil.getCalendarDay(task.dateStart))){
                return true;
            }
            String type = task.id.split("-")[0];
            return getTypeRes(type) && getPriorityRes(task.priority) &&
                    (CalendarUtil.getCalendarDay(task.dateStart).getMonth() == selectedMonth);
        }).collect(Collectors.toList());
    }

    public int getTypeMask(String type){
        if(type.equals("DateTask")) return TASK_MASK.DATE_TASK.get();
        return TASK_MASK.TASK.get();
    }
    public boolean getTypeRes(String type){
        int typeMask = (mask & TASK_MASK.TASK.get()) |
                (mask & TASK_MASK.DATE_TASK.get());
        return (typeMask & getTypeMask(type)) != 0;
    }
    public boolean getPriorityRes(String priority){
        return (mask & PriorityUtil.getPriorityEnum(priority).getPriority()) != 0;
    }
}
