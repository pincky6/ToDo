package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.ItemCheckableDeletableBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.SubtasksAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Subtask;

import java.util.ArrayList;

public class SubtaskDeletableHolder extends AbstractSubtaskHolder{
    ItemCheckableDeletableBinding binding;
    public SubtaskDeletableHolder(ItemCheckableDeletableBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(SubtasksAdapter adapter, ArrayList<Subtask> subtasks, int position) {
        binding.checkbox.setText(subtasks.get(position).subtaskString);
        binding.checkbox.setChecked(subtasks.get(position).complete);
        if (subtasks.get(position).complete) {
            binding.checkbox.setPaintFlags(binding.checkbox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            subtasks.get(position).complete = isChecked;
            if(isChecked) {
                binding.checkbox.setPaintFlags(binding.checkbox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                binding.checkbox.setPaintFlags(binding.checkbox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });
        binding.imageButton.setOnClickListener(v -> {
            subtasks.remove(position);
            adapter.notifyDataSetChanged();
        });
    }
}
