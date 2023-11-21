package com.diplom.todoapp.dialogs.viewmodels;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.diplom.todoapp.EditorsUtil;
import com.diplom.todoapp.PriorityUtil;
import com.diplom.todoapp.R;
import com.diplom.todoapp.ReminderUtil;
import com.diplom.todoapp.databinding.FragmentDateTaskDetailBinding;
import com.diplom.todoapp.dialogs.OnDataReceivedListener;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Priority;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Reminders;
import com.diplom.todoapp.firebase.FirebaseRepository;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTaskDetailViewModel {
    FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
    private DateTask dateTask = null;
    public DateTaskDetailViewModel(){}
    public DateTaskDetailViewModel(FragmentDateTaskDetailBinding binding, String key){
        firebaseRepository.getTaskFromKey(key, new OnDataReceivedListener() {
            @Override
            public void onDataReceived(AbstractTask data) {
                if(!(data instanceof DateTask)) return;
                dateTask = (DateTask) data;
                binding.dateTaskTitle.setText(dateTask.title);
                binding.dateTaskPlace.setText(dateTask.place);
                binding.dateTaskDescribe.setText(dateTask.describe);
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    String dateStart = format.format(dateTask.dateStart);
                    String dateEnd = format.format(dateTask.dateEnd);
                    binding.dateTaskEditTextDate.setText(dateStart);
                    binding.dateTaskEditTextDate2.setText(dateEnd);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                binding.allDayCheckBox.setChecked(dateTask.allDayFlag);
                binding.dateTaskPriority.setSelection(PriorityUtil.getPriorityIndex(PriorityUtil.getPriorityEnum(dateTask.priority)));
                binding.dateTaskReminder.setSelection(ReminderUtil.getReminderIndex(ReminderUtil.getReminderEnum(dateTask.reminder)));
            }

            @Override
            public void onError(IllegalArgumentException databaseError) {

            }
        });
    }
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
        String priority = binding.dateTaskPriority.getSelectedItem().toString();
        String reminder = binding.dateTaskReminder.getSelectedItem().toString();
        try {
            createDate = format.parse(format.format(new Date()));
            dateStart = format.parse(binding.dateTaskEditTextDate.getText().toString());
            dateEnd = format.parse(binding.dateTaskEditTextDate2.getText().toString());
            if(dateTask == null)
                dateTask = new DateTask("DateTask-" + firebaseRepository.generateKey(), createDate, place, title, describe, allDay,
                    dateStart, dateEnd, priority, reminder);
            else
                dateTask.setTask(dateTask.id, createDate, place, title, describe, allDay,
                        dateStart, dateEnd, priority, reminder);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("wrong date input");
        }
    }
}
