package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemCheckableBinding;
import com.diplom.todoapp.eventtask.listeners.OnCategoryAddListener;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;

public class CategoryHolder extends RecyclerView.ViewHolder {
    ItemCheckableBinding binding;
    String category;
    public CategoryHolder(@NonNull ItemCheckableBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(String category, OnCategoryAddListener addListener, OnRemoveListener removeListener){
        this.category = category;
        binding.checkbox.setText(category);
        binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                addListener.add(this.category);
            } else {
                removeListener.remove(this.category);
            }
        });
    }
}
