package com.diplom.todoapp.details.fragments;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentTaskDetailBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.utils.EditorsUtil;
import com.diplom.todoapp.details.viewmodels.TaskDetailViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;

public class TaskDetailFragment extends AbstractTaskDetailFragment {
    private TaskDetailViewModel taskDetailViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public FragmentTaskDetailBinding binding = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false);
        Bundle args = getArguments();
        String id = (String) args.get("taskID");
        ArrayList<Date> dates = (ArrayList<Date>) args.get("taskDates");
        Date day = ((Date) args.get("selectedDate"));
        if((id == null && day.getTime() != 0)|| id.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            binding.taskEditTextDate.setText(format.format(day));
            taskDetailViewModel = new TaskDetailViewModel(binding, dates, day);
        }
        else {
            taskDetailViewModel = new TaskDetailViewModel(binding, id, dates);
        }
        EditorsUtil.initTextWatchers(binding.taskTitle,
                        binding.taskEditTextDate, binding.taskEditTextTime);
        EditorsUtil.setConnectedTextWatchers(binding.taskEditTextDate, binding.taskEditTextTime);
        initSpinners(binding.taskReminder, binding.taskPriority);
        initToolbar(binding.toolbar, binding.getRoot());
        initDateInputs(binding.taskEditTextDate);
        initTimeInputs(binding.taskEditTextTime);
        initSaveButton();
        initCheckBox();
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
        binding.taskSaveButton.setOnClickListener(v -> {
            try {
                taskDetailViewModel.setTask(binding);
            }
            catch (IllegalArgumentException e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                EditorsUtil.setErrorBackground(binding.taskEditTextDate, binding.taskEditTextTime);
                return;
            }
            catch (IOException e){
                EditorsUtil.setErrorBackground(binding.taskTitle, binding.taskDescribe,
                                    binding.taskEditTextDate, binding.taskEditTextTime);
                return;
            }
            catch (InputMismatchException e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                EditorsUtil.setErrorState(binding.taskEditTextDate, binding.taskEditTextTime);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(TASK_DETAIL_KEY, taskDetailViewModel.getTask());
            getParentFragmentManager().setFragmentResult(TASK_DETAIL_KEY, bundle);
            findNavController(binding.getRoot()).popBackStack();
        });
    }
    private void initCheckBox(){
        binding.allDayCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                binding.taskEditTextTime.setText("00:00");
                binding.taskEditTextTime.setVisibility(View.INVISIBLE);
            } else {
                binding.taskEditTextTime.setVisibility(View.VISIBLE);
            }
        });
    }
}
