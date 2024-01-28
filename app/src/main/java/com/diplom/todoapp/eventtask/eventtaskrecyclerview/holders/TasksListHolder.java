package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import static androidx.navigation.ViewKt.findNavController;

import static com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskListFragment.REQUEST_REMOVE_TASK;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemDaysTasksBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.listeners.RemoveListener;
import com.diplom.todoapp.eventtask.listeners.SetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;

import java.util.ArrayList;

public class TasksListHolder extends RecyclerView.ViewHolder {
    ItemDaysTasksBinding binding;
    public TasksListHolder(@NonNull ItemDaysTasksBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(ArrayList<AbstractTask> tasks, TaskListener listener,
                     RemoveListener removeListener, SetSuccsessListener setSuccsessListener){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerView.setAdapter(new TaskAdapter(tasks, listener, removeListener, setSuccsessListener));
    }
}
