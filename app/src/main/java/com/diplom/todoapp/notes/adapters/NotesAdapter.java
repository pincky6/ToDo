package com.diplom.todoapp.notes.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemEmptyBinding;
import com.diplom.todoapp.databinding.ItemNoteBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.EmptyHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.HolderType;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.notes.holders.NoteHolder;
import com.diplom.todoapp.notes.listeners.NoteListener;
import com.diplom.todoapp.notes.listeners.OnSetListener;
import com.diplom.todoapp.notes.models.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Note> notes;
    NoteListener listener;
    OnRemoveListener onRemoveListener;
    OnSetListener onSetListener;
    public NotesAdapter(){
        notes = new ArrayList<>();
    }
    public NotesAdapter(ArrayList<Note> notes, NoteListener listener,
                        OnRemoveListener onRemoveListener, OnSetListener onSetListener){
        this.notes = notes;
        this.listener = listener;
        this.onRemoveListener = onRemoveListener;
        this.onSetListener = onSetListener;
    }
    @Override
    public int getItemViewType(int position) {
        if(notes.size() == 0){
            return HolderType.EMPTY_TASK.ordinal();
        } else {
            return HolderType.NOTE.ordinal();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == HolderType.EMPTY_TASK.ordinal()){
            ItemEmptyBinding binding = ItemEmptyBinding.inflate(inflater, parent, false);
            return new EmptyHolder(binding);
        }
        ItemNoteBinding binding = ItemNoteBinding.inflate(inflater, parent, false);
        return new NoteHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof EmptyHolder) return;
        NoteHolder noteHolder = (NoteHolder) holder;
        noteHolder.bind(notes.get(position), listener,  onRemoveListener, onSetListener);
    }

    @Override
    public int getItemCount() {
        if(notes == null) return 0;
        if(notes.size() == 0) return 1;
        return notes.size();
    }
}
