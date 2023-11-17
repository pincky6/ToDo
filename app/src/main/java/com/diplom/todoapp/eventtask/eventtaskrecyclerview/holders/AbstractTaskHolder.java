package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

public abstract class AbstractTaskHolder extends RecyclerView.ViewHolder {
    public AbstractTaskHolder(@NonNull View itemView) {
        super(itemView);
    }
    public abstract void bind(AbstractTask abstractTask);
}
