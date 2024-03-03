package com.diplom.todoapp.notes.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemNoteImageBinding;
import com.diplom.todoapp.databinding.ItemNoteTextBinding;
import com.diplom.todoapp.notes.holders.NotesImageHolder;
import com.diplom.todoapp.notes.holders.NotesTextHolder;
import com.diplom.todoapp.notes.models.Note;
import com.diplom.todoapp.notes.models.NoteElement;
import com.diplom.todoapp.notes.models.NoteType;
import com.diplom.todoapp.notes.models.NoteTypeEnum;

public class NotesDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Note note;
    public NotesDetailAdapter(){
        note = new Note();
    }

    public NotesDetailAdapter(Note note){
        this.note = note;
    }

    @Override
    public int getItemViewType(int position) {
        String type = note.elements.get(position).type;
        if(type.equals(NoteType.TEXT_TYPE)){
            return NoteTypeEnum.TEXT_TYPE.ordinal();
        } else if(type.equals(NoteType.PICTURE_TYPE)){
            return NoteTypeEnum.PICTURE_TYPE.ordinal();
        } else{
            return NoteTypeEnum.VOICE_TYPE.ordinal();
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == NoteTypeEnum.TEXT_TYPE.ordinal()){
            ItemNoteTextBinding binding = ItemNoteTextBinding.inflate(inflater, parent, false);
            return new NotesTextHolder(binding);
        } else if(viewType == NoteTypeEnum.PICTURE_TYPE.ordinal()){
            ItemNoteImageBinding binding = ItemNoteImageBinding.inflate(inflater, parent, false);
            return new NotesImageHolder(binding);
        } else {
            ItemNoteTextBinding binding = ItemNoteTextBinding.inflate(inflater, parent, false);
            return new NotesTextHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NotesTextHolder){
            ((NotesTextHolder)holder).bind(note, position);
        } else {
            ((NotesImageHolder)holder).bind(note, position);
        }
    }

    @Override
    public int getItemCount() {
        if(note.elements.isEmpty()) note.elements.add(new NoteElement(NoteType.TEXT_TYPE, ""));
        return note.elements.size();
    }

}
