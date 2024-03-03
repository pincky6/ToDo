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
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.eventtask.listeners.OnSetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class TaskDaysAdapter extends AbstractTaskAdapter {
    private ArrayList<ArrayList<AbstractTask>> taskList;
    private CalendarDay day;
    private final TaskListener listener;
    private final OnRemoveListener removeListener;
    private final OnSetSuccsessListener onSetSuccsessListener;
    public SimpleDateFormat format = new SimpleDateFormat("dd MMMM");
    public TaskDaysAdapter(ArrayList<AbstractTask> taskList,
                           CalendarDay day,
                           TaskListener listener,
                           OnRemoveListener removeListener,
                           OnSetSuccsessListener onSetSuccsessListener){
        this.taskList = new ArrayList<>();
        this.day = day;
        resetTaskList(taskList);
        this.listener = listener;
        this.removeListener = removeListener;
        this.onSetSuccsessListener = onSetSuccsessListener;
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
        binding.dayOfMonth.setTextSize(18);
        return new TaskListHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractTaskHolder holder, int position) {
        if (taskList.size() == 0) return;
        String dateString = null;
        Calendar calDate = Calendar.getInstance(), today = Calendar.getInstance();
        calDate.setTime(taskList.get(position).get(0).dateStart);
        int res = calDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR);
        if (res == -1) {
            dateString = "Yesterday";
        } else if (res == 0) {
            dateString = "Today";
        }else if (res == 1){
            dateString = "Tomorrow";
        }else{
            dateString = format.format(taskList.get(position).get(0).dateStart);
        }
        ((TaskListHolder)holder).bind(taskList.get(position), dateString
                ,new TaskAdapter(taskList.get(position), day, listener, removeListener, onSetSuccsessListener));
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
