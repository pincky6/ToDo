package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.diplom.todoapp.databinding.ItemListTasksBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.AbstractTaskAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.eventtask.listeners.OnSetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;

import java.util.ArrayList;

public class TaskListHolder extends AbstractTaskHolder {
    ItemListTasksBinding binding;
    public TaskListHolder(@NonNull ItemListTasksBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(ArrayList<AbstractTask> tasks, String title,AbstractTaskAdapter abstractTaskAdapter){

        binding.dayOfMonth.setText(title);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerView.setAdapter(abstractTaskAdapter);
    }

    @Override
    public void bind(AbstractTask abstractTask, TaskListener listener, OnRemoveListener removeListener, OnSetSuccsessListener onSetSuccsessListener) {

    }
}
