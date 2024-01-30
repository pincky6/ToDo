package com.diplom.todoapp.details.viewmodels;

import androidx.annotation.NonNull;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Priority;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Reminders;
import com.diplom.todoapp.utils.EditorsUtil;
import com.diplom.todoapp.utils.PriorityUtil;
import com.diplom.todoapp.utils.ReminderUtil;
import com.diplom.todoapp.databinding.FragmentTaskDetailBinding;
import com.diplom.todoapp.firebase.OnDataReceivedListener;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.utils.SuccsessFlagUtil;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;

public class TaskDetailViewModel {
    FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
    private Task task = null;
    private Date selectedDate;
    public ArrayList<Date> dates;
    public Task getTask(){
        return task;
    }

    public TaskDetailViewModel(){}
    public TaskDetailViewModel(FragmentTaskDetailBinding binding, ArrayList<Date> dates,
                               Date selectedDate)
    {
        this.dates = dates;
        initDetailFragment(binding, "", "", selectedDate,
                false,
                PriorityUtil.getPriorityIndex(PriorityUtil.getPriorityEnum(String.valueOf(Priority.LOW))),
                ReminderUtil.getReminderIndex(ReminderUtil.getReminderEnum(Reminders.DONT_REMIND.name())));
    }
    public TaskDetailViewModel(FragmentTaskDetailBinding binding, String key,
                               ArrayList<Date> dates) {
        this.dates = dates;
        firebaseRepository.getTaskFromKey(key, new OnDataReceivedListener() {
            @Override
            public void onDataReceived(AbstractTask data) {
                if(!(data instanceof Task)) return;
                task = (Task) data;
                initDetailFragment(binding, task.title, task.describe, task.dateStart,
                                   task.allDayFlag,
                                   PriorityUtil.getPriorityIndex(PriorityUtil.getPriorityEnum(task.priority)),
                                   ReminderUtil.getReminderIndex(ReminderUtil.getReminderEnum(task.reminder)));
//                binding.taskTitle.setText(task.title);
//                binding.taskDescribe.setText(task.describe);
//                SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
//                SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
//                try {
//                    String dateStart = formatDate.format(task.dateStart);
//                    String timeStart = formatTime.format(task.dateStart);
//                    binding.taskEditTextDate.setText(dateStart);
//                    binding.taskEditTextTime.setText(timeStart);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                binding.allDayCheckBox.setChecked(task.allDayFlag);
//                binding.taskPriority.setSelection(PriorityUtil.getPriorityIndex(PriorityUtil.getPriorityEnum(task.priority)));
//                binding.taskReminder.setSelection(ReminderUtil.getReminderIndex(ReminderUtil.getReminderEnum(task.reminder)));
            }
            @Override
            public void onError(IllegalArgumentException databaseError) {

            }
        });
    }

    private void initDetailFragment(FragmentTaskDetailBinding binding, String title, String describe, Date date,
                                    boolean allDayFlag, int selectPriority, int selectReminder)
    {
        binding.taskTitle.setText(title);
        binding.taskDescribe.setText(describe);
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm");
        try {
            String dateStart = formatDate.format(date);
            String timeStart = formatTime.format(date);
            binding.taskEditTextDate.setText(dateStart);
            binding.taskEditTextTime.setText(timeStart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        binding.allDayCheckBox.setChecked(allDayFlag);
        binding.taskPriority.setSelection(selectPriority);
        binding.taskReminder.setSelection(selectReminder);
    }

    public void setTask(@NonNull FragmentTaskDetailBinding binding) throws IOException {
        if(EditorsUtil.checkEditors(binding.taskTitle, binding.taskDescribe,
                        binding.taskEditTextTime, binding.taskEditTextDate)){
            throw new IOException();
        }
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Time time = Time.valueOf(binding.taskEditTextTime.getText().toString());
        String title = binding.taskTitle.getText().toString();
        String describe = binding.taskDescribe.getText().toString();
        boolean allDay = binding.allDayCheckBox.isChecked();
        Date dateStart;
        String priority = binding.taskPriority.getSelectedItem().toString();
        String reminder = binding.taskReminder.getSelectedItem().toString();
        String successFlag;
        try {
            dateStart = format.parse(binding.taskEditTextDate.getText().toString());
            assert dateStart != null;
            dateStart.setTime(dateStart.getTime() + time.getTime());
            successFlag = SuccsessFlagUtil.getStringFlagFromDate(dateStart);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("wrong date input");
        }
        for(Date date: dates)
        {
            if(date.equals(dateStart)) throw new InputMismatchException("wrong date input: dates crossing");
        }
        if (task == null)
            task = new Task("Task-" + firebaseRepository.generateKey(), title, describe, allDay,
                    dateStart, priority, reminder, successFlag);
        else
            task.setTask(task.id, title, describe, allDay,
                    dateStart, priority, reminder, successFlag);
    }
}
