package com.diplom.todoapp.eventtask.calendar.decorator;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.utils.CalendarUtil;
import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class RepeatDecorator extends AbstractDecorator{
    AbstractTask task;
    public RepeatDecorator(AbstractTask task, HashSet<Integer> colors) {
        super(colors);
        this.task = task;
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if(task.repeat.equals("Every Day")){
            Date dateCalendar = CalendarUtil.getDate(day);
            return task.dateStart.before(dateCalendar);
        }
        Calendar taskDay = CalendarUtil.getCalendar(task.dateStart);
        Calendar dayCalendar = CalendarUtil.getCalendar(day);
        if(task.repeat.equals(("Every Week"))){
            return dayCalendar.get(Calendar.DAY_OF_WEEK) == taskDay.get(Calendar.DAY_OF_WEEK);
        }
        return dayCalendar.get(Calendar.DAY_OF_YEAR) == taskDay.get(Calendar.DAY_OF_YEAR);
    }
    @Override
    public void decorate(DayViewFacade view) {
        colors.clear();
        colors.add(PriorityUtil.getPriorityColor(task.priority));
        view.addSpan(new MultipleDotSpan(5, colors));
    }
}
