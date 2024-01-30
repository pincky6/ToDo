package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemEmptyBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.listeners.RemoveListener;
import com.diplom.todoapp.eventtask.listeners.SetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;

public class EmptyHolder extends AbstractTaskHolder {
    ItemEmptyBinding binding;
    public EmptyHolder(@NonNull ItemEmptyBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void bind(AbstractTask abstractTask, TaskListener listener, RemoveListener removeListener, SetSuccsessListener setSuccsessListener) {
    }
}
