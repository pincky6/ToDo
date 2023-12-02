package com.diplom.todoapp.searchs;

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
import com.diplom.todoapp.dialogs.DatePickerDialogFragment;
import com.diplom.todoapp.details.fragments.TaskDetailFragment;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskListViewModel;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.filter.TaskFilter;
import com.diplom.todoapp.firebase.FirebaseRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

public class TaskSearchFragment extends Fragment {
    FragmentTaskSearchBinding binding = null;
    SearchView searchView = null;
    TaskFilter filter = new TaskFilter(0);
    TaskListViewModel taskListViewModel = new TaskListViewModel();
    String searchedTitle = null;
    Boolean searchByDate = false;

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
        initAdapter(taskViewModel.taskList);
        taskViewModel.loadFirebase(binding.recyclerView, null);
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
                Date date = (Date)result.get(requestKey);
                taskViewModel. date;
                resetAdapterList(filter.filterByDate(taskViewModel.taskList, date));
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
                searchedTitle = newText;
                resetAdapterList(filter.filterByTitle(taskViewModel.taskList, searchedTitle));
                return false;
            }
        });
    }
    private void initAdapter(ArrayList<AbstractTask> list){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new TaskAdapter(list,
                (AbstractTask task) -> {
                    if (task.id.split("-")[0].equals("Task")) {
                        findNavController(binding.getRoot()).navigate(
                                TaskSearchFragmentDirections.showTaskDetailFromSearch(task.id)
                        );
                    } else {
                        findNavController(binding.getRoot()).navigate(
                                TaskSearchFragmentDirections.showDateTaskDetailFromSearch(task.id)
                        );
                    }
                },
                id -> {
                    AbstractTask task = taskViewModel.getFromId(id);
                    removeTask(task);
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }));
        getParentFragmentManager().setFragmentResultListener(TaskDetailFragment.TASK_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            AbstractTask abstractTask = (AbstractTask) result.get(requestKey);
            assert abstractTask != null;
            firebase.addTask(abstractTask);
            taskViewModel.loadFirebase(binding.recyclerView, null);
            if(searchByDate)
                resetAdapterList(filter.filterByDate(taskViewModel.taskList, choosedDay));
            else
                resetAdapterList(filter.filterByTitle(taskViewModel.taskList, searchedTitle));
        });
    }
    private void removeTask(AbstractTask task){
        firebase.removeTask(task.id);
        taskViewModel.remove(task.id);
        if(searchByDate)
            resetAdapterList(filter.filterByDate(taskViewModel.taskList, choosedDay));
        else
            resetAdapterList(filter.filterByTitle(taskViewModel.taskList, searchedTitle));
    }
    private void resetAdapterList(ArrayList<AbstractTask> tasks){
        TaskAdapter taskAdapter = (TaskAdapter) Objects.requireNonNull(binding.recyclerView.getAdapter());
        taskAdapter.resetTaskList(tasks);
        taskAdapter.notifyDataSetChanged();
    }
}
