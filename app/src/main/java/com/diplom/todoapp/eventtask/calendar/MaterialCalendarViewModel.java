package com.diplom.todoapp.eventtask.calendar;

import androidx.lifecycle.ViewModel;
import com.diplom.todoapp.eventtask.calendar.decorator.Decorators;
import com.diplom.todoapp.eventtask.calendar.decorator.MaterialCalendarFragment;
import com.diplom.todoapp.eventtask.calendar.decorator.TaskDayDecorator;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.utils.CalendarUtil;
import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class MaterialCalendarViewModel extends ViewModel {
    private HashMap<CalendarDay, ArrayList<Integer>> dayTaskCalendarColors = null;
    private Decorators decorators = null;
    private CalendarDay selectedDay = null;
    public MaterialCalendarViewModel(){
        dayTaskCalendarColors = new HashMap<>();
        decorators = new Decorators();
    }
    public MaterialCalendarViewModel(ArrayList<AbstractTask> tasks){
        dayTaskCalendarColors = new HashMap<>();
        decorators = new Decorators();
        setTasksDecorators(tasks);
    }
    public HashMap<CalendarDay, ArrayList<Integer>> getDayTaskCalendarColors(){
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
        } else if(dayTaskCalendarColors.get(day).size() > 1) {
            ArrayList<Integer> colorsList = dayTaskCalendarColors.get(day);
            int index = colorsList.indexOf(color);
            colorsList.remove(index);
            dayTaskCalendarColors.put(day, colorsList);
            dayDecorator.setColors(convertToHashSetColors(colorsList)) ;
        }
        return dayDecorator;
    }
    public void addColorByDay(CalendarDay day, int color){
        get(day).add(color);
        decorators.search(day).setColors(convertToHashSetColors(get(day)));
    }
    public  boolean containsKey(CalendarDay day){
        return dayTaskCalendarColors.containsKey(day);
    }
    public ArrayList<Integer> get(CalendarDay day){
        return dayTaskCalendarColors.get(day);
    }
    public void put(CalendarDay day, ArrayList<Integer> colors){
        dayTaskCalendarColors.put(day, colors);
    }
    public ArrayList<TaskDayDecorator> getTaskDayDecoratorList(){
        return decorators.getDecorators();
    }
    public Decorators getDecorators(){
        return decorators;
    }
    private HashSet<Integer> convertToHashSetColors(ArrayList<Integer> list){
        return (HashSet<Integer>) list.stream().collect(Collectors.toSet());
    }
    public void setSelectedDay(CalendarDay calendarDay){
        selectedDay = calendarDay;
    }
    public CalendarDay getSelectedDay(){
        if(selectedDay == null) return CalendarUtil.getCalendarDay(new Date());
        return selectedDay;
    }
    public void setTasksDecorators(ArrayList<AbstractTask> tasks){

        for(AbstractTask task: tasks) {
            ArrayList<Integer> prioritiesColor = new ArrayList<>();
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
        for(Map.Entry<CalendarDay, ArrayList<Integer>> entry: dayTaskCalendarColors.entrySet()){
            decorators.addDecorator(new TaskDayDecorator(entry.getKey(),
                    convertToHashSetColors(entry.getValue())));
        }
    }
}
