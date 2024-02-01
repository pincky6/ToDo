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
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.AbstractTaskAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.TaskAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.TaskDaysAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.TaskMonthAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.SuccsessFlag;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.eventtask.filter.TaskFilter;
import com.diplom.todoapp.eventtask.TaskFragmentDirections;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.listeners.OnTaskListener;
import com.diplom.todoapp.eventtask.listeners.OnResetTaskLisener;
import com.diplom.todoapp.utils.CalendarUtil;
import com.diplom.todoapp.utils.NotificationsUtil;
import com.diplom.todoapp.utils.SuccsessFlagUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class TaskListFragment extends Fragment {
    public static final String REQUEST_ADD_TASK = "ADD_TASK";
    public static final String REQUEST_REMOVE_TASK = "REMOVE_TASK";
    private FragmentTaskListBinding binding = null;
    private  TaskListViewModel taskListViewModel = new TaskListViewModel();
    private TaskFilter filter = new TaskFilter(31);
    private OnTaskListener onTaskListener = null;
    private OnResetTaskLisener onResetTaskLisener = null;
    public ArrayList<AbstractTask> getTaskList() {
        return taskListViewModel.taskList;
    }
    public int getFilterMask(){
        return filter.getMask();
    }
    public void setFilterMask(int mask){
        filter.setMask(mask);
    }
    public TaskFilter getFilter(){
        return filter;
    }
    public ArrayList<Date> getTaskDate(){
        ArrayList<Date> tasks = new ArrayList<>();
        for(AbstractTask abstractTask: taskListViewModel.taskList){
            if(abstractTask instanceof Task) tasks.add(abstractTask.dateStart);
        }
        return tasks;
    }
    public  void setFilter(TaskFilter filter){
        this.filter = filter;
    }
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
    public void setOnTaskListener(OnTaskListener listener){
        onTaskListener = listener;
    }
    public void setOnResetTaskLisener(OnResetTaskLisener lisener) {
        onResetTaskLisener = lisener;
    }
    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        resetDayAdapter();
        if(!taskListViewModel.isEmpty()) return;
        taskListViewModel.loadFirebase(binding.recyclerView, tasks -> {
            CalendarDay day = CalendarUtil.getCalendarDay(new Date());
            filter.setSelectedDay(day);
            resetAdapterList(filter.filter(tasks));
        });
    }
    public void addTask(AbstractTask newTask){
        assert newTask != null;
        AbstractTask oldTask = taskListViewModel.getFromId(newTask.id);
        if(oldTask == null) {
            taskListViewModel.addTask(newTask);
            if(onTaskListener != null) {
                onTaskListener.listen(newTask, REQUEST_ADD_TASK);
            }
        } else {
            taskListViewModel.remove(oldTask.id);
            NotificationsUtil.deleteNotification(getContext(), oldTask);
            taskListViewModel.addTask(newTask);
            if(onResetTaskLisener != null){
                onResetTaskLisener.listen(oldTask, newTask);
            }
        }
        NotificationsUtil.createNotification(getContext(), newTask);
        resetAdapterList(filter.filter(taskListViewModel.taskList));
    }
    private void removeTask(AbstractTask abstractTask){
        taskListViewModel.removeById(abstractTask.id);
        NotificationsUtil.deleteNotification(getContext(), abstractTask);
//        int color = PriorityUtil.getPriorityColor(abstractTask.priority);
//        for(AbstractTask task1: taskListViewModel.taskList){
//            if(task1.dateStart == abstractTask.dateStart &&
//                    PriorityUtil.getPriorityColor(task1.priority) == color){
//                return;
//            }
//        }
        resetAdapterList(filter.filter(taskListViewModel.taskList));
    }
    private void resetAdapterList(ArrayList<AbstractTask> tasks){
        if(binding == null)return;
        AbstractTaskAdapter taskAdapter = (AbstractTaskAdapter) Objects.requireNonNull(binding.recyclerView.getAdapter());
        tasks.sort((o1, o2) -> {
            int compareResult = o1.dateStart.compareTo(o2.dateStart);
            if(o1.allDayFlag && o2.allDayFlag) return compareResult;
            if(o1.allDayFlag && !o2.allDayFlag) return -1;
            if(!o1.allDayFlag && o2.allDayFlag) return 1;
            return compareResult;
        });
        taskAdapter.resetTaskList(tasks);
        taskAdapter.notifyDataSetChanged();
    }
    public void updateUi(){
        resetAdapterList(filter.filter(taskListViewModel.taskList));
    }
    public void showAllList(){
        resetAllListAdapter();
        resetAdapterList(taskListViewModel.taskList);
    }
    public void resetAllListAdapter(){
        binding.recyclerView.setAdapter(new TaskMonthAdapter(taskListViewModel.taskList,
                (AbstractTask task) ->
                {
                    if (task.id.split("-")[0].equals("Task")) {
                        findNavController(binding.getRoot()).navigate(
                                TaskFragmentDirections.showTaskDetailFragment(task.id, getTaskDate(), task.dateStart)
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
                    if(onTaskListener != null) {
                        onTaskListener.listen(task, REQUEST_REMOVE_TASK);
                    }
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }
                ,
                abstractTask ->
                {
                    abstractTask.succsessFlag = SuccsessFlagUtil.getStringFromFlag(SuccsessFlag.DONE);
                    taskListViewModel.updateTask(abstractTask);
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }));
    }
    public void resetMonthAdapter(){
        binding.recyclerView.setAdapter(new TaskDaysAdapter(filter.filter(taskListViewModel.taskList),
                (AbstractTask task) ->
                {
                    if (task.id.split("-")[0].equals("Task")) {
                        findNavController(binding.getRoot()).navigate(
                                TaskFragmentDirections.showTaskDetailFragment(task.id, getTaskDate(), task.dateStart)
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
                    if(onTaskListener != null) {
                        onTaskListener.listen(task, REQUEST_REMOVE_TASK);
                    }
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }
                ,
                abstractTask ->
                {
                    abstractTask.succsessFlag = SuccsessFlagUtil.getStringFromFlag(SuccsessFlag.DONE);
                    taskListViewModel.updateTask(abstractTask);
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }));
    }
    public void resetDayAdapter(){
        binding.recyclerView.setAdapter(new TaskAdapter(taskListViewModel.taskList,
                (AbstractTask task) ->
                {
                    if (task.id.split("-")[0].equals("Task")) {
                        findNavController(binding.getRoot()).navigate(
                                TaskFragmentDirections.showTaskDetailFragment(task.id, getTaskDate(), task.dateStart)
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
                    if(onTaskListener != null) {
                        onTaskListener.listen(task, REQUEST_REMOVE_TASK);
                    }
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }
                ,
                abstractTask ->
                {
                    abstractTask.succsessFlag = SuccsessFlagUtil.getStringFromFlag(SuccsessFlag.DONE);
                    taskListViewModel.updateTask(abstractTask);
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }));
    }
}