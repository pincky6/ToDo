package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.listeners.RemoveListener;
import com.diplom.todoapp.eventtask.listeners.SetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

public abstract class AbstractTaskHolder extends RecyclerView.ViewHolder {
    public AbstractTaskHolder(@NonNull View itemView) {
        super(itemView);
    }
    public abstract void bind(AbstractTask abstractTask, TaskListener listener, RemoveListener removeListener, SetSuccsessListener setSuccsessListener);

}
