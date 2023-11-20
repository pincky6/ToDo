package com.diplom.todoapp.dialogs.fragments;

import static androidx.navigation.ViewKt.findNavController;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.diplom.todoapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

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
    protected void initToolbar(MaterialToolbar toolbar){
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(getView()).navigate(
                        DateTaskDetailFragmentDirections.actionDateTaskDetailFragmentToEventTaskFragment()
                );
            }
        });
    }
    protected void showDatePickerDialog(EditText textInputEditText){
        DatePickerDialog datePicker = new DatePickerDialog(getContext());
        datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                String dateString = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        .format(selectedDate.getTime());
                textInputEditText.setText(dateString);
            }
        });
        datePicker.show();
    }
    protected void showTimePickerDialog(EditText textInputEditText){
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                textInputEditText.setText(time);
            }
        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
        timePicker.show();
    }
    protected <Editor extends EditText> void initTimeInputs(Editor... editors){
        for(Editor timeEditor: editors){
            timeEditor.setFocusable(false);
            timeEditor.setClickable(true);
            timeEditor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePickerDialog(timeEditor);
                }
            });
        }
    }
    protected <Editor extends EditText> void initDateInputs(Editor... editors){
        for(Editor timeEditor: editors){
            timeEditor.setFocusable(false);
            timeEditor.setClickable(true);
            timeEditor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(timeEditor);
                }
            });
        }
    }

    protected <Editor extends EditText> void initTextWatchers(Editor... editors){
        for(Editor editor: editors)
            editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editor.getText().toString().isEmpty()){
                    editor.setBackgroundResource(R.drawable.normal_background);
                    editor.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
        protected  <Editor extends EditText> void setErrorBackground(Editor... editors){
        for(Editor editor: editors){
            if(editor.getText().toString().isEmpty()){
                editor.setBackgroundResource(R.drawable.error_background);
                editor.setError("Empty");
            }
        }
    }
}
