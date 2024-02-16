package com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.AbstractTaskHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.TaskHolderFactory;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.TaskType;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.eventtask.listeners.OnSetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;

import java.util.ArrayList;

public class TaskAdapter extends AbstractTaskAdapter {
    private ArrayList<AbstractTask> taskList;
    private final TaskListener listener;
    private final OnRemoveListener removeListener;
    private final OnSetSuccsessListener onSetSuccsessListener;
    public TaskAdapter(ArrayList<AbstractTask> taskList,
                       TaskListener listener,
                       OnRemoveListener removeListener,
                       OnSetSuccsessListener onSetSuccsessListener){
        this.taskList = taskList;
        this.listener = listener;
        this.removeListener = removeListener;
        this.onSetSuccsessListener = onSetSuccsessListener;
    }
    @Override
    public int getItemViewType(int position) {
            if(taskList.size() == 0){
                return TaskType.EMPTY_TASK.ordinal();
            }
            if(taskList.get(position) instanceof Task){
                return TaskType.TASK.ordinal();
            }
            else {
                return TaskType.DATE_TASK.ordinal();
            }
    }

    @NonNull
    @Override
    public AbstractTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int taskType) {
        return TaskHolderFactory.produce(parent, taskType);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractTaskHolder holder, int position) {
        if(taskList.isEmpty()) return;
        holder.bind(taskList.get(position), listener, removeListener, onSetSuccsessListener);
    }

    @Override
    public int getItemCount() {
        return taskList.size() > 0 ? taskList.size() : 1;
    }

    @Override
    public void resetTaskList(ArrayList<AbstractTask> taskList){
        this.taskList = taskList;
    }
}
