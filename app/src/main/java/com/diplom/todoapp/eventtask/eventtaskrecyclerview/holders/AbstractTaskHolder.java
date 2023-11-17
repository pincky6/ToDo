package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

public abstract class AbstractTaskHolder extends RecyclerView.ViewHolder {
    public AbstractTaskHolder(@NonNull View itemView) {
        super(itemView);
    }
    public abstract void bind(AbstractTask abstractTask);
    public int getPriorityColor(int priority){
        switch (priority){
            case 0:
                return Color.GREEN;
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.RED;
        }
        return 0;
    }
}
