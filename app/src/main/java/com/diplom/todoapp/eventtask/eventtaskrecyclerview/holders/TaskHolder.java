package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Holiday;
import com.diplom.todoapp.eventtask.listeners.OnSetSuccsessListener;
import com.diplom.todoapp.utils.PriorityUtil;
import com.diplom.todoapp.databinding.ItemTaskBinding;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;
import com.diplom.todoapp.utils.SuccsessFlagUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskHolder extends AbstractTaskHolder {
    private ItemTaskBinding binding;
    public TaskHolder(@NonNull ItemTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(AbstractTask abstractTask, Date day, TaskListener listener, OnRemoveListener removeListener, OnSetSuccsessListener onSetSuccsessListener) {
        bind(abstractTask, listener, removeListener, onSetSuccsessListener);
        Holiday holiday = (Holiday) abstractTask;
        Date today = new Date();
        Date actualDate;
        if(holiday.dateStart.getTime() - today.getTime() > 0) {
            actualDate = today;
        } else if(day.after(holiday.dateStart)){
            actualDate = calculateNewDate(holiday, day);
        } else {
            actualDate = (Date) holiday.dateStart.clone();
        }
        long days = (day.getTime() - actualDate.getTime()) / 86400000;
        binding.untilDate.setText(String.valueOf(days) + " days\n");
    }
    @Override
    public void bind(AbstractTask abstractTask, TaskListener listener, OnRemoveListener removeListener, OnSetSuccsessListener onSetSuccsessListener){
        if(!(abstractTask instanceof Holiday)) throw new IllegalArgumentException("wrong type of argument in task holder");
        Holiday holiday = (Holiday) abstractTask;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy;HH :mm");
        String[] formatedDateStart = formatDate.format(holiday.dateStart).split(";");
        binding.title.setText(holiday.title);
        binding.date.setText(formatedDateStart[0]);
        binding.dateStart.setText((holiday.allDayFlag == true) ? "All      \nDay" : formatedDateStart[1]);
        binding.describe.setText(holiday.describe);
        binding.category.setText(holiday.category);

        int borderResource = PriorityUtil.getPriorityBorderResource(PriorityUtil.getPriorityEnum(holiday.priority));
        int statusResource = SuccsessFlagUtil.getBackgroundResourceFromSuccsessFlagString(holiday.succsessFlag);
        String succsessIndicatorText = SuccsessFlagUtil.getSuccsessStringFromString(holiday.succsessFlag);
        binding.leftSide.setBackgroundResource(borderResource);
        binding.succsessIndicator.setBackgroundResource(statusResource);
        binding.succsessIndicator.setText(succsessIndicatorText);
        binding.getRoot().setOnClickListener(v -> listener.taskNavigation(holiday));
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
    private Date calculateNewDate(Holiday holiday, Date selectedDay){
        Date date = (Date) holiday.dateStart.clone();
        Calendar calendar = Calendar.getInstance(), calendarSelected = Calendar.getInstance();
        calendar.setTime(date);
        calendarSelected.setTime(selectedDay);
        if(date.getTime() - selectedDay.getTime() > 0) return date;
        if(holiday.repeat.equals("Every Day")){
            calendar.add(Calendar.DAY_OF_YEAR, calendarSelected.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR));
            return calendar.getTime();
        } else if(holiday.repeat.equals("Every Week")){
            calendar.add(Calendar.WEEK_OF_YEAR, calendarSelected.get(Calendar.WEEK_OF_YEAR) - calendar.get(Calendar.WEEK_OF_YEAR));
            return calendar.getTime();
        } else if(holiday.repeat.equals("Every Year")){
            calendar.add(Calendar.YEAR, calendarSelected.get(Calendar.YEAR) - calendar.get(Calendar.YEAR));
            return calendar.getTime();
        }
        return date;
    }
}
