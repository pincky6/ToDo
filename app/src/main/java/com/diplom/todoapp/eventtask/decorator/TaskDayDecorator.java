package com.diplom.todoapp.eventtask.decorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;

public class TaskDayDecorator implements DayViewDecorator {

    private final CalendarDay day;
    private final HashSet<Integer> colors;
    public TaskDayDecorator(CalendarDay day, HashSet<Integer> colors) {
        this.day = day;
        this.colors = colors;
    }
    public CalendarDay getDay(){
        return day;
    }
    public HashSet<Integer> getColors(){
        return colors;
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return this.day.getDay() == day.getDay() &&
                this.day.getMonth() == day.getMonth() &&
                this.day.getYear() == day.getYear();
    }
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new MultipleDotSpan(5, colors));
    }
}