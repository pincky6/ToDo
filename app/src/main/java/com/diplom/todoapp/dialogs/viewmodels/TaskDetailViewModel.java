package com.diplom.todoapp.dialogs.viewmodels;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.diplom.todoapp.EditorsUtil;
import com.diplom.todoapp.databinding.FragmentTaskDetailBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Priority;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Reminders;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskDetailViewModel {
    private Task task = null;
    public Task getTask(){
        return task;
    }

    public void setTask(@NonNull FragmentTaskDetailBinding binding) throws IOException {
        if(EditorsUtil.checkEditors(binding.taskTitle, binding.taskDescribe,
                        binding.taskEditTextTime, binding.taskEditTextDate)){
            throw new IOException();
        }
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date createDate = null;
        String title = binding.taskTitle.getText().toString();
        String describe = binding.taskDescribe.getText().toString();
        boolean allDay = binding.allDayCheckBox.isChecked();
        Date dateStart = null;
        Priority priority = getPriority(binding.taskPriority.getSelectedItem().toString());
        Reminders reminders = getReminder(binding.taskReminder.getSelectedItem().toString());
        try {
            createDate = format.parse(format.format(new Date()));
            dateStart = format.parse(binding.taskEditTextDate.getText().toString());
            task = new Task(createDate, title, describe, allDay,
                            dateStart, priority, reminders);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("wrong date input");
        }
    }

    private Priority getPriority(String priority){
        switch (priority){
            case "Low":
                return Priority.LOW;
            case "Medium":
                return Priority.MEDIUM;
            case "HIGH":
                return Priority.HIGH;
        }
        return Priority.LOW;
    }
    private Reminders getReminder(String reminder){
        switch (reminder){
            case "1 day before":
                return Reminders.DAY_1_BEFORE;
            case "5 minutes before":
                return Reminders.MINUTES_5_BEFORE;
            case "Don\'t remind":
                return Reminders.DONT_REMIND;
        }
        return Reminders.DONT_REMIND;
    }
}
