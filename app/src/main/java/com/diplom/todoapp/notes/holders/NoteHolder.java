package com.diplom.todoapp.notes.holders;

import android.app.AlertDialog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.ItemNoteBinding;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.notes.listeners.NoteListener;
import com.diplom.todoapp.notes.listeners.OnSetListener;
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
    public void bind(Note note, NoteListener listener, OnRemoveListener removeListener, OnSetListener onSetListener){
        Optional<NoteElement> optionalNoteElement = note.elements.stream()
                .filter(noteElement -> noteElement.type.equals(NoteType.TEXT_TYPE))
                .findFirst();
        String text;
        if(!optionalNoteElement.isPresent()) {
            text = "";
        } else {
            text = optionalNoteElement.get().element;
        }

        if (!note.title.isEmpty()){
            binding.title.setText(note.title);
        }else if(note.title.isEmpty() && !text.isEmpty()) {
            int length = text.length();
            int halfLength = length / 2;
            binding.title.setText(text.substring(0, halfLength));
            text = text.substring(halfLength, length);
        }
        binding.getRoot().setOnClickListener(view -> {listener.listen(note);});
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
        binding.text.setText(text);
        binding.category.setText(note.category);
        binding.dateCreate.setText(format.format(note.createDate));
        binding.secureImage.setImageResource(R.drawable.baseline_flag_24);
        if(!note.secure){
            binding.secureImage.setVisibility(View.GONE);
        } else {
            binding.secureImage.setVisibility(View.VISIBLE);
        }
        binding.getRoot().setOnLongClickListener(v -> {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
            builder.setMessage("What do you want?")
                    .setNeutralButton("Cancel", ((dialog, which) ->{} ))
                    .setPositiveButton(getSetString(note.secure), (dialog, id) -> onSetListener.set(note))
                    .setNegativeButton("Delete Note", (dialog, id) -> removeListener.remove(note.id));
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
    }

    private String getSetString(boolean secure){
        return (!secure) ? "Secure Note" : "Unsecure Note";
    }
}
