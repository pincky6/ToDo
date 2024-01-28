package com.diplom.todoapp.eventtask.calendar.decorator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.databinding.FragmentMaterialCalendarViewBinding;
import com.diplom.todoapp.eventtask.calendar.MaterialCalendarViewModel;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.listeners.OnDayChangedListener;
import com.diplom.todoapp.eventtask.listeners.OnMonthChangedListener;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.utils.CalendarUtil;
import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

public class MaterialCalendarFragment extends Fragment {
    private FragmentMaterialCalendarViewBinding binding = null;
    private MaterialCalendarViewModel model = null;
    private OnDayChangedListener dayChangedListener = null;
    private OnMonthChangedListener monthChangedListener = null;
    public Date getSelectedCalendarDay(){
        CalendarDay day = binding.calendar.getSelectedDate();
        if(day == null) return new Date();
        return CalendarUtil.getDate(binding.calendar.getSelectedDate());
    }
    public void setSelectedDay(CalendarDay day){
        model.setSelectedDay(day);
        binding.calendar.setSelectedDate(day);
    }
    public void setOnDayChangedListener(OnDayChangedListener dayChangedListener){
        this.dayChangedListener = dayChangedListener;
    }
    public void setOnMonthChangedListener(OnMonthChangedListener monthChangedListener){
        this.monthChangedListener = monthChangedListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new MaterialCalendarViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMaterialCalendarViewBinding.inflate(inflater, container, false);
        initCalendar();
        initCalendarFragmentResults();
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
    private void initCalendar(){
        FirebaseRepository firebase = FirebaseRepository.getInstance();
        ArrayList<AbstractTask> taskList = new ArrayList<>();
        firebase.readAllTasks(taskList, days -> {
            model.setTasksDecorators(days);
            binding.calendar.addDecorators(model.getTaskDayDecoratorList());
            binding.calendar.setSelectedDate(model.getSelectedDaySafe());
        });
    }
    private void initCalendarFragmentResults(){
        binding.calendar.setOnMonthChangedListener((MaterialCalendarView widget, CalendarDay date) ->{
            if(monthChangedListener != null) {
                monthChangedListener.listen(date.getMonth(), model);
            }
        });
        binding.calendar.setOnDateChangedListener((widget, date, selected) -> {
            if(dayChangedListener != null){
                dayChangedListener.listen(date, model);
            }
        });
    }
    public CalendarDay getSelectedDate(){
        return binding.calendar.getSelectedDate();
    }
    public void unselectDate(){
        binding.calendar.setSelectedDate((CalendarDay) null);
    }
    public void addNewTaskDecorator(AbstractTask abstractTask){
        if(abstractTask == null) return;
        CalendarDay day = CalendarUtil.getCalendarDay(abstractTask.dateStart);
        int color = PriorityUtil.getPriorityColor(abstractTask.priority);
        if(model.containsKey(day)){
            model.addColorByDay(day, color);
//            model.getDecorators().addDecorator(new TaskDayDecorator(day, model.get(day)), binding);
        } else {
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(color);
            model.put(day, colors);
            TaskDayDecorator dayDecorator = new TaskDayDecorator(day,
                    (HashSet<Integer>) colors.stream().collect(Collectors.toSet()));
            model.getDecorators().addDecorator(dayDecorator);
            binding.calendar.addDecorator(dayDecorator);
        }
        binding.calendar.addDecorators(model.getTaskDayDecoratorList());
    }
    public void removeTaskDecorator(AbstractTask abstractTask){
        if(abstractTask == null) return;
        int color = PriorityUtil.getPriorityColor(abstractTask.priority);
        CalendarDay day = CalendarUtil.getCalendarDay(abstractTask.dateStart);
        model.removeByColor(day, color);
        binding.calendar.removeDecorators();
        binding.calendar.addDecorators(model.getTaskDayDecoratorList());
    }
}
