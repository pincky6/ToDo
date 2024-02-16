package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.eventtask.listeners.OnSetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

public abstract class AbstractTaskHolder extends RecyclerView.ViewHolder {
    public AbstractTaskHolder(@NonNull View itemView) {
        super(itemView);
    }
    public abstract void bind(AbstractTask abstractTask, TaskListener listener, OnRemoveListener removeListener, OnSetSuccsessListener onSetSuccsessListener);

}
