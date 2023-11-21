package com.diplom.todoapp.eventtask;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Priority;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Reminders;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class TaskViewModel extends ViewModel {
    public ArrayList<AbstractTask> taskList;
    private FirebaseRepository firebase;
    public TaskViewModel(){
        firebase = FirebaseRepository.getInstance();
        taskList = new ArrayList<>();
//        Priority[] priorities = {Priority.LOW, Priority.MEDIUM, Priority.HIGH};
//        Reminders[] reminders = {Reminders.DONT_REMIND, Reminders.MINUTES_5_BEFORE, Reminders.DAY_1_BEFORE};
//        for(int i = 0; i < 100; i++){
//            Date date = new Date();
//            if(i % 2 == 0)
//            taskList.add(new Task(date, "Task #" + i,
//                          "this is my task",
//                    true, date, priorities[i % 3], reminders[i % 3]));
//            else
//                taskList.add(new DateTask(date,
//                        "Task #" + i, "dom",
//                        "this is my date task",
//                        false, date,
//                        date,
//                        priorities[i % 3], reminders[i % 3]));
//        }
    }
    public void loadFirebase(RecyclerView recyclerView){
        firebase.readAllTasks(taskList, recyclerView);
    }
    public void add(AbstractTask abstractTask){
        firebase.addTask(abstractTask.id, abstractTask);
        taskList.add(abstractTask);
    }
    public void remove(String id){
        for(AbstractTask abstractTask: taskList){
            if(abstractTask.id == id) {
                taskList.remove(abstractTask);
                return;
            }
        }
    }
    public boolean isEmpty(){
        return taskList.isEmpty();
    }
}
