package com.diplom.todoapp.eventtask;


import static androidx.navigation.ViewKt.findNavController;

import com.diplom.todoapp.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.diplom.todoapp.databinding.FragmentEventTaskBinding;
import com.diplom.todoapp.dialogs.fragments.DateTaskDetailFragment;
import com.diplom.todoapp.dialogs.fragments.TaskDetailFragment;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.firebase.FirebaseRepository;

import com.*;
import java.util.GregorianCalendar;

public class TaskFragment extends Fragment {
    private FragmentEventTaskBinding binding = null;
    private TaskViewModel taskViewModel;
    private FirebaseRepository firebase;
    private int lastScrollPosition = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = FirebaseRepository.getInstance();
        taskViewModel = new TaskViewModel();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventTaskBinding.inflate(inflater);

//        initialCalendarViewHeight = binding.calendar.getHeight();
        initMenus();
        initRecyclerView();
        initFragmentResults();
        initCalendar();
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










    private void initMenus(){

        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
         binding.toolbar.setNavigationOnClickListener(v -> {
             firebase.signOut();
             findNavController(getView()).navigate(
                     TaskFragmentDirections.showLoginFragment()
             );
         });

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_settings){
                Toast.makeText(getContext(), "smthj", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        binding.fab.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if(id == R.id.add_new_task){
                    findNavController(getView()).navigate(
                            TaskFragmentDirections.showTaskDetailFragment("")
                    );
                    return true;
                } else if (id == R.id.add_new_event) {
                    findNavController(getView()).navigate(
                            TaskFragmentDirections.showDateTaskDetailFragment("")
                    );
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }
    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new TaskAdapter(taskViewModel.taskList, (AbstractTask task) -> {
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
                id -> {
                    firebase.removeTask(id);
                    taskViewModel.remove(id);
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }));
        binding.recyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            Log.d("Y", lastScrollPosition + " " + oldScrollY);
            if(lastScrollPosition < oldScrollY) {
                binding.appBarLayout.animate().translationY(-(float)binding.appBarLayout.getHeight());
            }
            else {
                binding.appBarLayout.animate().translationY(0f);
            }
            lastScrollPosition = oldScrollY;
        });
        if(taskViewModel.isEmpty())
            taskViewModel.loadFirebase(binding.recyclerView);
    }
    private void initFragmentResults(){
        getParentFragmentManager().setFragmentResultListener(TaskDetailFragment.TASK_KEY, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Task task = (Task) result.get(requestKey);
                assert task != null;
                firebase.addTask(task);
                //binding.recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        getParentFragmentManager().setFragmentResultListener(DateTaskDetailFragment.DATE_TASK_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            DateTask dateTask = (DateTask) result.get(requestKey);
            assert dateTask != null;
            firebase.addTask(dateTask);
           // binding.recyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    private void initCalendar(){
//        binding.calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
//            GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth);
//            Log.d("Date", calendar.getTime().toString());
//        });
    }
}
