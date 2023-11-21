package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.PriorityUtil;
import com.diplom.todoapp.databinding.ItemTaskBinding;
import com.diplom.todoapp.eventtask.RemoveListener;
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
    public void bind(AbstractTask abstractTask, TaskListener listener, RemoveListener removeListener){
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
        binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
                builder.setMessage("You really want to delete this task?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeListener.remove(task.id);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }
}
