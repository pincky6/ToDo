package com.diplom.todoapp.eventtask.calendar.decorator;

import androidx.lifecycle.ViewModel;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.utils.CalendarUtil;
import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MaterialCalendarViewModel extends ViewModel {
    private HashMap<CalendarDay, HashSet<Integer>> dayTaskCalendarColors = null;
    private Decorators decorators = null;
    public MaterialCalendarViewModel(ArrayList<AbstractTask> tasks){
        dayTaskCalendarColors = new HashMap<>();
        decorators = new Decorators();
        for(AbstractTask task: tasks) {
            HashSet<Integer> prioritiesColor = new HashSet<>();
            CalendarDay day = CalendarUtil.getCalendarDay(task.dateStart);
            if(dayTaskCalendarColors.containsKey(day)) continue;
            for(AbstractTask taskPr: tasks){
                CalendarDay taskPrDay= CalendarUtil.getCalendarDay(taskPr.dateStart);
                if(taskPrDay.getYear() == day.getYear() &&
                        taskPrDay.getMonth() == day.getMonth() &&
                        taskPrDay.getDay() == day.getDay()) {
                    int color = PriorityUtil.getPriorityColor(PriorityUtil.getPriorityEnum(taskPr.priority));
                    prioritiesColor.add(color);
                }
            }
            dayTaskCalendarColors.put(day, prioritiesColor);
        }
        for(Map.Entry<CalendarDay, HashSet<Integer>> entry: dayTaskCalendarColors.entrySet()){
            decorators.addDecorator(new TaskDayDecorator(entry.getKey(), entry.getValue()));
        }
    }
    public HashMap<CalendarDay, HashSet<Integer>> getDayTaskCalendarColors(){
        return dayTaskCalendarColors;
    }
    public TaskDayDecorator removeByColor(CalendarDay day, int color){
        if(!dayTaskCalendarColors.containsKey(day)) return null;
        TaskDayDecorator dayDecorator = decorators.search(day);
        if(dayTaskCalendarColors.get(day).size() == 1) {
            dayTaskCalendarColors.remove(day);
            if(dayDecorator != null) {
                decorators.removeDecorator(dayDecorator);
            }
        }else if(dayTaskCalendarColors.get(day).size() > 1){
            dayTaskCalendarColors.get(day).removeIf((dayColor)-> color == dayColor);
            dayDecorator.setColors(get(day)) ;
        }
        return dayDecorator;
    }
    public void addColorByDay(CalendarDay day, int color){
        get(day).add(color);
        decorators.search(day).setColors(get(day));
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
    public ArrayList<TaskDayDecorator> getTaskDayDecoratorList(){
        return decorators.getDecorators();
    }
    public Decorators getDecorators(){
        return decorators;
    }

}
