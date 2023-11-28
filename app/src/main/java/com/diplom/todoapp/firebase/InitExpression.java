package com.diplom.todoapp.firebase;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

import java.util.ArrayList;

public interface InitExpression {
    public void init(ArrayList<AbstractTask> days);
}
