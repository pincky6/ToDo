package com.diplom.todoapp.eventtask.details.fragments;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentTaskDetailBinding;
import com.diplom.todoapp.eventtask.details.viewmodels.HolidayDetailViewModel;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.CategorySpinnerAdapter;
import com.diplom.todoapp.utils.EditorsUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;

public class HolidayDetailFragment extends AbstractTaskDetailFragment {
    private HolidayDetailViewModel taskDetailViewModel;
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
            taskDetailViewModel = new HolidayDetailViewModel(binding, dates, day);
        }
        else {
            taskDetailViewModel = new HolidayDetailViewModel(binding, id, dates);
        }
        EditorsUtil.initTextWatchers(binding.taskTitle,
                        binding.taskEditTextDate, binding.taskEditTextTime);
        EditorsUtil.setConnectedTextWatchers(binding.taskEditTextDate, binding.taskEditTextTime);
        initSpinners(binding.taskReminder, binding.taskPriority, binding.taskRepeat);
        initToolbar(binding.toolbar, binding.getRoot());
        initDateInputs(binding.taskEditTextDate);
        initTimeInputs(binding.taskEditTextTime);
        initAppBarCheck();
       // initCheckBox();
        initCategorySpinner();
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
        private void initAppBarCheck(){
            binding.toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.app_bar_check) {
                    try {
                        taskDetailViewModel.setTask(binding);
                    }
                    catch (IllegalArgumentException e){
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        EditorsUtil.setErrorBackground(binding.taskEditTextDate, binding.taskEditTextTime);
                        return false;
                    }
                    catch (IOException e){
                        EditorsUtil.setErrorBackground(binding.taskTitle, binding.taskDescribe,
                                binding.taskEditTextDate, binding.taskEditTextTime);
                        return false;
                    }
                    catch (InputMismatchException e){
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        EditorsUtil.setErrorState(binding.taskEditTextDate, binding.taskEditTextTime);
                        return false;
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TASK_DETAIL_KEY, taskDetailViewModel.getTask());
                    getParentFragmentManager().setFragmentResult(TASK_DETAIL_KEY, bundle);
                    findNavController(binding.getRoot()).popBackStack();
                }
                return false;
            });
    }
//    private void initCheckBox(){
//        binding.allDayCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if(isChecked){
//                binding.taskEditTextTime.setText("00:00");
//                binding.taskEditTextTime.setVisibility(View.INVISIBLE);
//            } else {
//                binding.taskEditTextTime.setVisibility(View.VISIBLE);
//            }
//        });
//    }
    private void initCategorySpinner(){
        ArrayList<String> categories = (ArrayList<String>) taskDetailViewModel.getCategory().clone();
        categories.add(getResources().getString(R.string.without_category_text));
        categories.add(getResources().getString(R.string.add_new_category_text));
        binding.categoriesSpinner.setAdapter(new CategorySpinnerAdapter(getContext(), binding.categoriesSpinner,0, categories));
    }
}