package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.ItemEmptyBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.eventtask.listeners.OnSetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;

public class EmptyHolder extends AbstractTaskHolder {
    ItemEmptyBinding binding;
    public EmptyHolder(@NonNull ItemEmptyBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void bind(AbstractTask abstractTask, TaskListener listener, OnRemoveListener removeListener, OnSetSuccsessListener onSetSuccsessListener) {
    }
}
