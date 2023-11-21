package com.diplom.todoapp.dialogs.fragments;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diplom.todoapp.EditorsUtil;
import com.diplom.todoapp.databinding.FragmentTaskDetailBinding;
import com.diplom.todoapp.dialogs.viewmodels.DateTaskDetailViewModel;
import com.diplom.todoapp.dialogs.viewmodels.TaskDetailViewModel;

import java.io.IOException;
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
        Bundle args = getArguments();
        String id = (String) args.get("taskID");
        if(id == null || id.isEmpty()) {
            taskDetailViewModel = new TaskDetailViewModel();
        }
        else {
            String key = (String) args.get("taskID");
            taskDetailViewModel = new TaskDetailViewModel(binding, key);
        }
        EditorsUtil.initTextWatchers(binding.taskTitle, binding.taskDescribe,
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
                    EditorsUtil.setErrorBackground(binding.taskTitle, binding.taskDescribe,
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
