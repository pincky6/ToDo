package com.diplom.todoapp.notes.details;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentNoteDetailBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.CategorySpinnerAdapter;
import com.diplom.todoapp.firebase.InitNoteDetailExpression;
import com.diplom.todoapp.firebase.InitNoteExpression;
import com.diplom.todoapp.notes.adapters.NotesDetailAdapter;
import com.diplom.todoapp.notes.models.Note;
import com.diplom.todoapp.notes.models.NoteElement;
import com.diplom.todoapp.utils.EditorsUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;


public class NoteDetailFragment extends Fragment {
    public static final String NOTE_KEY = "NOTE_KEY";
    public FragmentNoteDetailBinding binding = null;
    public NoteDetailViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false);
        Bundle args = getArguments();
        String noteID = (String)args.get("noteID");
        if(noteID.isEmpty()){
            model = new NoteDetailViewModel();
        } else {
            model = new NoteDetailViewModel(noteID, note -> {
                binding.noteTitle.setText(model.getNote().title);
                initRecyclerView();
            });
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(binding.toolbar, binding.getRoot());
        initToolbarCheck();
        initCategorySpinner();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new NotesDetailAdapter(model.getNote()));
    }
    private void initToolbar(MaterialToolbar toolbar, View parentView){
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> findNavController(parentView).popBackStack());
    }
    private void initToolbarCheck(){
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.app_bar_check) {
                try {
                    model.setNote(binding);
                }
                catch (IllegalArgumentException e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return false;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(NOTE_KEY, model.getNote());
                getParentFragmentManager().setFragmentResult(NOTE_KEY, bundle);
                findNavController(binding.getRoot()).popBackStack();
            }
            return false;

        });
    }
    private void initCategorySpinner(){
        ArrayList<String> categories = (ArrayList<String>) model.getCategory().clone();
        categories.add(getResources().getString(R.string.without_category_text));
        categories.add(getResources().getString(R.string.add_new_category_text));
        binding.categoriesSpinner.setAdapter(new CategorySpinnerAdapter(getContext(), binding.categoriesSpinner,0, categories));
    }
}
