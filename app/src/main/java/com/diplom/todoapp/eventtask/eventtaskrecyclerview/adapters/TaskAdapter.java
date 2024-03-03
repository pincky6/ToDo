package com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.AbstractTaskHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.TaskHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.TaskHolderFactory;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.HolderType;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Holiday;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.eventtask.listeners.OnSetSuccsessListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;
import com.diplom.todoapp.utils.CalendarUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;

public class TaskAdapter extends AbstractTaskAdapter {
    private ArrayList<AbstractTask> taskList;
    private Date day;
    private final TaskListener listener;
    private final OnRemoveListener removeListener;
    private final OnSetSuccsessListener onSetSuccsessListener;
    public TaskAdapter(ArrayList<AbstractTask> taskList,
                       CalendarDay day,
                       TaskListener listener,
                       OnRemoveListener removeListener,
                       OnSetSuccsessListener onSetSuccsessListener){
        this.taskList = taskList;
        this.day = CalendarUtil.getDate(day);
        this.listener = listener;
        this.removeListener = removeListener;
        this.onSetSuccsessListener = onSetSuccsessListener;
    }
    @Override
    public int getItemViewType(int position) {
            if(taskList.size() == 0){
                return HolderType.EMPTY_TASK.ordinal();
            }
            if(taskList.get(position) instanceof Holiday){
                return HolderType.TASK.ordinal();
            }
            else {
                return HolderType.DATE_TASK.ordinal();
            }
    }

    @NonNull
    @Override
    public AbstractTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int taskType) {
        return TaskHolderFactory.produce(parent, taskType);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractTaskHolder holder, int position) {
        if(taskList.isEmpty()) return;
        if(holder instanceof TaskHolder) ((TaskHolder) holder).bind(taskList.get(position), day, listener, removeListener, onSetSuccsessListener);
        holder.bind(taskList.get(position), listener, removeListener, onSetSuccsessListener);
    }

    @Override
    public int getItemCount() {
        return taskList.size() > 0 ? taskList.size() : 1;
    }

    @Override
    public void resetTaskList(ArrayList<AbstractTask> taskList){
        this.taskList = taskList;
    }
}
