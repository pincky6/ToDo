package com.diplom.todoapp.dialogs.viewmodels;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.diplom.todoapp.EditorsUtil;
import com.diplom.todoapp.PriorityUtil;
import com.diplom.todoapp.ReminderUtil;
import com.diplom.todoapp.databinding.FragmentDateTaskDetailBinding;
import com.diplom.todoapp.databinding.FragmentTaskDetailBinding;
import com.diplom.todoapp.dialogs.OnDataReceivedListener;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Priority;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Reminders;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.firebase.FirebaseRepository;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskDetailViewModel {
    FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
    private Task task = null;
    public Task getTask(){
        return task;
    }

    public TaskDetailViewModel(){}
    public TaskDetailViewModel(FragmentTaskDetailBinding binding, String key) {
        firebaseRepository.getTaskFromKey(key, new OnDataReceivedListener() {
            @Override
            public void onDataReceived(AbstractTask data) {
                if(!(data instanceof Task)) return;
                task = (Task) data;
                binding.taskTitle.setText(task.title);
                binding.taskDescribe.setText(task.describe);
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    String dateStart = format.format(task.dateStart);
                    binding.taskEditTextDate.setText(dateStart);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                binding.allDayCheckBox.setChecked(task.allDayFlag);
                binding.taskPriority.setSelection(PriorityUtil.getPriorityIndex(PriorityUtil.getPriorityEnum(task.priority)));
                binding.taskReminder.setSelection(ReminderUtil.getReminderIndex(ReminderUtil.getReminderEnum(task.reminder)));
            }

            @Override
            public void onError(IllegalArgumentException databaseError) {

            }
        });
    }

    public void setTask(@NonNull FragmentTaskDetailBinding binding) throws IOException {
        if(EditorsUtil.checkEditors(binding.taskTitle, binding.taskDescribe,
                        binding.taskEditTextTime, binding.taskEditTextDate)){
            throw new IOException();
        }
        if(task != null) return;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date createDate = null;
        String title = binding.taskTitle.getText().toString();
        String describe = binding.taskDescribe.getText().toString();
        boolean allDay = binding.allDayCheckBox.isChecked();
        Date dateStart = null;
        String priority = binding.taskPriority.getSelectedItem().toString().toUpperCase();
        String reminder = binding.taskReminder.getSelectedItem().toString();
        try {
            createDate = format.parse(format.format(new Date()));
            dateStart = format.parse(binding.taskEditTextDate.getText().toString());
            if (task == null)
                task = new Task("Task-" + firebaseRepository.generateKey(), createDate, title, describe, allDay,
                            dateStart, priority, reminder);
            else
                task.setTask(task.id, createDate, title, describe, allDay,
                        dateStart, priority, reminder);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("wrong date input");
        }
    }
}
