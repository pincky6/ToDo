package com.diplom.todoapp.eventtask;


import com.diplom.todoapp.eventtask.CalendarSingletone;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.firebase.InitExpression;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Objects;

public class TaskViewModel extends ViewModel {
    public ArrayList<AbstractTask> taskList;
    private FirebaseRepository firebase;
    public TaskViewModel(){
        firebase = FirebaseRepository.getInstance();
        taskList = new ArrayList<>();
    }
    public void loadFirebase(RecyclerView recyclerView, InitExpression initLambda){
        firebase.readAllTasks(taskList, recyclerView, initLambda);
    }
    public AbstractTask getFromId(String id){
        for(AbstractTask abstractTask: taskList){
            if(Objects.equals(abstractTask.id, id)) {
                return abstractTask;
            }
        }
        throw new IllegalArgumentException("wrong id");
    }
    public void remove(String id){
        for(AbstractTask abstractTask: taskList){
            if(Objects.equals(abstractTask.id, id)) {
                taskList.remove(abstractTask);
                return;
            }
        }
    }
    public ArrayList<AbstractTask> filterForDay(CalendarDay day){
        ArrayList<AbstractTask> tasks = new ArrayList<>();
        CalendarSingletone calendarSingletone = CalendarSingletone.initialize(taskList);
        for(AbstractTask task: taskList){
            CalendarDay taskDay = calendarSingletone.getCalendarDay(task.dateStart);
            if(calendarSingletone.compareCalendarDays(taskDay, day)){
                tasks.add(task);
            }
        }
        return tasks;
    }
    public ArrayList<AbstractTask> filterForMonth(CalendarDay day){
        ArrayList<AbstractTask> tasks = new ArrayList<>();
        CalendarSingletone calendarSingletone = CalendarSingletone.initialize(taskList);
        for (AbstractTask task : taskList) {
            CalendarDay taskDay = calendarSingletone.getCalendarDay(task.dateStart);
            if (day.getMonth() == taskDay.getMonth() &&
                    day.getYear() == taskDay.getYear()) {
                tasks.add(task);
            }
        }
        return tasks;
    }
    public boolean isEmpty(){
        return taskList.isEmpty();
    }
}
