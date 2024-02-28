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
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Holiday;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.utils.SuccsessFlagUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskDetailViewModel {
    FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
    private Holiday holiday = null;
    private Date selectedDate;
    public ArrayList<Date> dates;
    public Holiday getTask(){
        return holiday;
    }

    public TaskDetailViewModel(){}
    public TaskDetailViewModel(FragmentTaskDetailBinding binding, ArrayList<Date> dates,
                               Date selectedDate)
    {
        this.dates = dates;
        if(holiday != null && holiday.category == null) holiday.category = new String("Without Category");
        initDetailFragment(
                            binding, "", "", selectedDate,
                            false,
                            PriorityUtil.getPriorityIndex(PriorityUtil.getPriorityEnum(String.valueOf(Priority.LOW))),
                            ReminderUtil.getReminderIndex(ReminderUtil.getReminderEnum(Reminders.DONT_REMIND.name())),
                            0
                          );
    }
    public TaskDetailViewModel(FragmentTaskDetailBinding binding, String key,
                               ArrayList<Date> dates) {
        this.dates = dates;
        firebaseRepository.getTaskFromKey(key, new OnDataReceivedListener() {
            @Override
            public void onDataReceived(AbstractTask data) {
                if(!(data instanceof Holiday)) return;
                holiday = (Holiday) data;
                if(holiday.category == null) holiday.category = new String("Without Category");
                initDetailFragment(
                                   binding, holiday.title, holiday.describe, holiday.dateStart,
                                   holiday.allDayFlag,
                                   PriorityUtil.getPriorityIndex(PriorityUtil.getPriorityEnum(holiday.priority)),
                                   ReminderUtil.getReminderIndex(ReminderUtil.getReminderEnum(holiday.reminder)),
                                   firebaseRepository.getCategories().indexOf(holiday.category)
                                  );
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
                                    boolean allDayFlag, int selectPriority, int selectReminder, int selectCategory)
    {
        binding.taskTitle.setText(title);
        binding.taskDescribe.setText(describe);
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        try {
            String dateStart = formatDate.format(date);
            String timeStart = formatTime.format(date);
            binding.taskEditTextDate.setText(dateStart);
            binding.taskEditTextTime.setText(timeStart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        binding.allDayCheckBox.setChecked(allDayFlag);
        binding.taskPriority.setSelection(selectPriority);
        binding.taskReminder.setSelection(selectReminder);
    }

    public void setTask(@NonNull FragmentTaskDetailBinding binding) throws IOException, ParseException {
        if(EditorsUtil.checkEditors(binding.taskTitle,
                        binding.taskEditTextTime, binding.taskEditTextDate)){
            throw new IOException();
        }
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date time = timeFormat.parse(binding.taskEditTextTime.getText().toString());
        String title = binding.taskTitle.getText().toString();
        String describe = binding.taskDescribe.getText().toString();
        boolean allDay = true;
        Date dateStart;
        String priority = binding.taskPriority.getSelectedItem().toString();
        String reminder = binding.taskReminder.getSelectedItem().toString();
        String repeat = binding.taskRepeat.getSelectedItem().toString();
        String category = binding.categoriesSpinner.getSelectedItem().toString();
        String successFlag;
        try {
            dateStart = format.parse(binding.taskEditTextDate.getText().toString());
            assert dateStart != null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            dateStart.setTime(dateStart.getTime() + (calendar.get(Calendar.HOUR_OF_DAY) * 3600 +
                    calendar.get(Calendar.MINUTE) * 60) * 1000L);
            successFlag = SuccsessFlagUtil.getStringFlagFromDate(dateStart);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("wrong date input");
        }
        if(!allDay) {
//            for (Date date : dates) {
//                if (date.equals(dateStart))
//                    throw new InputMismatchException("wrong date input: dates crossing");
//            }
        }
        if (holiday == null)
            holiday = new Holiday("Task-" + firebaseRepository.generateKey(), title, describe, allDay,
                    dateStart, priority, reminder, successFlag, repeat, category);
        else
            holiday.setTask(holiday.id, title, describe, allDay,
                    dateStart, priority, reminder, successFlag, repeat, category);
    }
    public ArrayList<String> getCategory(){
        if(firebaseRepository == null) return new ArrayList<>();
        return (ArrayList<String>)firebaseRepository.getCategories().clone();
    }
}
