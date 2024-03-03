package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.graphics.Paint;
import android.text.Html;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemCheckableBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Subtask;

public class SubtaskHolder extends AbstractSubtaskHolder {
    ItemCheckableBinding binding;
    Subtask subtask;
    public SubtaskHolder(@NonNull ItemCheckableBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(Subtask subtask){
        this.subtask = subtask;
        binding.checkbox.setChecked(subtask.complete);
        if(subtask.complete) {
            binding.checkbox.setPaintFlags(binding.checkbox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        binding.checkbox.setText(subtask.subtaskString);
        binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            this.subtask.complete = isChecked;
            if(isChecked) {
                binding.checkbox.setPaintFlags(binding.checkbox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                binding.checkbox.setPaintFlags(binding.checkbox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });
    }

}
