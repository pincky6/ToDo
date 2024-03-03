package com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemAddNewItemsBinding;
import com.diplom.todoapp.databinding.ItemCheckableBinding;
import com.diplom.todoapp.databinding.ItemCheckableDeletableBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.AbstractSubtaskHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.SubtaskAddNewHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.SubtaskDeletableHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.SubtaskHolder;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Subtask;

import java.util.ArrayList;

public class SubtasksAdapter extends RecyclerView.Adapter<AbstractSubtaskHolder> {
    ArrayList<Subtask> subtasks;
    boolean produceDeletableItem = false;
    public static final int ITEM_ADD_NEW_SUBTASKS = 0;
    public static final int ITEM_SUBTASK = 1;
    public static final int ITEM_DELETABLE_SUBTASK = 2;
    @Override
    public int getItemViewType(int position){
        if(position == subtasks.size()) return  ITEM_ADD_NEW_SUBTASKS;
        if(produceDeletableItem) return ITEM_DELETABLE_SUBTASK;
        return ITEM_SUBTASK;
    }
    public SubtasksAdapter(ArrayList<Subtask> subtasks, boolean produceDeletableItem){
        this.subtasks = subtasks;
        this.produceDeletableItem = produceDeletableItem;
    }
    @NonNull
    @Override
    public AbstractSubtaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == 1) {
            ItemCheckableBinding binding = ItemCheckableBinding.inflate(inflater, parent, false);
            return new SubtaskHolder(binding);
        } else if(viewType == 2){
            ItemCheckableDeletableBinding binding = ItemCheckableDeletableBinding.inflate(inflater, parent, false);
            return new SubtaskDeletableHolder(binding);
        }
        ItemAddNewItemsBinding binding = ItemAddNewItemsBinding.inflate(inflater, parent, false);
        binding.spinnerItem.setText("Add New Subtask");
        return new SubtaskAddNewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractSubtaskHolder holder, int position) {
        if(holder instanceof SubtaskHolder) {
           ((SubtaskHolder)holder).bind(subtasks.get(position));
        } else if(holder instanceof  SubtaskDeletableHolder){
            ((SubtaskDeletableHolder)holder).bind(this, subtasks, position);
        } else {
            ((SubtaskAddNewHolder)holder).bind(this, subtasks);
        }
    }

    @Override
    public int getItemCount() {
        if(subtasks == null) return 0;
        if(produceDeletableItem) return subtasks.size() + 1;
        return subtasks.size();
    }
    public void setSubtasks(@Nullable ArrayList<Subtask> subtasks){
        this.subtasks = subtasks;
    }
}
