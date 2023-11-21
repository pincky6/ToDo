package com.diplom.todoapp.eventtask;


import static androidx.navigation.ViewKt.findNavController;

import com.diplom.todoapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.diplom.todoapp.databinding.FragmentEventTaskBinding;
import com.diplom.todoapp.dialogs.fragments.DateTaskDetailFragment;
import com.diplom.todoapp.dialogs.fragments.DateTaskDetailFragmentDirections;
import com.diplom.todoapp.dialogs.fragments.TaskDetailFragment;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.google.android.material.appbar.MaterialToolbar;

public class TaskFragment extends Fragment {
    private FragmentEventTaskBinding binding = null;
    private TaskViewModel taskViewModel;
    private FirebaseRepository firebase;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = FirebaseRepository.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventTaskBinding.inflate(inflater);
        initMenus();
        initRecyclerView();
        initFragmentResults();
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
         binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebase.signOut();
                    findNavController(getView()).navigate(
                            TaskFragmentDirections.actionEventTaskFragmentToLoginFragment()
                    );
                }
            });

        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_settings){
                    Toast.makeText(getContext(), "smthj", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int id = item.getItemId();
                                if(id == R.id.add_new_task){
                                    findNavController(getView()).navigate(
                                            TaskFragmentDirections.actionEventTaskFragmentToTaskDetailFragment("")
                                    );
                                    return true;
                                } else if (id == R.id.add_new_event) {
                                    findNavController(getView()).navigate(
                                            TaskFragmentDirections.actionEventTaskFragmentToDateTaskDetailFragment("")
                                    );
                                    return true;
                                }
                                return false;
                            }
                        });
                popupMenu.show();
            }
        });
    }
    private void initRecyclerView(){
        taskViewModel = new TaskViewModel();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new TaskAdapter(taskViewModel.taskList, (AbstractTask task) -> {
            if(task.id.split("-")[0].equals("Task")){
                findNavController(getView()).navigate(
                        TaskFragmentDirections.actionEventTaskFragmentToTaskDetailFragment(task.id)
                );
            }
            else{
                findNavController(getView()).navigate(
                        TaskFragmentDirections.actionEventTaskFragmentToDateTaskDetailFragment(task.id)
                );
            }
        }));
        if(taskViewModel.isEmpty())
            taskViewModel.loadFirebase(binding.recyclerView);
    }
    private void initFragmentResults(){
        getParentFragmentManager().setFragmentResultListener(TaskDetailFragment.TASK_KEY, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Task task = (Task) result.get(requestKey);
                assert task != null;
                firebase.addTask(task.id, task);
                //binding.recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        getParentFragmentManager().setFragmentResultListener(DateTaskDetailFragment.DATE_TASK_KEY, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                DateTask dateTask = (DateTask) result.get(requestKey);
                assert dateTask != null;
                firebase.addTask(dateTask.id, dateTask);
               // binding.recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }
}
