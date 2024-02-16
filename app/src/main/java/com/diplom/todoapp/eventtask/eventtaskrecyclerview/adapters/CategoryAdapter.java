package com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.ItemCheckableCategoryBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.CategoryHolder;
import com.diplom.todoapp.eventtask.listeners.OnCategoryAddListener;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
    private ArrayList<String> categories;
    private final OnCategoryAddListener addListener;
    private final OnRemoveListener removeListener;
    public CategoryAdapter(ArrayList<String> categories,
                           OnCategoryAddListener addListener,
                        OnRemoveListener removeListener){
        this.categories = categories;
        this.addListener = addListener;
        this.removeListener = removeListener;
    }
    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int taskType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCheckableCategoryBinding binding = ItemCheckableCategoryBinding.inflate(inflater, parent, false);
        return new CategoryHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.bind(categories.get(position), addListener, removeListener);
    }

    @Override
    public int getItemCount() {
        return categories.size() > 0 ? categories.size() : 1;
    }

    public void resetCategoriesList(ArrayList<String> categories){
        this.categories = categories;
    }
}
