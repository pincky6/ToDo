package com.diplom.todoapp.eventtask;

import static androidx.navigation.ViewKt.findNavController;

import com.diplom.todoapp.BottomFragment;
import com.diplom.todoapp.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.diplom.todoapp.databinding.FragmentEventTaskBinding;
import com.diplom.todoapp.details.fragments.AbstractTaskDetailFragment;
import com.diplom.todoapp.eventtask.calendar.decorator.MaterialCalendarFragment;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskListFragment;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.filter.TaskFilter;
import com.diplom.todoapp.eventtask.filter.TaskFilterFragmentDialog;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class TaskFragment extends Fragment {
    private FragmentEventTaskBinding binding = null;
    private FirebaseRepository firebase;
    private BottomNavigationView bottomNavigationView = null;
    private TaskListFragment taskListFragment = null;
    private MaterialCalendarFragment materialCalendarFragment = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = FirebaseRepository.getInstance();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TASK_FRAGMENT", "onCreateView");
        binding = FragmentEventTaskBinding.inflate(inflater);
        initMenus();
        initUpdateTaskListListener();
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskListFragment = (TaskListFragment) getChildFragmentManager().findFragmentById(R.id.taskListFragment);
        materialCalendarFragment = (MaterialCalendarFragment)
                getChildFragmentManager().findFragmentById(R.id.calendarFragment);
        if(bottomNavigationView != null){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        initTaskListFragmentListeners();
        initMaterialCalendarFragment();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void initMenus(){
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(v -> {
            firebase.signOut();
            findNavController(getView()).popBackStack();
        });
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_show_task_list){
                taskListFragment.getFilter().setSelectedDay(null);
                materialCalendarFragment.unselectDate();
                taskListFragment.showAllList();
            }
            else if(item.getItemId() == R.id.action_settings){
                bottomNavigationView.setVisibility(View.GONE);
                findNavController(getView()).navigate(
                        TaskFragmentDirections.showSettingsFragment()
                );
            }
            else if(item.getItemId() == R.id.action_filter){
                bottomNavigationView.setVisibility(View.GONE);
                findNavController(getView()).navigate(
                        TaskFragmentDirections.showTaskFilterDialog(taskListFragment.getFilterMask(),
                                taskListFragment.getFilterCategories())
                );
            } else if (item.getItemId() == R.id.action_search_by_title){
                bottomNavigationView.setVisibility(View.GONE);
                findNavController(binding.getRoot()).navigate(
                        TaskFragmentDirections.showSearchFragment(false)
                );
            } else if (item.getItemId() == R.id.action_search_by_date){
                bottomNavigationView.setVisibility(View.GONE);
                findNavController(binding.getRoot()).navigate(
                        TaskFragmentDirections.showSearchFragment(true)
                );
            }

            return false;
        });
        binding.fab.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if(id == R.id.add_new_task) {
                    bottomNavigationView.setVisibility(View.GONE);
                    findNavController(binding.getRoot()).navigate(
                            TaskFragmentDirections.showTaskDetailFragment("",
                                                                          taskListFragment.getTaskDate(),
                                                                          materialCalendarFragment.getSelectedCalendarDay()
                                                                         )
                                                                  );
                    return true;
                } else if (id == R.id.add_new_event) {
                    bottomNavigationView.setVisibility(View.GONE);
                    findNavController(binding.getRoot()).navigate(
                            TaskFragmentDirections.showDateTaskDetailFragment("",
                                                                                        taskListFragment.getDateTaskDate(),
                                                                                        materialCalendarFragment.getSelectedCalendarDay())
                    );
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    private void initUpdateTaskListListener(){
        getParentFragmentManager().setFragmentResultListener(TaskFilterFragmentDialog.FILTER_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            Integer mask = (Integer)result.get(requestKey);
            ArrayList<String> categories = (ArrayList<String>)result.get(TaskFilterFragmentDialog.ARRAY_FILTER_KEY) ;
            taskListFragment.setFilterMask(mask);
            taskListFragment.setFilterCategories(categories);
            taskListFragment.updateUi();
        });
        getParentFragmentManager().setFragmentResultListener(AbstractTaskDetailFragment.TASK_DETAIL_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            AbstractTask abstractTask = (AbstractTask)result.get(requestKey);
            taskListFragment.addTask(abstractTask);
        });
    }

    private void initTaskListFragmentListeners(){
        taskListFragment.setOnTaskListener((abstractTask, requestKey) -> {
            if(requestKey.equals(TaskListFragment.REQUEST_ADD_TASK)) {
                materialCalendarFragment.addNewTaskDecorator(abstractTask);
            } else {
                materialCalendarFragment.removeTaskDecorator(abstractTask);
            }
        });
        taskListFragment.setOnResetTaskLisener((oldTask, newTask) -> {
            materialCalendarFragment.removeTaskDecorator(oldTask);
            materialCalendarFragment.addNewTaskDecorator(newTask);
        });
    }
    private void initMaterialCalendarFragment(){
        materialCalendarFragment.setOnDayChangedListener((day, model) -> {
            TaskFilter filter = taskListFragment.getFilter();
            filter.setSelectedMonth(day.getMonth());
            if(day.equals(model.getSelectedDayUnsafe())){
                model.setSelectedDay(null);
                filter.setSelectedDay(null);
                materialCalendarFragment.unselectDate();
                taskListFragment.resetMonthAdapter();
            } else {
                model.setSelectedDay(day);
                filter.setSelectedDay(day);
                taskListFragment.resetDayAdapter();
            }
            taskListFragment.updateUi();
        });
        materialCalendarFragment.setOnMonthChangedListener((month, model) -> {
            TaskFilter filter = taskListFragment.getFilter();
            CalendarDay calendarDay = model.getSelectedDayUnsafe();
            filter.setSelectedMonth(month);
            if(calendarDay != null && calendarDay.getMonth() == month){
                model.setSelectedDay(calendarDay);
                filter.setSelectedDay(calendarDay);
                taskListFragment.resetDayAdapter();
                taskListFragment.updateUi();
                return;
            }
            model.setSelectedDay(null);
            filter.setSelectedDay(null);
            taskListFragment.resetMonthAdapter();
            filter.setSelectedDay(calendarDay);
            model.setSelectedDay(calendarDay);
            taskListFragment.updateUi();
        });
    }
    public void setBottomNavigationView(BottomNavigationView bottomNavigationView){
        this.bottomNavigationView = bottomNavigationView;
    }
}