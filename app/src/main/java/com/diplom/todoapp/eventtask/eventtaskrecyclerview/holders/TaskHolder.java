package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.diplom.todoapp.PriorityUtil;
import com.diplom.todoapp.databinding.ItemTaskBinding;
import com.diplom.todoapp.eventtask.TaskListener;
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
    public void bind(AbstractTask abstractTask, TaskListener listener){
        if(!(abstractTask instanceof Task)) throw new IllegalArgumentException("wrong type of argument in task holder");
        Task task = (Task) abstractTask;

        binding.title.setText(task.title);
        binding.date.setText(task.createDate.toString());
        binding.dateStart.setText(task.dateStart.toString());
        binding.describe.setText(task.describe);

        int color = PriorityUtil.getPriorityColor(PriorityUtil.getPriorityEnum(task.priority));
        binding.leftSide.setBackgroundColor(color);
        binding.rightSide.setBackgroundColor(color);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.taskNavigation(task);
            }
        });
    }
}
