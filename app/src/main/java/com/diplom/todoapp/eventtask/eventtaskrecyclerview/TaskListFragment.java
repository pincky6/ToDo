package com.diplom.todoapp.eventtask.eventtaskrecyclerview;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.diplom.todoapp.databinding.FragmentTaskListBinding;
import com.diplom.todoapp.eventtask.TaskFragmentDirections;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.listeners.OnAbstractTaskDecoratorListener;
import com.diplom.todoapp.utils.CalendarUtil;
import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class TaskListFragment extends Fragment {
    public static final String TASK_LIST_KEY = "TASK_LIST_KEY";
    public static final String REQUEST_ADD_TASK = "ADD_TASK";
    public static final String REQUEST_REMOVE_TASK = "REMOVE_TASK";

    private FragmentTaskListBinding binding = null;
    private TaskListViewModel taskListViewModel = new TaskListViewModel();
    private OnAbstractTaskDecoratorListener abstractTaskDecoratorListener = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskListBinding.inflate(inflater, container, false);
        initRecyclerView();
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
    public void setAbstractTaskDecoratorListener(OnAbstractTaskDecoratorListener listener){
        abstractTaskDecoratorListener = listener;
    }
    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new TaskAdapter(taskListViewModel.taskList, (AbstractTask task) ->
        {
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
                id ->
                {
                    if(id == null) return;
                    AbstractTask task = taskListViewModel.getFromId(id);
                    removeTask(task);
                    if(abstractTaskDecoratorListener != null) {
                        abstractTaskDecoratorListener.run(task, REQUEST_REMOVE_TASK);
                    }
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }));
        if(!taskListViewModel.isEmpty()) return;
        taskListViewModel.loadFirebase(binding.recyclerView, tasks -> {
            CalendarDay day = CalendarUtil.getCalendarDay(new Date());
            ArrayList<AbstractTask> showedTask;
            showedTask = taskListViewModel.filterForMonth(day);
            resetAdapterList(showedTask);
        });
    }
    public void addNewTask(AbstractTask abstractTask){
        assert abstractTask != null;
        Bundle bundle = new Bundle();
        bundle.putSerializable(REQUEST_ADD_TASK, abstractTask);
        getParentFragmentManager().setFragmentResult(TASK_LIST_KEY, bundle);
        taskListViewModel.addTask(abstractTask);
        if(abstractTaskDecoratorListener != null) {
            abstractTaskDecoratorListener.run(abstractTask, REQUEST_ADD_TASK);
        }
        resetAdapterList(taskListViewModel.taskList);
    }
    private void removeTask(AbstractTask abstractTask){
        taskListViewModel.removeById(abstractTask.id);
        int color = PriorityUtil.getPriorityColor(abstractTask.priority);
        for(AbstractTask task1: taskListViewModel.taskList){
            if(task1.dateStart == abstractTask.dateStart &&
                    PriorityUtil.getPriorityColor(task1.priority) == color){
                return;
            }
        }
        resetAdapterList(taskListViewModel.taskList);
    }
    private void resetAdapterList(ArrayList<AbstractTask> tasks){
        TaskAdapter taskAdapter = (TaskAdapter) Objects.requireNonNull(binding.recyclerView.getAdapter());
        taskAdapter.resetTaskList(tasks);
        taskAdapter.notifyDataSetChanged();
    }
}

