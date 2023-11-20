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
import com.diplom.todoapp.databinding.FragmentTaskDetailBinding;
import com.diplom.todoapp.dialogs.viewmodels.TaskDetailViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskDetailFragment extends AbstractTaskDetailFragment {
    private TaskDetailViewModel taskDetailViewModel;
    public FragmentTaskDetailBinding binding = null;
    public static final String TASK_KEY = "TASK_KEY";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskDetailViewModel = new TaskDetailViewModel();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false);
        initTextWatchers(binding.taskTitle, binding.taskDescribe,
                        binding.taskEditTextDate, binding.taskEditTextTime);
        initSpinners(binding.taskReminder, binding.taskPriority);
        initToolbar(binding.toolbar);
        initDateInputs(binding.taskEditTextDate);
        initTimeInputs(binding.taskEditTextTime);
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
        binding.taskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    taskDetailViewModel.setTask(binding);
                }
                catch (IllegalArgumentException e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                catch (IOException e){
                    setErrorBackground(binding.taskTitle, binding.taskDescribe,
                                        binding.taskEditTextDate, binding.taskEditTextTime);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(TASK_KEY, taskDetailViewModel.getTask());
                getParentFragmentManager().setFragmentResult(TASK_KEY, bundle);
                findNavController(getView()).navigate(
                        TaskDetailFragmentDirections.actionTaskDetailFragmentToEventTaskFragment()
                );
            }
        });
    }
}
