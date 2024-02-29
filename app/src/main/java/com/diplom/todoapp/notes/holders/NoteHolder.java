package com.diplom.todoapp.notes.holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.ItemNoteBinding;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.notes.models.Note;
import com.diplom.todoapp.notes.models.NoteElement;
import com.diplom.todoapp.notes.models.NoteType;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.function.Predicate;

public class NoteHolder extends RecyclerView.ViewHolder {
    ItemNoteBinding binding;
    public NoteHolder(@NonNull ItemNoteBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(Note note, OnRemoveListener removeListener){
        Optional<NoteElement> optionalNoteElement = note.elements.stream()
                .filter(noteElement -> noteElement.type.equals(NoteType.TEXT_TYPE))
                .findFirst();
        String text;
        if(!optionalNoteElement.isPresent()) {
            text = "";
        } else {
            text = optionalNoteElement.get().element;
        }
        if(note.title.isEmpty() && !text.isEmpty()) {
            int length = text.length();
            int halfLength = length / 2;
            binding.title.setText(text.substring(0, halfLength));
            text = text.substring(halfLength, length);
        } else if (!note.title.isEmpty()){
            binding.title.setText(note.title);
        }
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
        binding.text.setText(text);
        binding.category.setText(note.category);
        binding.dateCreate.setText(format.format(note.createDate));
        binding.secureImage.setImageResource(R.drawable.baseline_flag_24);
    }
}
