package com.diplom.todoapp.dialogs.fragments;

import static androidx.navigation.ViewKt.findNavController;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentTaskDetailBinding;
import com.diplom.todoapp.eventtask.TaskFragmentDirections;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskDetailFragment extends Fragment {
    FragmentTaskDetailBinding binding = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false);
        initSpinners();
        initToolbar();
        initTimeDateInput();
        initSaveButton();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
//--------------------------------------------------------------------------------------------------
    private void initSpinners(){

        String[] reminders = new String[]{"1 day before", "5 minutes before", "Don\'t remind"};
        ArrayAdapter<String> remindAdapter =
                new ArrayAdapter<>(getContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        reminders);
        binding.taskReminder.setAdapter(remindAdapter);

        String[] priorities = new String[]{"Low", "Middle", "High"};
        ArrayAdapter<String> prioritiyAdapter =
                new ArrayAdapter<>(getContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        priorities);
        binding.taskPriority.setAdapter(prioritiyAdapter);
    }
    private void initToolbar(){
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(getView()).navigate(
                        TaskDetailFragmentDirections.actionTaskDetailFragmentToEventTaskFragment()
                );
            }
        });
    }
    private void initTimeDateInput(){
        binding.taskEditTextDate.setFocusable(false);
        binding.taskEditTextDate.setClickable(true);
        binding.taskEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        binding.taskEditTextTime.setFocusable(false);
        binding.taskEditTextTime.setClickable(true);
        binding.taskEditTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }
    private void initSaveButton(){
        binding.taskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   String title = binding.taskTitle.getText().toString();
                   String describe = binding.taskDescribe.getText().toString();
                   boolean allDay = binding.allDayCheckBox.isChecked();
                   SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");
                   Date dateStart = null, remindDate = null;
                try {
                    dateStart = format.parse(binding.taskEditTextDate.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
    private void showDatePickerDialog(){
        DatePickerDialog datePicker = new DatePickerDialog(getContext());
        datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                String dateString = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        .format(selectedDate.getTime());
                binding.taskEditTextDate.setText(dateString);
            }
        });
        datePicker.show();
    }
    private void showTimePickerDialog(){
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                binding.taskEditTextTime.setText(time);
            }
        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
        timePicker.show();
    }
}
