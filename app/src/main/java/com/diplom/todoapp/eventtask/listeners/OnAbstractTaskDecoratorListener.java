package com.diplom.todoapp.eventtask.listeners;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

public interface OnAbstractTaskDecoratorListener {
    public void run(AbstractTask abstractTask, String requestKey);
}
