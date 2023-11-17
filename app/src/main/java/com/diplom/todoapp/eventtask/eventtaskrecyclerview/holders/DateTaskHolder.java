package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.ItemDateTaskBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;

import java.util.Date;

public class DateTaskHolder extends AbstractTaskHolder {
    ItemDateTaskBinding binding;
    public DateTaskHolder(@NonNull ItemDateTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    @Override
    public void bind(AbstractTask abstractTask){
        if(!(abstractTask instanceof DateTask)) throw new IllegalArgumentException("wrong type of argument in date task holder");
        DateTask dateTask = (DateTask) abstractTask;

        binding.date.setText(dateTask.createDate.toString());
        binding.dateStart.setText(dateTask.dateStart.toString());
        binding.dateEnd.setText(dateTask.dateEnd.toString());
        binding.describe.setText(dateTask.describe);

        int color = getPriorityColor(dateTask.priority.ordinal());
        binding.leftSide.setBackgroundColor(color);
        binding.rightSide.setBackgroundColor(color);
    }
}
