package com.diplom.todoapp.notes.holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemNoteImageBinding;
import com.diplom.todoapp.notes.models.Note;
import com.diplom.todoapp.notes.models.NoteElement;

public class NotesImageHolder extends RecyclerView.ViewHolder {
    ItemNoteImageBinding binding;
    public NotesImageHolder(@NonNull ItemNoteImageBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(Note note, int position){

    }
}
