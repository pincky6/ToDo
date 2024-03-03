package com.diplom.todoapp.eventtask.calendar.decorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class Decorators {
    private ArrayList<AbstractDecorator> decorators = new ArrayList<>();
    public ArrayList<AbstractDecorator> getDecorators(){
        return decorators;
    }
    public AbstractDecorator getLast(){
        return decorators.get(decorators.size() - 1);
    }
    public AbstractDecorator getDecorator(int index){
        return decorators.get(index);
    }
    public void addDecorator(AbstractDecorator decorator){
        decorators.add(decorator);
    }
    public void removeDecorator(AbstractDecorator decorator){
        decorators.remove(decorator);
    }

    public TaskDayDecorator search(CalendarDay day){
        TaskDayDecorator searchedDecorator = null;
        for(AbstractDecorator decorator: decorators){
            if(decorator instanceof TaskDayDecorator) {
                TaskDayDecorator dayDecorator = (TaskDayDecorator) decorator;
                if (dayDecorator.getDay().getDay() == day.getDay() &&
                        dayDecorator.getDay().getMonth() == day.getMonth() &&
                        dayDecorator.getDay().getYear() == day.getYear()) {
                    searchedDecorator = dayDecorator;
                    break;
                }
            }
        }
        return searchedDecorator;
    }


}
