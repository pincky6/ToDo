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
import com.diplom.todoapp.utils.CalendarUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

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
    private CalendarDay day;
    private final TaskListener listener;
    private final OnRemoveListener removeListener;
    private final OnSetSuccsessListener onSetSuccsessListener;
    public TaskMonthAdapter(ArrayList<AbstractTask> taskList,
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
        binding.dayOfMonth.setTextSize(36);
        return new TaskListHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractTaskHolder holder, int position) {
        if(taskList.size() == 0) return;
        ((TaskListHolder)holder).bind(taskList.get(position).array, taskList.get(position).monthString,
                new TaskDaysAdapter(taskList.get(position).array, day, listener, removeListener, onSetSuccsessListener));
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
        ArrayList<AbstractTask> tasks = (ArrayList<AbstractTask>) taskListDays.clone();
        Calendar oldCal = Calendar.getInstance();
        tasks.sort(Comparator.comparing((AbstractTask abstractTask) -> abstractTask.dateStart));
        String monthString = null;
        if(tasks.size() > 0) {
            oldCal.setTime(tasks.get(0).dateStart);
            monthString = CalendarUtil.getMonthStringFromString(tasks.get(0).dateStart);
            taskList.add(new Pair(monthString, new ArrayList<>()));
        }
        for (AbstractTask task : tasks) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(task.dateStart);
            if(oldCal.get(Calendar.MONTH) != cal.get(Calendar.MONTH)){
                monthString = CalendarUtil.getMonthStringFromString(task.dateStart);
                taskList.add(new Pair(monthString, new ArrayList<>()));
                i++;
            }
            oldCal = cal;
            taskList.get(i).array.add(task);
        }
    }
}
