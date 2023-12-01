package com.diplom.todoapp.eventtask.eventtaskrecyclerview;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.firebase.InitExpression;
import com.diplom.todoapp.utils.CalendarUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Objects;

public class TaskListViewModel extends ViewModel {
    public ArrayList<AbstractTask> taskList;
    private FirebaseRepository firebase;
    public TaskListViewModel(){
        firebase = FirebaseRepository.getInstance();
        taskList = new ArrayList<>();
    }
    public void loadFirebase(RecyclerView recyclerView, InitExpression initLambda){
        firebase.readAllTasks(taskList, recyclerView, initLambda);
    }
    public void addTask(AbstractTask abstractTask){
        firebase.addTask(abstractTask);
        taskList.add(abstractTask);
    }
    public void removeById(String id){
        firebase.removeTask(id);
        remove(id);
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
        for(AbstractTask task: taskList){
            CalendarDay taskDay = CalendarUtil.getCalendarDay(task.dateStart);
            if(CalendarUtil.compareCalendarDays(taskDay, day)){
                tasks.add(task);
            }
        }
        return tasks;
    }
    public ArrayList<AbstractTask> filterForMonth(CalendarDay day){
        ArrayList<AbstractTask> tasks = new ArrayList<>();
        for (AbstractTask task : taskList) {
            CalendarDay taskDay = CalendarUtil.getCalendarDay(task.dateStart);
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
