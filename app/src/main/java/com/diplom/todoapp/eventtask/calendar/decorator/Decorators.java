package com.diplom.todoapp.eventtask.decorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class Decorators {
    private ArrayList<TaskDayDecorator> decorators = new ArrayList<>();
    public ArrayList<TaskDayDecorator> getDecorators(){
        return decorators;
    }
    public TaskDayDecorator getLast(){
        return decorators.get(decorators.size() - 1);
    }
    public TaskDayDecorator getDecorator(int index){
        return decorators.get(index);
    }
    public void addDecorator(TaskDayDecorator decorator){
        decorators.add(decorator);
    }
    public void removeDecorator(TaskDayDecorator decorator){
        decorators.remove(decorator);
    }

    public TaskDayDecorator search(CalendarDay day){
        TaskDayDecorator searchedDecorator = null;
        for(TaskDayDecorator decorator: decorators){
            if(decorator.getDay().getDay() == day.getDay() &&
                    decorator.getDay().getMonth() == day.getMonth() &&
                    decorator.getDay().getYear() == day.getYear()){
                searchedDecorator = decorator;
                break;
            }
        }
        return searchedDecorator;
    }


}
