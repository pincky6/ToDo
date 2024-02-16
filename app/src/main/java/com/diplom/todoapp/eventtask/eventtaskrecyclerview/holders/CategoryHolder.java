package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemCategoryBinding;
import com.diplom.todoapp.databinding.ItemCheckableCategoryBinding;
import com.diplom.todoapp.eventtask.listeners.OnCategoryAddListener;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;

public class CategoryHolder extends RecyclerView.ViewHolder {
    ItemCheckableCategoryBinding binding;
    String category;
    public CategoryHolder(@NonNull ItemCheckableCategoryBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(String category, OnCategoryAddListener addListener, OnRemoveListener removeListener){
        this.category = category;
        binding.spinnerItem.setText(category);
        binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                addListener.add(this.category);
            } else {
                removeListener.remove(this.category);
            }
        });
    }
}
