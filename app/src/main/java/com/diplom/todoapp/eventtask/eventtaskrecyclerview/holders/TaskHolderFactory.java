package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.ItemDateTaskBinding;
import com.diplom.todoapp.databinding.ItemEmptyBinding;
import com.diplom.todoapp.databinding.ItemTaskBinding;

public class TaskHolderFactory {
    public static AbstractTaskHolder produce(@NonNull ViewGroup parent, int taskType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(taskType == HolderType.EMPTY_TASK.ordinal()){
            ItemEmptyBinding binding = ItemEmptyBinding.inflate(inflater, parent, false);
            return new EmptyHolder(binding);
        }
        if(taskType == HolderType.TASK.ordinal()){
            ItemTaskBinding binding = ItemTaskBinding.inflate(inflater, parent, false);
            return new TaskHolder(binding);
        }
        else{
            ItemDateTaskBinding binding = ItemDateTaskBinding.inflate(inflater, parent, false);
            return new DateTaskHolder(binding);
        }
    }
}
