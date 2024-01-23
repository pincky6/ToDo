package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.app.AlertDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.listeners.SetSuccsessListener;
import com.diplom.todoapp.utils.PriorityUtil;
import com.diplom.todoapp.databinding.ItemDateTaskBinding;
import com.diplom.todoapp.eventtask.listeners.RemoveListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;
import com.diplom.todoapp.utils.SuccsessFlagUtil;

import java.text.SimpleDateFormat;

public class DateTaskHolder extends AbstractTaskHolder {
    ItemDateTaskBinding binding;
    public DateTaskHolder(@NonNull ItemDateTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    @Override
    public void bind(AbstractTask abstractTask,
                     TaskListener listener,
                     RemoveListener removeListener,
                     SetSuccsessListener setSuccsessListener){
        if(!(abstractTask instanceof DateTask)) throw new IllegalArgumentException("wrong type of argument in date task holder");
        DateTask dateTask = (DateTask) abstractTask;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        binding.title.setText(dateTask.title);
        binding.date.setText(formatDate.format(dateTask.createDate));
        binding.dateStart.setText(formatDate.format(dateTask.dateStart));
        binding.dateEnd.setText(formatDate.format(dateTask.dateEnd));
        binding.describe.setText(dateTask.describe);

        int color = PriorityUtil.getPriorityColor(PriorityUtil.getPriorityEnum(dateTask.priority));
        int succsessIndicatorColor = SuccsessFlagUtil.getColorFromSuccsessFlagString(dateTask.succsessFlag);
        binding.leftSide.setBackgroundColor(color);
        binding.succsessIndicator.setBackgroundColor(succsessIndicatorColor);
        Log.d("DateTask", dateTask.id);
        binding.getRoot().setOnClickListener(v -> listener.taskNavigation(dateTask));
        binding.getRoot().setOnLongClickListener(v -> {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
            builder.setMessage("What do you want?")
                    .setNeutralButton("Cancel", ((dialog, which) ->{} ))
                    .setPositiveButton("Set Task", (dialog, id) ->setSuccsessListener.set(abstractTask))
                    .setNegativeButton("Delete Task", (dialog, id) -> removeListener.remove(dateTask.id));
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
    }
}
