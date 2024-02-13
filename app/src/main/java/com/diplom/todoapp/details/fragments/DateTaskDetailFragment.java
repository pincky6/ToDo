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

import com.diplom.todoapp.databinding.FragmentDateTaskDetailBinding;
import com.diplom.todoapp.utils.EditorsUtil;
import com.diplom.todoapp.details.viewmodels.DateTaskDetailViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class DateTaskDetailFragment extends AbstractTaskDetailFragment {

    private DateTaskDetailViewModel dateTaskDetailViewModel;
    public FragmentDateTaskDetailBinding binding = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDateTaskDetailBinding.inflate(inflater, container, false);
        Bundle args = getArguments();
        String dateTaskId = (String)args.get("dateTaskID");
        if(dateTaskId == null || dateTaskId.isEmpty()) {
            dateTaskDetailViewModel = new DateTaskDetailViewModel();
        }
        else {
            String key = (String) args.get("dateTaskID");
            dateTaskDetailViewModel = new DateTaskDetailViewModel(binding, key);
        }
        EditorsUtil.initTextWatchers(binding.dateTaskTitle, binding.dateTaskEditTextDate,
                binding.dateTaskEditTextTime, binding.dateTaskEditTextDate2,
                binding.dateTaskEditTextTime2);
        initSpinners(binding.dateTaskReminder, binding.dateTaskPriority);
        initToolbar(binding.toolbar, binding.getRoot());
        initDateInputs(binding.dateTaskEditTextDate,
                        binding.dateTaskEditTextDate2);
        initTimeInputs(binding.dateTaskEditTextTime,
                binding.dateTaskEditTextTime2);
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
        binding.dateTaskSaveButton.setOnClickListener(v -> {
            try {
                dateTaskDetailViewModel.setTask(binding);
            }
            catch (IllegalArgumentException e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            catch (IOException e){
                EditorsUtil.setErrorBackground(binding.dateTaskTitle, binding.dateTaskDescribe,
                        binding.dateTaskPlace, binding.dateTaskEditTextDate,
                        binding.dateTaskEditTextTime, binding.dateTaskEditTextDate2,
                        binding.dateTaskEditTextTime2);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(TASK_DETAIL_KEY, dateTaskDetailViewModel.getDateTask());
            getParentFragmentManager().setFragmentResult(TASK_DETAIL_KEY, bundle);
            findNavController(binding.getRoot()).popBackStack();
        });
    }

    private void initCheckBox(){
        binding.allDayCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked){
                binding.dateTaskEditTextTime.setVisibility(View.VISIBLE);
                binding.dateTaskEditTextDate2.setVisibility(View.VISIBLE);
                binding.dateTaskEditTextTime2.setVisibility(View.VISIBLE);
                return;
            }
            binding.dateTaskEditTextTime.setVisibility(View.INVISIBLE);
            binding.dateTaskEditTextTime.setText("00:00");
            binding.dateTaskEditTextDate2.setVisibility(View.INVISIBLE);
            binding.dateTaskEditTextTime2.setVisibility(View.INVISIBLE);
            binding.dateTaskEditTextTime2.setText("00:00");
        });
    }
}
