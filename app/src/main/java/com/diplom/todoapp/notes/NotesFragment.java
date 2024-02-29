package com.diplom.todoapp.notes;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentNotesBinding;
import com.diplom.todoapp.eventtask.TaskFragmentDirections;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotesFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private FragmentNotesBinding binding;
    public void setBottomNavigationView(BottomNavigationView bottomNavigationView){
        this.bottomNavigationView = bottomNavigationView;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(bottomNavigationView != null){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        initFabButton();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    public void initFabButton(){
        binding.fabNotes.setOnClickListener(v -> {
            if(bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });
    }
}
