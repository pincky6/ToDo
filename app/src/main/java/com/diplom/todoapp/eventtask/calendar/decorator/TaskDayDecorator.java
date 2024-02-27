package com.diplom.todoapp.eventtask.calendar.decorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;


import java.util.HashSet;

public class TaskDayDecorator extends AbstractDecorator {

    private final CalendarDay day;
    public TaskDayDecorator(CalendarDay day, HashSet<Integer> colors) {
        super(colors);
        this.day = day;
    }
    public CalendarDay getDay(){
        return day;
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