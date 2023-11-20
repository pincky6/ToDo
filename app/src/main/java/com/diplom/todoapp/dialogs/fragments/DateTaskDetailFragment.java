package com.diplom.todoapp.dialogs.fragments;

import static androidx.navigation.ViewKt.findNavController;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentDateTaskDetailBinding;
import com.diplom.todoapp.dialogs.viewmodels.DateTaskDetailViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateTaskDetailFragment extends AbstractTaskDetailFragment {

    private DateTaskDetailViewModel dateTaskDetailViewModel;
    public FragmentDateTaskDetailBinding binding = null;
    public static final String DATE_TASK_KEY = "DATE_TASK_KEY";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateTaskDetailViewModel = new DateTaskDetailViewModel();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDateTaskDetailBinding.inflate(inflater, container, false);
        initTextWatchers(binding.dateTaskTitle, binding.dateTaskDescribe,
                binding.dateTaskPlace, binding.dateTaskEditTextDate,
                binding.dateTaskEditTextTime, binding.dateTaskEditTextDate2,
                binding.dateTaskEditTextTime2);
        initSpinners(binding.dateTaskReminder, binding.dateTaskPriority);
        initToolbar(binding.toolbar);
        initDateInputs(binding.dateTaskEditTextDate,
                        binding.dateTaskEditTextDate2);
        initTimeInputs(binding.dateTaskEditTextTime,
                binding.dateTaskEditTextTime2);
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
        private void initSaveButton(){
        binding.dateTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dateTaskDetailViewModel.setTask(binding);
                }
                catch (IllegalArgumentException e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                catch (IOException e){
                    setErrorBackground(binding.dateTaskTitle, binding.dateTaskDescribe,
                            binding.dateTaskPlace, binding.dateTaskEditTextDate,
                            binding.dateTaskEditTextTime, binding.dateTaskEditTextDate2,
                            binding.dateTaskEditTextTime2);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(DATE_TASK_KEY, dateTaskDetailViewModel.getDateTask());
                getParentFragmentManager().setFragmentResult(DATE_TASK_KEY, bundle);
                findNavController(getView()).navigate(
                        DateTaskDetailFragmentDirections.actionDateTaskDetailFragmentToEventTaskFragment()
                );
            }
        });
    }
}
