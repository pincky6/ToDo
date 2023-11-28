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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.diplom.todoapp.databinding.FragmentEventTaskBinding;
import com.diplom.todoapp.dialogs.fragments.TaskDetailFragment;
import com.diplom.todoapp.eventtask.decorator.Decorators;
import com.diplom.todoapp.eventtask.decorator.TaskDayDecorator;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.firebase.FirebaseRepository;


import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class TaskFragment extends Fragment {
    private FragmentEventTaskBinding binding = null;
    private TaskViewModel taskViewModel;
    private FirebaseRepository firebase;
    private CalendarSingletone calendarSingletone;
    private Decorators decorators = new Decorators();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = FirebaseRepository.getInstance();
        taskViewModel = new TaskViewModel();

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventTaskBinding.inflate(inflater);
        initMenus();
        initRecyclerView();
        initFragmentResults();
        initCalendar();
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
    private void initMenus(){
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(v -> {
             firebase.signOut();
             findNavController(getView()).navigate(
                     TaskFragmentDirections.showLoginFragment()
             );
        });
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_show_task_list){
                binding.calendar.setSelectedDate((CalendarDay)null);
                resetAdapterList(taskViewModel.taskList);
            }
            else if(item.getItemId() == R.id.action_settings){
                Toast.makeText(getContext(), "smthj", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        binding.fab.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if(id == R.id.add_new_task){
                    findNavController(getView()).navigate(
                            TaskFragmentDirections.showTaskDetailFragment("")
                    );
                    return true;
                } else if (id == R.id.add_new_event) {
                    findNavController(getView()).navigate(
                            TaskFragmentDirections.showDateTaskDetailFragment("")
                    );
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }
    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new TaskAdapter(taskViewModel.taskList, (AbstractTask task) -> {
            if (task.id.split("-")[0].equals("Task")) {
                findNavController(binding.getRoot()).navigate(
                        TaskFragmentDirections.showTaskDetailFragment(task.id)
                );
            } else {
                findNavController(binding.getRoot()).navigate(
                        TaskFragmentDirections.showDateTaskDetailFragment(task.id)
                );
            }
        },
        id -> {
            AbstractTask task = taskViewModel.getFromId(id);
            removeTask(task);
            binding.recyclerView.getAdapter().notifyDataSetChanged();
        }));
        if(!taskViewModel.isEmpty()) return;
        taskViewModel.loadFirebase(binding.recyclerView, tasks -> {
            initDecorators();
            calendarSingletone = CalendarSingletone.initialize(taskViewModel.taskList);
            CalendarDay day = calendarSingletone.getCalendarDay(new Date());
            ArrayList<AbstractTask> showedTask;
            showedTask = taskViewModel.filterForMonth(day);
            resetAdapterList(showedTask);
        });
    }
    private void initFragmentResults(){
        getParentFragmentManager().setFragmentResultListener(TaskDetailFragment.TASK_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            AbstractTask abstractTask = (AbstractTask) result.get(requestKey);
            calendarSingletone = CalendarSingletone.initialize(taskViewModel.taskList);
            assert abstractTask != null;
            CalendarDay day = calendarSingletone.getCalendarDay(abstractTask.dateStart);
            int color = PriorityUtil.getPriorityColor(abstractTask.priority);
            if(calendarSingletone.containsKey(day)){
                calendarSingletone.get(day).add(color);
                decorators.removeDecorator(day, binding);
                decorators.addDecorator(new TaskDayDecorator(day, calendarSingletone.get(day)), binding);
            } else {
                HashSet<Integer> colors = new HashSet<>();
                colors.add(color);
                calendarSingletone.put(day, colors);
                decorators.addDecorator(new TaskDayDecorator(day, colors), binding);
            }
            firebase.addTask(abstractTask);
        });
    }
    private void initCalendar(){
        binding.calendar.setOnMonthChangedListener((widget, date) ->{
            ArrayList<AbstractTask> tasks;
            CalendarDay selectedDate = binding.calendar.getSelectedDate();
            calendarSingletone = CalendarSingletone.initialize(taskViewModel.taskList);
            if(selectedDate != null &&
                    selectedDate.getMonth() == date.getMonth() &&
                    selectedDate.getYear() == date.getYear()){
                tasks = taskViewModel.filterForDay(selectedDate);
            }
            else {
                tasks = taskViewModel.filterForMonth(date);
            }
            resetAdapterList(tasks);
        });

        binding.calendar.setOnDateChangedListener((widget, date, selected) -> {
            ArrayList<AbstractTask> tasks;
            calendarSingletone = CalendarSingletone.initialize(taskViewModel.taskList);
            tasks = taskViewModel.filterForDay(date);
            resetAdapterList(tasks);
        });
    }
    private void initDecorators(){
        calendarSingletone = CalendarSingletone.initialize(taskViewModel.taskList);
        Date date = new Date();
        CalendarDay day = calendarSingletone.getCalendarDay(date);
        for(Map.Entry<CalendarDay, HashSet<Integer>> entry: calendarSingletone.getDayTaskCalendarColors().entrySet()){
            decorators.addDecorator(new TaskDayDecorator(entry.getKey(), entry.getValue()));
        }
        binding.calendar.addDecorators(decorators.getDecorators());
    }
    private void removeTask(AbstractTask task){
        CalendarDay day = calendarSingletone.getCalendarDay(task.dateStart);
        int color = PriorityUtil.getPriorityColor(task.priority);
        calendarSingletone.removeFromDateTaskCalendarColors(day, color);
        firebase.removeTask(task.id);
        taskViewModel.remove(task.id);
        for(AbstractTask task1: taskViewModel.taskList){
            if(task1.dateStart == task.dateStart &&
               PriorityUtil.getPriorityColor(task1.priority) == color){
                return;
            }
        }
        decorators.removeDecorator(day, binding);
        if(calendarSingletone.containsKey(day)){
            decorators.addDecorator(new TaskDayDecorator(day, calendarSingletone.get(day)), binding);
        }
    }
    private void resetAdapterList(ArrayList<AbstractTask> tasks){
        TaskAdapter taskAdapter = (TaskAdapter)Objects.requireNonNull(binding.recyclerView.getAdapter());
        taskAdapter.resetTaskList(tasks);
        taskAdapter.notifyDataSetChanged();
    }
}