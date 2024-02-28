package com.diplom.todoapp.eventtask.searchs;

import static androidx.navigation.ViewKt.findNavController;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentTaskSearchBinding;
import com.diplom.todoapp.details.fragments.AbstractTaskDetailFragment;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.TaskAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskListViewModel;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.SuccsessFlag;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Holiday;
import com.diplom.todoapp.eventtask.filter.TaskFilter;
import com.diplom.todoapp.utils.CalendarUtil;
import com.diplom.todoapp.utils.SuccsessFlagUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TaskSearchFragment extends Fragment {
    FragmentTaskSearchBinding binding = null;
    SearchView searchView = null;
    TaskFilter filter = new TaskFilter(31, new ArrayList<>());
    TaskListViewModel taskListViewModel = new TaskListViewModel();
    String searchedTitle = null;
    Boolean searchByDate = false;
    public ArrayList<Date> getTaskDate(){
        ArrayList<Date> tasks = new ArrayList<>();
        for(AbstractTask abstractTask: taskListViewModel.taskList){
            if(abstractTask instanceof Holiday) tasks.add(abstractTask.dateStart);
        }
        return tasks;
    }
    public ArrayList<Date> getDateTaskDate(){
        ArrayList<Date> tasks = new ArrayList<>();
        for(AbstractTask abstractTask: taskListViewModel.taskList){
            if(abstractTask instanceof DateTask) {
                DateTask task = (DateTask)abstractTask;
                tasks.add(task.dateStart);
                tasks.add(task.dateEnd);
            }
        }
        return tasks;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        searchView = binding.toolbar.findViewById(R.id.app_bar_search);
        searchByDate = (Boolean)args.get("searchByDate");
        initMenus(searchByDate);
        initAdapter(taskListViewModel.taskList);
        taskListViewModel.loadFirebase(binding.recyclerView, null);
        initCalendar(searchByDate);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
    private void initMenus(Boolean searchByDate){
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(v -> {
            findNavController(binding.getRoot()).popBackStack();
        });
        if(searchByDate){
            searchView.setOnSearchClickListener(view->{
                findNavController(binding.getRoot()).navigate(
                        TaskSearchFragmentDirections.showDatePicker()
                );
            });
        }
    }
    private void initCalendar(Boolean searchByDate){
        if(searchByDate){
            getParentFragmentManager().setFragmentResultListener(DatePickerDialogFragment.DATE_PICKER_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
                CalendarDay date = CalendarUtil.getCalendarDay((Date)result.get(requestKey));
                filter.setSelectedDay(date);
                filter.setSelectedMonth(date.getMonth());
                resetAdapterList(filter.filter(taskListViewModel.taskList));
                searchView.setIconified(true);
            });
            return;
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter.setSelectedTitle(newText);
                resetAdapterList(filter.filterByTitle(taskListViewModel.taskList));
                return false;
            }
        });
    }
    private void initAdapter(ArrayList<AbstractTask> list){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new TaskAdapter(list, filter.getSelectedDay(),
                (AbstractTask task) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(0);
                    if (task.id.split("-")[0].equals("Task")) {
                        findNavController(binding.getRoot()).navigate(
                                TaskSearchFragmentDirections.showTaskDetailFromSearch(task.id, getTaskDate(), task.dateStart)
                        );
                    } else {
                        findNavController(binding.getRoot()).navigate(
                                TaskSearchFragmentDirections.showDateTaskDetailFromSearch(task.id, getDateTaskDate(), task.dateStart)
                        );
                    }
                },
                id -> {
                    AbstractTask task = taskListViewModel.getFromId(id);
                    removeTask(task);
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                },
                abstractTask ->
                {
                    abstractTask.succsessFlag = SuccsessFlagUtil.getStringFromFlag(SuccsessFlag.DONE);
                    taskListViewModel.updateTask(abstractTask);
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }));
        getParentFragmentManager().setFragmentResultListener(AbstractTaskDetailFragment.TASK_DETAIL_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            AbstractTask resetedTask = (AbstractTask) result.get(requestKey);
            assert resetedTask != null;
            AbstractTask oldTask = taskListViewModel.getFromId(resetedTask.id);
            taskListViewModel.remove(oldTask.id);
            taskListViewModel.addTask(resetedTask);
            if(searchByDate)
                resetAdapterList(filter.filter(taskListViewModel.taskList));
            else
                resetAdapterList(filter.filterByTitle(taskListViewModel.taskList));
        });
    }

    private void removeTask(AbstractTask abstractTask){
        taskListViewModel.removeById(abstractTask.id);
        if(searchByDate)
            resetAdapterList(filter.filter(taskListViewModel.taskList));
        else
            resetAdapterList(filter.filterByTitle(taskListViewModel.taskList));
    }
    private void resetAdapterList(ArrayList<AbstractTask> tasks){
        TaskAdapter taskAdapter = (TaskAdapter) Objects.requireNonNull(binding.recyclerView.getAdapter());
        taskAdapter.resetTaskList(tasks);
        taskAdapter.notifyDataSetChanged();
    }
}
