package com.diplom.todoapp.eventtask;

import androidx.lifecycle.ViewModel;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Priority;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class TaskViewModel extends ViewModel {
    public ArrayList<AbstractTask> taskList;
    TaskViewModel(){
        taskList = new ArrayList<>();
        Priority[] priorities = {Priority.LOW, Priority.MEDIUM, Priority.HIGH};
        Date date = new Date();
        for(int i = 0; i < 100; i++){
            if(i % 2 == 0)
            taskList.add(new Task(date, "Task #" + i,
                          "this is my task",
                    true, date,
                    date, priorities[i % 3]));
            else
                taskList.add(new DateTask(date,
                        "Task #" + i, "dom",
                        "this is my date task",
                        false, date,
                        date, date,
                        priorities[i % 3]));
        }
    }
}
