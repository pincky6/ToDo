package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.ItemTaskBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;

import java.util.Date;

public class TaskHolder extends AbstractTaskHolder {
    private ItemTaskBinding binding;
    public TaskHolder(@NonNull ItemTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    @Override
    public void bind(AbstractTask abstractTask){
        if(!(abstractTask instanceof Task)) throw new IllegalArgumentException("wrong type of argument in task holder");
        Task task = (Task) abstractTask;

        binding.title.setText(task.title);
        binding.date.setText(task.createDate.toString());
        binding.dateStart.setText(task.dateStart.toString());
        binding.describe.setText(task.describe);

        int color = getPriorityColor(task.priority.ordinal());
        binding.leftSide.setBackgroundColor(color);
        binding.rightSide.setBackgroundColor(color);
    }
}
