package com.diplom.todoapp.eventtask;

import static androidx.navigation.ViewKt.findNavController;

import com.diplom.todoapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.diplom.todoapp.databinding.FragmentEventTaskBinding;
import com.diplom.todoapp.details.fragments.AbstractTaskDetailFragment;
import com.diplom.todoapp.eventtask.calendar.decorator.MaterialCalendarFragment;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskListFragment;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.firebase.FirebaseRepository;

public class TaskFragment extends Fragment {
    private FragmentEventTaskBinding binding = null;
    private FirebaseRepository firebase;
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
                taskListFragment.showAllList();
                //binding.calendar.setSelectedDate((CalendarDay)null);
                // resetAdapterList(taskViewModel.taskList);
            }
            else if(item.getItemId() == R.id.action_settings){
                Toast.makeText(getContext(), "smthj", Toast.LENGTH_SHORT).show();
            }
            else if(item.getItemId() == R.id.action_filter){
                findNavController(getView()).navigate(
                        TaskFragmentDirections.showTaskFilterDialog(taskListFragment.getFilterMask())
                );
            }
            return false;
        });
        binding.fab.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if(id == R.id.add_new_task){
                    findNavController(binding.getRoot()).navigate(
                            TaskFragmentDirections.showTaskDetailFragment("")
                    );
                    return true;
                } else if (id == R.id.add_new_event) {
                    findNavController(binding.getRoot()).navigate(
                            TaskFragmentDirections.showDateTaskDetailFragment("")
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
            taskListFragment.setFilterMask(mask);
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
        materialCalendarFragment.setOnDayChangedListener(day -> {
            TaskFilter filter = taskListFragment.getFilter();
            filter.setSelectedDay(day);
            filter.setSelectedMonth(day.getMonth());
            taskListFragment.updateUi();
        });
        materialCalendarFragment.setOnMonthChangedListener((month) -> {
            TaskFilter filter = taskListFragment.getFilter();
            filter.setSelectedMonth(month);
            taskListFragment.updateUi();
        });
    }
}