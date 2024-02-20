package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.eventtask.listeners.OnSetSuccsessListener;
import com.diplom.todoapp.utils.PriorityUtil;
import com.diplom.todoapp.databinding.ItemTaskBinding;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;
import com.diplom.todoapp.utils.SuccsessFlagUtil;

import java.text.SimpleDateFormat;

public class TaskHolder extends AbstractTaskHolder {
    private ItemTaskBinding binding;
    public TaskHolder(@NonNull ItemTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    @Override
    public void bind(AbstractTask abstractTask, TaskListener listener, OnRemoveListener removeListener, OnSetSuccsessListener onSetSuccsessListener){
        if(!(abstractTask instanceof Task)) throw new IllegalArgumentException("wrong type of argument in task holder");
        Task task = (Task) abstractTask;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy;HH:mm");
        String[] formatedDateStart = formatDate.format(task.dateStart).split(";");
        binding.title.setText(task.title);
        binding.date.setText(formatedDateStart[0]);
        binding.dateStart.setText((task.allDayFlag == true) ? "All      \nDay" : formatedDateStart[1]);
        binding.describe.setText(task.describe);

        int borderResource = PriorityUtil.getPriorityBorderResource(PriorityUtil.getPriorityEnum(task.priority));
        int statusResource = SuccsessFlagUtil.getBackgroundResourceFromSuccsessFlagString(task.succsessFlag);
        String succsessIndicatorText = SuccsessFlagUtil.getSuccsessStringFromString(task.succsessFlag);
        binding.leftSide.setBackgroundResource(borderResource);
        binding.succsessIndicator.setBackgroundResource(statusResource);
        binding.succsessIndicator.setText(succsessIndicatorText);
        binding.getRoot().setOnClickListener(v -> listener.taskNavigation(task));
        binding.getRoot().setOnLongClickListener(v -> {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
            builder.setMessage("What do you want?")
                    .setNeutralButton("Cancel", ((dialog, which) ->{} ))
                    .setPositiveButton("Set Task", (dialog, id) -> onSetSuccsessListener.set(abstractTask))
                    .setNegativeButton("Delete Task", (dialog, id) -> removeListener.remove(abstractTask.id));
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
    }
}
