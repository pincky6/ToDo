package com.diplom.todoapp.eventtask.filter;

import androidx.annotation.NonNull;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Holiday;
import com.diplom.todoapp.utils.CalendarUtil;
import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class TaskFilter {
    private int mask = 0;
    private ArrayList<String> categories;
    private CalendarDay selectedDay = null;
    String selectedTitle = null;
    private int selectedMonth = CalendarUtil.getCalendarDay(new Date()).getMonth();
    public TaskFilter(int mask, ArrayList<String> categories){
        this.mask = mask;
        this.categories = categories;
    }
    public int getMask(){
        return mask;
    }
    public void setMask(int mask){
        this.mask = mask;
    }
    public ArrayList<String> getCategories(){
        return categories;
    }
    public void setCategories(ArrayList<String> categories){
        this.categories = categories;
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
    public String getSelectedTitle(){
        return selectedTitle;
    }
    public void setSelectedTitle(String selectedTitle){
        this.selectedTitle = selectedTitle;
    }
    public ArrayList<AbstractTask> filter(@NonNull ArrayList<AbstractTask> list){
        int currentMonth = CalendarUtil.getCalendarDay(new Date()).getMonth();
        if(mask == 0 && selectedDay == null && selectedMonth == currentMonth){
            return list;
        }
        return (ArrayList<AbstractTask>) list.stream().filter(task -> {
            if(selectedDay != null && (!checkRepeat(task) && !CalendarUtil
                    .compareCalendarDays(selectedDay, CalendarUtil.getCalendarDay(task.dateStart)))){
                return false;
            }
            if(selectedDay != null && mask == 0 &&
                    (checkRepeat(task) || CalendarUtil.compareCalendarDays(selectedDay, CalendarUtil.getCalendarDay(task.dateStart)))){
                return true;
            }
            String type = task.id.split("-")[0];
            return getTypeRes(type) && getPriorityRes(task.priority) &&
                    ((checkRepeat(task)||
                            CalendarUtil.getCalendarDay(task.dateStart).getMonth() == selectedMonth) );
        }).collect(Collectors.toList());
    }
    public ArrayList<AbstractTask> filterByTitle(@NonNull ArrayList<AbstractTask> list){
        if(selectedTitle == null) return list;
        return (ArrayList<AbstractTask>) list.stream().filter(abstractTask -> selectedTitle.equals(abstractTask.title))
                .collect(Collectors.toList());
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
    public boolean checkCategories(AbstractTask abstractTask){
        if(categories.isEmpty()) return true;
        if(abstractTask instanceof Holiday) return false;
        DateTask dateTask = (DateTask) abstractTask;
        return categories.contains(dateTask.category);
    }
    public boolean checkRepeat(AbstractTask abstractTask){
        if(abstractTask.repeat.equals("Don\'t Repeat") || abstractTask.repeat.isEmpty()){
            if(selectedDay != null) {
                Calendar selected = CalendarUtil.getCalendar(CalendarUtil.getDate(selectedDay));
                Calendar date = CalendarUtil.getCalendar(abstractTask.dateStart);
                return selected.equals(date);
            }
            return true;
        }
        if(abstractTask.repeat.equals("Every Day")){
            return true;
        }
        if(abstractTask.repeat.equals("Every week")){
            if(selectedDay == null) return true;
            Calendar selected = CalendarUtil.getCalendar(CalendarUtil.getDate(selectedDay));
            Calendar date = CalendarUtil.getCalendar(abstractTask.dateStart);
            return (date.get(Calendar.DAY_OF_WEEK) == selected.get(Calendar.DAY_OF_WEEK));
        }
        if(abstractTask.repeat.equals("Every year")){
            Calendar selected = CalendarUtil.getCalendar(CalendarUtil.getDate(selectedDay));
            Calendar date = CalendarUtil.getCalendar(abstractTask.dateStart);
            return (date.get(Calendar.DAY_OF_YEAR) == selected.get(Calendar.DAY_OF_YEAR));
        }
        return true;
    }
}
