package com.diplom.todoapp.details.fragments;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentDateTaskDetailBinding;
import com.diplom.todoapp.details.viewmodels.TaskDetailViewModel;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.CategorySpinnerAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.SubtasksAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Subtask;
import com.diplom.todoapp.utils.EditorsUtil;
import com.diplom.todoapp.details.viewmodels.DateTaskDetailViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;

public class DateTaskDetailFragment extends AbstractTaskDetailFragment {

    private DateTaskDetailViewModel dateTaskDetailViewModel;
    public FragmentDateTaskDetailBinding binding = null;
    private boolean show = false;
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
        ArrayList<Date> dates = (ArrayList<Date>) args.get("taskDates");
        Date day = ((Date) args.get("selectedDate"));
        if(dateTaskId == null || dateTaskId.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(day);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            binding.dateTaskEditTextDate.setText(format.format(day));
            binding.dateTaskEditTextTime.setText("00:00");
            binding.dateTaskEditTextDate2.setText(format.format(calendar.getTime()));
            binding.dateTaskEditTextTime2.setText("00:00");
            dateTaskDetailViewModel = new DateTaskDetailViewModel(binding, dates, day);
        }
        else {
            String key = (String) args.get("dateTaskID");
            dateTaskDetailViewModel = new DateTaskDetailViewModel(binding, key);
        }
        EditorsUtil.initTextWatchers(binding.dateTaskTitle, binding.dateTaskEditTextDate,
                binding.dateTaskEditTextTime, binding.dateTaskEditTextDate2,
                binding.dateTaskEditTextTime2);
        initSpinners(binding.dateTaskReminder, binding.dateTaskPriority, binding.dateTaskRepeat);
        initToolbar(binding.toolbar, binding.getRoot());
        initDateInputs(binding.dateTaskEditTextDate,
                        binding.dateTaskEditTextDate2);
        initTimeInputs(binding.dateTaskEditTextTime,
                binding.dateTaskEditTextTime2);
        initAppBarCheck();
        initCheckBox();
        initCategorySpinner();
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.subtasksRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.subtasksRecyclerView.setAdapter(new SubtasksAdapter(null, true));
        initShowSubtasksButton();
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
        private void initAppBarCheck(){
            binding.toolbar.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.app_bar_check) {
                            try {
                                dateTaskDetailViewModel.setTask(binding);
                            }
                            catch (IllegalArgumentException e){
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                return false;
                            }
                            catch (IOException e){
                                EditorsUtil.setErrorBackground(binding.dateTaskTitle, binding.dateTaskDescribe,
                                        binding.dateTaskPlace, binding.dateTaskEditTextDate,
                                        binding.dateTaskEditTextTime, binding.dateTaskEditTextDate2,
                                        binding.dateTaskEditTextTime2);
                                return false;
                            }
                            catch (InputMismatchException e){
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                EditorsUtil.setErrorState(binding.dateTaskEditTextDate, binding.dateTaskEditTextTime,
                                                          binding.dateTaskEditTextDate2, binding.dateTaskEditTextTime2);
                                return false;
                            }
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(TASK_DETAIL_KEY, dateTaskDetailViewModel.getDateTask());
                            getParentFragmentManager().setFragmentResult(TASK_DETAIL_KEY, bundle);
                            findNavController(binding.getRoot()).popBackStack();
                        }
                        return false;
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
    private void initShowSubtasksButton(){
        binding.showSubtasksButton.setOnClickListener(v -> {
            if(!show){
                binding.showSubtasksButton.setImageResource(R.drawable.baseline_expand_more_24);
                ((SubtasksAdapter)binding.subtasksRecyclerView.getAdapter()).setSubtasks(dateTaskDetailViewModel.getDateTask().subtasks);
                binding.subtasksRecyclerView.getAdapter().notifyDataSetChanged();
                binding.subtasksRecyclerView.setVisibility(View.VISIBLE);
                show = true;
            } else {
                binding.showSubtasksButton.setImageResource(R.drawable.baseline_expand_less_24);
                ((SubtasksAdapter)binding.subtasksRecyclerView.getAdapter()).setSubtasks(null);
                binding.subtasksRecyclerView.setVisibility(View.INVISIBLE);
                binding.subtasksRecyclerView.getAdapter().notifyDataSetChanged();
                show = false;
            }
        });
    }
    private void initCategorySpinner(){
        ArrayList<String> categories = (ArrayList<String>) dateTaskDetailViewModel.getCategory().clone();
        categories.add(getResources().getString(R.string.without_category_text));
        categories.add(getResources().getString(R.string.add_new_category_text));
        binding.categoriesSpinner.setAdapter(new CategorySpinnerAdapter(getContext(), binding.categoriesSpinner,0, categories));
    }
}
