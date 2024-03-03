package com.diplom.todoapp.notes.details;

import androidx.lifecycle.ViewModel;

import com.diplom.todoapp.databinding.FragmentNoteDetailBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.firebase.InitNoteDetailExpression;
import com.diplom.todoapp.firebase.InitNoteExpression;
import com.diplom.todoapp.firebase.OnDataReceivedListener;
import com.diplom.todoapp.notes.models.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class NoteDetailViewModel extends ViewModel {

    private FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
    private Note note;

    public Note getNote() {
        return note;
    }

    public NoteDetailViewModel(){
        note = new Note();
        note.id = firebaseRepository.generateKey();
    }
    public NoteDetailViewModel(String id, InitNoteDetailExpression init){
        firebaseRepository.getNoteFromKey(id, new OnDataReceivedListener() {
            @Override
            public void onDataReceived(Object data) {
                if(!(data instanceof Note)) return;
                note = (Note) data;
                if(init != null)
                init.init(note);
            }

            @Override
            public void onError(IllegalArgumentException databaseError) {

            }
        });
    }
    public void setNote(FragmentNoteDetailBinding binding){
        note.title = binding.noteTitle.getText().toString();
        note.category = binding.categoriesSpinner.getSelectedItem().toString();
        note.createDate = new Date();
    }
    public ArrayList<String> getCategory(){
        if(firebaseRepository == null) return new ArrayList<>();
        return firebaseRepository.getCategories();
    }
}
