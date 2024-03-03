package com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.AbstractTaskHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

import java.util.ArrayList;

public abstract class AbstractTaskAdapter extends RecyclerView.Adapter<AbstractTaskHolder>{

    public abstract void onBindViewHolder(@NonNull AbstractTaskHolder holder, int position);

    public void resetTaskList(ArrayList<AbstractTask> taskList) {

    }
}
