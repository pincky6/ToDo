package com.diplom.todoapp.eventtask.listeners;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

public interface OnResetTaskLisener {
    public void listen(AbstractTask oldTask, AbstractTask newTask);
}
