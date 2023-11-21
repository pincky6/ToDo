package com.diplom.todoapp.dialogs.viewmodels;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.diplom.todoapp.EditorsUtil;
import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentDateTaskDetailBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Priority;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Reminders;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTaskDetailViewModel {
    private DateTask dateTask = null;
    public DateTask getDateTask(){
        return dateTask;
    }

    public void setTask(@NonNull FragmentDateTaskDetailBinding binding) throws IOException {
        if(EditorsUtil.checkEditors(binding.dateTaskTitle, binding.dateTaskDescribe,
                        binding.dateTaskPlace, binding.dateTaskEditTextDate,
                        binding.dateTaskEditTextTime, binding.dateTaskEditTextDate2,
                        binding.dateTaskEditTextTime2)){
            throw new IOException();
        }
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date createDate = null;
        String title = binding.dateTaskTitle.getText().toString();
        String place = binding.dateTaskPlace.getText().toString();
        String describe = binding.dateTaskDescribe.getText().toString();
        boolean allDay = binding.allDayCheckBox.isChecked();
        Date dateStart = null;
        Date dateEnd = null;
        Priority priority = getPriority(binding.dateTaskPriority.getSelectedItem().toString());
        Reminders reminders = getReminder(binding.dateTaskReminder.getSelectedItem().toString());
        try {
            createDate = format.parse(format.format(new Date()));
            dateStart = format.parse(binding.dateTaskEditTextDate.getText().toString());
            dateEnd = format.parse(binding.dateTaskEditTextDate2.getText().toString());
            dateTask = new DateTask(createDate, place, title, describe, allDay,
                    dateStart, dateEnd, priority, reminders);
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
