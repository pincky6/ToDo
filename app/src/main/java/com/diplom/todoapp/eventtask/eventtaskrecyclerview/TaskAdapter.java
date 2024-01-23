package com.diplom.todoapp.eventtask.eventtaskrecyclerview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.AbstractTaskHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.TaskHolderFactory;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.TaskType;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.eventtask.listeners.RemoveListener;
import com.diplom.todoapp.eventtask.listeners.SetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<AbstractTaskHolder> {
    private ArrayList<AbstractTask> taskList;
    private TaskListener listener;
    private RemoveListener removeListener;
    private SetSuccsessListener setSuccsessListener;
    public TaskAdapter(ArrayList<AbstractTask> taskList,
                       TaskListener listener,
                       RemoveListener removeListener,
                       SetSuccsessListener setSuccsessListener){
        this.taskList = taskList;
        this.listener = listener;
        this.removeListener = removeListener;
        this.setSuccsessListener = setSuccsessListener;
    }
    @Override
    public int getItemViewType(int position) {
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
        holder.bind(taskList.get(position), listener, removeListener, setSuccsessListener);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void resetTaskList(ArrayList<AbstractTask> taskList){
        this.taskList = taskList;
    }
}
