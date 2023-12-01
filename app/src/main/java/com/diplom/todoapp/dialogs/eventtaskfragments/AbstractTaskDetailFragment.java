package com.diplom.todoapp.dialogs.eventtaskfragments;

import static androidx.navigation.ViewKt.findNavController;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.diplom.todoapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public abstract class AbstractTaskDetailFragment extends Fragment {
    protected void initSpinners(Spinner taskReminder, Spinner taskPriority){

        String[] reminders = new String[]{"1 day before", "5 minutes before", "Don\'t remind"};
        ArrayAdapter<String> remindAdapter =
                new ArrayAdapter<>(getContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        reminders);
        taskReminder.setAdapter(remindAdapter);

        String[] priorities = new String[]{"Low", "Middle", "High"};
        ArrayAdapter<String> prioritiyAdapter =
                new ArrayAdapter<>(getContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        priorities);
        taskPriority.setAdapter(prioritiyAdapter);
    }
    protected void initToolbar(MaterialToolbar toolbar, View parentView){
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> findNavController(parentView).popBackStack());
    }
    protected void showDatePickerDialog(EditText textInputEditText){
        DatePickerDialog datePicker = new DatePickerDialog(getContext());
        datePicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            String dateString = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(selectedDate.getTime());
            textInputEditText.setText(dateString);
        });
        datePicker.show();
    }
    protected void showTimePickerDialog(EditText textInputEditText){
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            textInputEditText.setText(time);
        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
        timePicker.show();
    }
    protected <Editor extends EditText> void initTimeInputs(Editor... editors){
        for(Editor timeEditor: editors){
            timeEditor.setFocusable(false);
            timeEditor.setClickable(true);
            timeEditor.setOnClickListener(v -> showTimePickerDialog(timeEditor));
        }
    }
    protected <Editor extends EditText> void initDateInputs(Editor... editors){
        for(Editor timeEditor: editors){
            timeEditor.setFocusable(false);
            timeEditor.setClickable(true);
            timeEditor.setOnClickListener(v -> showDatePickerDialog(timeEditor));
        }
    }
}
