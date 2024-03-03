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
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.TaskListFragment;
import com.diplom.todoapp.eventtask.filter.TaskFilterFragmentDialog;
import com.diplom.todoapp.notes.details.NoteDetailFragment;
import com.diplom.todoapp.notes.models.Note;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class NotesFragment extends Fragment {
    private FragmentNotesBinding binding;
    private  NotesListFragment notesListFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notesListFragment = (NotesListFragment) getChildFragmentManager().findFragmentById(R.id.noteListFragment);
        initFabButton();
        getParentFragmentManager().setFragmentResultListener(NoteDetailFragment.NOTE_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            Note note = (Note)result.get(NoteDetailFragment.NOTE_KEY) ;
            notesListFragment.addNote(note);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    public void initFabButton(){
        binding.fabNotes.setOnClickListener(v -> {
            findNavController(getView()).navigate(
                    NotesFragmentDirections.showNoteDetail("")
            );
        });
    }
    public void showFabButton(){
        binding.fabNotes.setVisibility(View.VISIBLE);
    }
    public void hideFabButton(){
        binding.fabNotes.setVisibility(View.GONE);
    }
}
