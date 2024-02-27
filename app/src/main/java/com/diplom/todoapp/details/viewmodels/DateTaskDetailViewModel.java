package com.diplom.todoapp.details.viewmodels;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.FragmentTaskDetailBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Priority;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Reminders;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;

public class DateTaskDetailViewModel {
    FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
    private DateTask dateTask = null;
    public ArrayList<Date> dates;
    public DateTaskDetailViewModel(){
        dateTask = new DateTask();
        dateTask.id = "DateTask-" + firebaseRepository.generateKey();
    }
    public DateTaskDetailViewModel(FragmentDateTaskDetailBinding binding, ArrayList<Date> dates,
                               Date selectedDate)
    {
        this.dates = dates;
        dateTask = new DateTask();
        dateTask.id = "DateTask-" + firebaseRepository.generateKey();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        initDetailFragment(
                binding, "", "", "",
                selectedDate, calendar.getTime(),
                false,
                PriorityUtil.getPriorityIndex(PriorityUtil.getPriorityEnum(String.valueOf(Priority.LOW))),
                ReminderUtil.getReminderIndex(ReminderUtil.getReminderEnum(Reminders.DONT_REMIND.name())),
                firebaseRepository.getCategories().indexOf("Don\'t Repeat")
        );
    }
    public DateTaskDetailViewModel(FragmentDateTaskDetailBinding binding, String key){
        firebaseRepository.getTaskFromKey(key, new OnDataReceivedListener() {
            @Override
            public void onDataReceived(AbstractTask data) {
                if(!(data instanceof DateTask)) return;
                dateTask = (DateTask) data;
                initDetailFragment(
                                   binding, dateTask.title, dateTask.place, dateTask.describe,
                                   dateTask.dateStart, dateTask.dateEnd,
                                   dateTask.allDayFlag,
                                   PriorityUtil.getPriorityIndex(PriorityUtil.getPriorityEnum(dateTask.priority)),
                                   ReminderUtil.getReminderIndex(ReminderUtil.getReminderEnum(dateTask.reminder)),
                                   firebaseRepository.getCategories().indexOf(dateTask.repeat)
                                   );
//                if(dateTask.subtasks == null) dateTask.subtasks = new ArrayList<>();
//                binding.dateTaskTitle.setText(dateTask.title);
//                binding.dateTaskPlace.setText(dateTask.place);
//                binding.dateTaskDescribe.setText(dateTask.describe);
//                SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
//                SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
//                try {
//                    String dateStart = formatDate.format(dateTask.dateStart);
//                    String dateEnd = formatDate.format(dateTask.dateEnd);
//                    String timeStart = formatTime.format(dateTask.dateStart);
//                    String timeEnd = formatTime.format(dateTask.dateEnd);
//
//                    binding.dateTaskEditTextDate.setText(dateStart);
//                    binding.dateTaskEditTextDate2.setText(dateEnd);
//                    binding.dateTaskEditTextTime.setText(timeStart);
//                    binding.dateTaskEditTextTime2.setText(timeEnd);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                binding.allDayCheckBox.setChecked(dateTask.allDayFlag);
//                binding.dateTaskPriority.setSelection(PriorityUtil.getPriorityIndex(PriorityUtil.getPriorityEnum(dateTask.priority)));
//                binding.dateTaskReminder.setSelection(ReminderUtil.getReminderIndex(ReminderUtil.getReminderEnum(dateTask.reminder)));
            }

            @Override
            public void onError(IllegalArgumentException databaseError) {

            }
        });
    }

    private void initDetailFragment(FragmentDateTaskDetailBinding binding, String title, String place, String describe,
                                    Date dateStart, Date dateEnd,
                                    boolean allDayFlag, int selectPriority, int selectReminder, int selectRepeat)
    {
        if(dateTask != null && dateTask.subtasks == null) dateTask.subtasks = new ArrayList<>();
        binding.dateTaskTitle.setText(title);
        binding.dateTaskPlace.setText(place);
        binding.dateTaskDescribe.setText(describe);
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        try {
            String dateStartStr = formatDate.format(dateStart);
            String dateEndStr = formatDate.format(dateEnd);
            String timeStart = formatTime.format(dateStart);
            String timeEnd = formatTime.format(dateEnd);

            binding.dateTaskEditTextDate.setText(dateStartStr);
            binding.dateTaskEditTextDate2.setText(dateEndStr);
            binding.dateTaskEditTextTime.setText(timeStart);
            binding.dateTaskEditTextTime2.setText(timeEnd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        binding.allDayCheckBox.setChecked(allDayFlag);
        binding.dateTaskPriority.setSelection(selectPriority);
        binding.dateTaskReminder.setSelection(selectReminder);
    }

    public DateTask getDateTask(){
        return dateTask;
    }

    public void setTask(@NonNull FragmentDateTaskDetailBinding binding) throws IOException {
        if(EditorsUtil.checkEditors(binding.dateTaskTitle, binding.dateTaskEditTextDate)){
            throw new IOException();
        }
        if(!binding.allDayCheckBox.isChecked()){
            if(EditorsUtil.checkEditors(
                    binding.dateTaskEditTextTime, binding.dateTaskEditTextDate2,
                    binding.dateTaskEditTextTime2)){
                throw new IOException();
            }
        }

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String title = binding.dateTaskTitle.getText().toString();
        String place = binding.dateTaskPlace.getText().toString();
        String describe = binding.dateTaskDescribe.getText().toString();
        boolean allDay = binding.allDayCheckBox.isChecked();
        Date dateStart = null;
        Date dateEnd = null;
        String priority = binding.dateTaskPriority.getSelectedItem().toString();
        String reminder = binding.dateTaskReminder.getSelectedItem().toString();
        String category = binding.categoriesSpinner.getSelectedItem().toString();
        String repeat = binding.dateTaskRepeat.getSelectedItem().toString();
        String successFlag = "";
        try {
            dateStart = format.parse(binding.dateTaskEditTextDate.getText().toString());
            successFlag = SuccsessFlagUtil.getStringFlagFromDate(dateStart);
            if(allDay) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateStart);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                dateEnd = calendar.getTime();
            } else {
                Date timeStart = timeFormat.parse(binding.dateTaskEditTextTime.getText().toString());
                Date timeEnd = timeFormat.parse(binding.dateTaskEditTextTime2.getText().toString());
                Calendar calendarStart = Calendar.getInstance();
                Calendar calendarEnd = Calendar.getInstance();
                calendarStart.setTime(timeStart);
                calendarEnd.setTime(timeEnd);
                dateEnd = format.parse(binding.dateTaskEditTextDate2.getText().toString());
                dateStart.setTime(dateStart.getTime() + (calendarStart.get(Calendar.HOUR_OF_DAY) * 3600 +
                        calendarStart.get(Calendar.MINUTE) * 60) * 1000L);
                dateEnd.setTime(dateEnd.getTime() + (calendarEnd.get(Calendar.HOUR_OF_DAY) * 3600 +
                        calendarEnd.get(Calendar.MINUTE) * 60) * 1000L);
                if(dateEnd.before(dateStart)){
                    throw new IllegalArgumentException();
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("wrong date input");
        }
        if(!allDay) {
            for(int i = 0; i < dates.size(); i += 2){
                if (dates.get(i).after(dateStart) && dates.get(i + 1).before(dateEnd))
                    throw new InputMismatchException("wrong date input: dates crossing");
            }
        }
        if(dateTask == null) {
            dateTask = new DateTask("DateTask-" + firebaseRepository.generateKey(), title, place, describe, allDay,
                    dateStart, dateEnd, priority, reminder, successFlag, category, repeat);
        } else {
            dateTask.setTask(dateTask.id, title, place, describe, allDay,
                    dateStart, dateEnd, priority, reminder, dateTask.succsessFlag, category, repeat);
        }
    }

    public ArrayList<String> getCategory(){
        if(firebaseRepository == null) return new ArrayList<>();
        return (ArrayList<String>)firebaseRepository.getCategories().clone();
    }
}
