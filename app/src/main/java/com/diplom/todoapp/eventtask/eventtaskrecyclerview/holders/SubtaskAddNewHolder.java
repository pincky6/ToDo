package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.ItemAddNewItemsBinding;
import com.diplom.todoapp.databinding.ItemCheckableBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.SubtasksAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Subtask;

import java.util.ArrayList;

public class SubtaskAddNewHolder extends AbstractSubtaskHolder{
    ItemAddNewItemsBinding binding;
    public SubtaskAddNewHolder(@NonNull ItemAddNewItemsBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(SubtasksAdapter adapter, ArrayList<Subtask> subtasks){
        binding.getRoot().getRootView().setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Enter new category");
            EditText input = new EditText(v.getContext());
            builder.setView(input);
            builder.setPositiveButton("Ok", (dialog, which) -> {
                String enteredText = input.getText().toString();
                if(subtasks == null || adapter == null) return;
                subtasks.add(new Subtask(false, enteredText));
                adapter.notifyDataSetChanged();
                dialog.cancel();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });
    }
}
