package com.diplom.todoapp.eventtask.eventtaskrecyclerview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.AbstractTaskHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.TaskHolderFactory;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.TaskType;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<AbstractTaskHolder> {
    private ArrayList<AbstractTask> taskList;
    public TaskAdapter(ArrayList<AbstractTask> taskList){
        this.taskList = taskList;
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
        holder.bind(taskList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
