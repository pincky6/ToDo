package com.diplom.todoapp.eventtask;


import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.firebase.InitExpression;

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
    public boolean isEmpty(){
        return taskList.isEmpty();
    }
}
