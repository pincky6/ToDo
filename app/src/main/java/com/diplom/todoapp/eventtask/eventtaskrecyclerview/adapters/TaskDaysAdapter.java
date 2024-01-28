package com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.ItemListTasksBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.AbstractTaskHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.TaskListHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.listeners.RemoveListener;
import com.diplom.todoapp.eventtask.listeners.SetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class TaskDaysAdapter extends AbstractTaskAdapter {
    private ArrayList<ArrayList<AbstractTask>> taskList;
    private final TaskListener listener;
    private final RemoveListener removeListener;
    private final SetSuccsessListener setSuccsessListener;
    public SimpleDateFormat format = new SimpleDateFormat("dd MMMM");
    public TaskDaysAdapter(ArrayList<AbstractTask> taskList,
                       TaskListener listener,
                       RemoveListener removeListener,
                       SetSuccsessListener setSuccsessListener){
        this.taskList = new ArrayList<>();
        resetTaskList(taskList);
        this.listener = listener;
        this.removeListener = removeListener;
        this.setSuccsessListener = setSuccsessListener;
    }
    @NonNull
    @Override
    public TaskListHolder onCreateViewHolder(@NonNull ViewGroup parent, int taskType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemListTasksBinding binding = ItemListTasksBinding.inflate(inflater, parent, false);
        return new TaskListHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractTaskHolder holder, int position) {
        ((TaskListHolder)holder).bind(taskList.get(position),format.format(taskList.get(position).get(0).dateStart)
                ,new TaskAdapter(taskList.get(position), listener, removeListener, setSuccsessListener));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void resetTaskList(ArrayList<AbstractTask> taskListDays){
       if(taskListDays.isEmpty()){
           return;
       }
        if(taskList.size() > 0) {
            taskList.clear();
        }
        taskList.add(new ArrayList<>());
        ArrayList<AbstractTask> tasks = (ArrayList<AbstractTask>) taskListDays.clone();
        Calendar oldCal = Calendar.getInstance();
        tasks.sort(Comparator.comparing((AbstractTask abstractTask) -> abstractTask.dateStart));
        if(tasks.size() > 0) {
            oldCal.setTime(tasks.get(0).dateStart);
        }
        for (AbstractTask task : tasks) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(task.dateStart);
            if(oldCal.get(Calendar.DAY_OF_MONTH) != cal.get(Calendar.DAY_OF_MONTH)){
                taskList.add(new ArrayList<>());
            }
            oldCal = cal;
            taskList.get(taskList.size() - 1).add(task);
        }
    }
}
