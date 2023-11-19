package com.diplom.todoapp.dialogs.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentTaskDetailBinding;

public class DateTaskDetailFragment extends Fragment {
    FragmentTaskDetailBinding binding = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false);

//        String[] reminders = new String[]{"1 day before", "5 minutes before", "Don\'t remind"};
//        ArrayAdapter<String> remindAdapter = new ArrayAdapter<>(getContext(), R.layout.fragment_task_detail, reminders);
//        binding.taskRemind.setAdapter(remindAdapter);
//
//        String[] priorities = new String[]{"Low", "Middle", "High"};
//        ArrayAdapter<String> prioritiyAdapter = new ArrayAdapter<>(getContext(), R.layout.fragment_task_detail, priorities);
//        binding.taskRemind.setAdapter(prioritiyAdapter);



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
}
