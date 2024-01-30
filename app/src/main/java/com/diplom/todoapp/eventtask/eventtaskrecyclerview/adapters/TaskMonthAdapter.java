package com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.ItemEmptyBinding;
import com.diplom.todoapp.databinding.ItemListTasksBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.AbstractTaskHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.EmptyHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.TaskListHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.listeners.RemoveListener;
import com.diplom.todoapp.eventtask.listeners.SetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TaskMonthAdapter extends AbstractTaskAdapter{
    class Pair{
        public Pair(String monthString, ArrayList<AbstractTask> array){
            this.monthString = monthString;
            this.array = array;
        }
        public String monthString;
        public ArrayList<AbstractTask> array;
    }
    private ArrayList<Pair> taskList;
    private final TaskListener listener;
    private final RemoveListener removeListener;
    private final SetSuccsessListener setSuccsessListener;
    public TaskMonthAdapter(ArrayList<AbstractTask> taskList,
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
    public AbstractTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int taskType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(taskList.size() == 0){
            ItemEmptyBinding binding = ItemEmptyBinding.inflate(inflater, parent, false);
            return new EmptyHolder(binding);
        }
        ItemListTasksBinding binding = ItemListTasksBinding.inflate(inflater, parent, false);
        binding.dayOfMonth.setTextSize(36);
        return new TaskListHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractTaskHolder holder, int position) {
        if(taskList.size() == 0) return;
        ((TaskListHolder)holder).bind(taskList.get(position).array, taskList.get(position).monthString,
                new TaskDaysAdapter(taskList.get(position).array, listener, removeListener, setSuccsessListener));
    }

    @Override
    public int getItemCount() {
        return taskList.size() > 0 ? taskList.size() : 1;
    }

    public void resetTaskList(ArrayList<AbstractTask> taskListDays){
        if(taskListDays.isEmpty()){
            return;
        }
        if(taskList.size() > 0) {
            taskList.clear();
        }
        int i = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
        ArrayList<AbstractTask> tasks = (ArrayList<AbstractTask>) taskListDays.clone();
        Calendar oldCal = Calendar.getInstance();
        tasks.sort(Comparator.comparing((AbstractTask abstractTask) -> abstractTask.dateStart));
        String monthString = null;
        if(tasks.size() > 0) {
            oldCal.setTime(tasks.get(0).dateStart);
            monthString = dateFormat.format(tasks.get(0).dateStart);
            taskList.add(new Pair(monthString, new ArrayList<>()));
        }
        for (AbstractTask task : tasks) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(task.dateStart);
            if(oldCal.get(Calendar.MONTH) != cal.get(Calendar.MONTH)){
                monthString = dateFormat.format(task.dateStart);
                taskList.add(new Pair(monthString, new ArrayList<>()));
                i++;
            }
            oldCal = cal;
            taskList.get(i).array.add(task);
        }
    }
}
