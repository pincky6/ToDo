package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.diplom.todoapp.R;

public class SpinnerCategoryItemFactory {
    public static View produce_deletable(@NonNull ViewGroup parent, int position, int size){
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(position == size - 2){
            view = inflater.inflate(R.layout.item_default, parent, false);
        } else if(position == size - 1){
            view = inflater.inflate(R.layout.item_add_new_items, parent, false);
        } else{
            view = inflater.inflate(R.layout.item_category, parent, false);
        }
        return view;
    }
    public static View produce(@NonNull ViewGroup parent, int position, int size){
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(position == size - 1){
            view = inflater.inflate(R.layout.item_add_new_items, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_default, parent, false);
        }
        return view;
    }
}
