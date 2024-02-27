package com.diplom.todoapp.eventtask.calendar.decorator;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;

public class AbstractDecorator implements DayViewDecorator {
    protected HashSet<Integer> colors;
    public AbstractDecorator(HashSet<Integer> colors){
        this.colors = colors;
    }
    public HashSet<Integer> getColors(){
        return colors;
    }
    public void setColors(HashSet<Integer> colors){
        this.colors = colors;
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {

    }
}
