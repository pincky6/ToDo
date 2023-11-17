package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.ItemDateTaskBinding;
import com.diplom.todoapp.databinding.ItemTaskBinding;

public class TaskHolderFactory {
    public static AbstractTaskHolder produce(@NonNull ViewGroup parent, int taskType){
        if(taskType == TaskType.TASK.ordinal()){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemTaskBinding binding = ItemTaskBinding.inflate(inflater, parent, false);
            return new TaskHolder(binding);
        }
        else{
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemDateTaskBinding binding = ItemDateTaskBinding.inflate(inflater, parent, false);
            return new DateTaskHolder(binding);
        }
    }
}
