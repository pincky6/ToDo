package com.diplom.todoapp.notes.holders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemNoteTextBinding;
import com.diplom.todoapp.notes.models.Note;
import com.diplom.todoapp.notes.models.NoteElement;

public class NotesTextHolder extends RecyclerView.ViewHolder {
    ItemNoteTextBinding binding;
    public NotesTextHolder(@NonNull ItemNoteTextBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(Note note, int position){
        binding.editText.setText(note.elements.get(position).element);
        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                note.title = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                note.elements.get(position).element = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
