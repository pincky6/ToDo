package com.diplom.todoapp.eventtask.listeners;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

public interface OnTaskListener {
    public void listen(AbstractTask abstractTask, String requestKey);
}
