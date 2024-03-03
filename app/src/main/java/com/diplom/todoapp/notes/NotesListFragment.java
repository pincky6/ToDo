package com.diplom.todoapp.notes;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.diplom.todoapp.databinding.FragmentListBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.notes.adapters.NotesAdapter;
import com.diplom.todoapp.notes.models.Note;

import java.util.Comparator;

public class NotesListFragment extends Fragment {
    FragmentListBinding binding;
    NotesListViewModel model = new NotesListViewModel();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        model.noteList.clear();
        model.loadFirebase(binding.recyclerView, notes -> {
            if(binding == null || binding.recyclerView.getAdapter() == null)
                return;
            model.noteList.sort((o1, o2) -> {
                if(o1.secure == o2.secure)
                    return 0;
                if(o1.secure && !o2.secure)
                    return -1;
                return  1;
            });
            binding.recyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addNote(Note newNote){
        assert newNote != null;
        Note oldNote = model.getFromId(newNote.id);
        if(oldNote == null) {
            model.addNote(newNote);
            model.add(newNote);
        } else {
            model.remove(oldNote.id);
            model.addNote(newNote);
        }
        updateUI();
    }
    public void updateUI(){
        if(binding.recyclerView.getAdapter() == null) return;
        binding.recyclerView.getAdapter().notifyDataSetChanged();
    }
    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(new NotesAdapter(model.noteList,
                note -> {
                        findNavController(binding.getRoot()).navigate(
                                NotesFragmentDirections.showNoteDetail(note.id)
                        );
                },
                id -> {
                    if(id == null) return;
                    Note note = model.getFromId(id);
                    model.removeById(note.id);
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                },
                note -> {
                    note.secure = !note.secure;
                    model.addNote(note);
                    model.noteList.sort((o1, o2) -> {
                        if(o1.secure == o2.secure)
                            return 0;
                        if(o1.secure && !o2.secure)
                            return -1;
                        return  1;
                    });
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }));
    }
}
