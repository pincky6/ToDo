package com.diplom.todoapp.details.viewmodels;

import androidx.annotation.NonNull;

import com.diplom.todoapp.utils.EditorsUtil;
import com.diplom.todoapp.utils.PriorityUtil;
import com.diplom.todoapp.utils.ReminderUtil;
import com.diplom.todoapp.databinding.FragmentDateTaskDetailBinding;
import com.diplom.todoapp.firebase.OnDataReceivedListener;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.utils.SuccsessFlagUtil;

import java.io.IOException;
import java.sql.Time;
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
                SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
                try {
                    String dateStart = formatDate.format(dateTask.dateStart);
                    String dateEnd = formatDate.format(dateTask.dateEnd);
                    String timeStart = formatTime.format(dateTask.dateStart);
                    String timeEnd = formatTime.format(dateTask.dateEnd);

                    binding.dateTaskEditTextDate.setText(dateStart);
                    binding.dateTaskEditTextDate2.setText(dateEnd);
                    binding.dateTaskEditTextTime.setText(timeStart);
                    binding.dateTaskEditTextTime2.setText(timeEnd);

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
        String title = binding.dateTaskTitle.getText().toString();
        String place = binding.dateTaskPlace.getText().toString();
        String describe = binding.dateTaskDescribe.getText().toString();
        boolean allDay = binding.allDayCheckBox.isChecked();
        Date dateStart = null;
        Date dateEnd = null;
        Time timeStart = Time.valueOf(binding.dateTaskEditTextTime.getText().toString() + ":00");
        Time timeEnd = Time.valueOf(binding.dateTaskEditTextTime2.getText().toString() + ":00");
        String priority = binding.dateTaskPriority.getSelectedItem().toString();
        String reminder = binding.dateTaskReminder.getSelectedItem().toString();
        String successFlag = "";
        try {
            dateStart = format.parse(binding.dateTaskEditTextDate.getText().toString());
            dateEnd = format.parse(binding.dateTaskEditTextDate2.getText().toString());
            assert dateStart != null;
            assert dateEnd != null;
            dateStart.setTime(dateStart.getTime() + timeStart.getTime());
            dateEnd.setTime(dateEnd.getTime() + timeEnd.getTime());
            successFlag = SuccsessFlagUtil.getStringFlagFromDate(dateStart);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("wrong date input");
        }
        if(dateTask == null)
            dateTask = new DateTask("DateTask-" + firebaseRepository.generateKey(), place, title, describe, allDay,
                    dateStart, dateEnd, priority, reminder, successFlag);
        else
            dateTask.setTask(dateTask.id, place, title, describe, allDay,
                    dateStart, dateEnd, priority, reminder, successFlag);
    }
}
