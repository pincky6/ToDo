package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.utils.PriorityUtil;
import com.diplom.todoapp.databinding.ItemTaskBinding;
import com.diplom.todoapp.eventtask.listeners.RemoveListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;

import java.text.SimpleDateFormat;

public class TaskHolder extends AbstractTaskHolder {
    private ItemTaskBinding binding;
    public TaskHolder(@NonNull ItemTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    @Override
    public void bind(AbstractTask abstractTask, TaskListener listener, RemoveListener removeListener){
        if(!(abstractTask instanceof Task)) throw new IllegalArgumentException("wrong type of argument in task holder");
        Task task = (Task) abstractTask;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");

        binding.title.setText(task.title);
        binding.date.setText(formatDate.format(task.createDate));
        binding.dateStart.setText(formatDate.format(task.dateStart));
        binding.describe.setText(task.describe);

        int color = PriorityUtil.getPriorityColor(PriorityUtil.getPriorityEnum(task.priority));
        binding.leftSide.setBackgroundColor(color);
        binding.rightSide.setBackgroundColor(color);
        binding.getRoot().setOnClickListener(v -> listener.taskNavigation(task));
        binding.getRoot().setOnLongClickListener(v -> {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
            builder.setMessage("You really want to delete this task?")
                    .setPositiveButton("Delete", (dialog, id) -> removeListener.remove(task.id))
                    .setNegativeButton("Cancel", (dialog, id) -> {
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
    }
}
