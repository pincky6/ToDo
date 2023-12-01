package com.diplom.todoapp.eventtask.calendar.decorator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.databinding.FragmentMaterialCalendarViewBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.utils.CalendarUtil;
import com.diplom.todoapp.utils.PriorityUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.HashSet;

public class MaterialCalendarFragment extends Fragment {
    private static final String MONTH_KEY = "MONTH_KEY";
    private static final String DAY_KEY = "DAY_KEY";
    private FragmentMaterialCalendarViewBinding binding = null;
    private MaterialCalendarViewModel model = null;
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
            model = new MaterialCalendarViewModel(days);
            binding.calendar.addDecorators(model.getTaskDayDecoratorList());
        });
    }
    private void initCalendarFragmentResults(){
        binding.calendar.setOnMonthChangedListener((MaterialCalendarView widget, CalendarDay date) ->{
            CalendarDay selectedDate = binding.calendar.getSelectedDate();
            Bundle bundle = new Bundle();
            if(selectedDate == null){
                bundle.putSerializable(DAY_KEY, null);
            } else {
                bundle.putSerializable(DAY_KEY, selectedDate.getDate());
            }
            bundle.putInt(MONTH_KEY, date.getMonth());
            getParentFragmentManager().setFragmentResult(MONTH_KEY, bundle);
        });

        binding.calendar.setOnDateChangedListener((widget, date, selected) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(DAY_KEY, date.getDate());
            getParentFragmentManager().setFragmentResult(DAY_KEY, bundle);
        });
    }
    public void addNewTaskDecorator(AbstractTask abstractTask){
        if(abstractTask == null) return;
        CalendarDay day = CalendarUtil.getCalendarDay(abstractTask.dateStart);
        int color = PriorityUtil.getPriorityColor(abstractTask.priority);
        if(model.containsKey(day)){
            model.addColorByDay(day, color);
//            model.getDecorators().addDecorator(new TaskDayDecorator(day, model.get(day)), binding);
        } else {
            HashSet<Integer> colors = new HashSet<>();
            colors.add(color);
            model.put(day, colors);
            TaskDayDecorator dayDecorator = new TaskDayDecorator(day, colors);
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
