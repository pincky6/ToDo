package com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.diplom.todoapp.R;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.SpinnerCategoryItemFactory;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.firebase.FirebaseRepository;

import java.util.ArrayList;

public class DeletableCategorySpinnerAdapter extends CategorySpinnerAdapter{
    private static final int ITEM_WITHOUT_CATEGORY = 2;
    public DeletableCategorySpinnerAdapter(@NonNull Context context, Spinner spinner, int resource, ArrayList<String> categories) {
        super(context, spinner, resource, categories);
        inflater = LayoutInflater.from(context);
        this.categories = categories;
        this.spinner = spinner;
    }

    @Override
    public void remove(String item) {
        if(categories == null) return;
        if(categories.size() == 2) {
            return;
        }
        categories.remove(item);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position){
        if(position == categories.size() - 1) return ITEM_ADD_NEW_CATEGORY;
        if(position == categories.size() - 2) return ITEM_WITHOUT_CATEGORY;
        return ITEM_CATEGORY;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        int type = getItemViewType(position);
        String initCategory = getItem(position);
        convertView = getTypeOfView(parent, convertView, initCategory, position);
        if(type == 0){
            TextView textView = convertView.findViewById(R.id.spinnerItem);
            textView.setText(initCategory);
            convertView.setOnClickListener(getAddItemOnClickListener());
        } else if(type == 1){
            Log.d("CATEGORY", initCategory);
            TextView textView = convertView.findViewById(R.id.spinnerItem);
            textView.setText(initCategory);
            ImageButton imageButton = convertView.findViewById(R.id.deleteCategoryButton);
            if(imageButton == null) return convertView;
            imageButton.setOnClickListener(v -> {
                FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
                if(firebaseRepository == null) return;
                String category = getItem(position);
                if(haveCategoryInList(firebaseRepository, category)) {
                    firebaseRepository.removeCategory(category);
                    remove(category);
                } else {
                    AlertDialog.Builder builder = buildDeleteDialog(firebaseRepository, category);
                    builder.show();
                }
            });
            convertView.setOnClickListener(getItemOnClickListener(position));
        } else {
            TextView textView = convertView.findViewById(R.id.spinnerItem);
            textView.setText(initCategory);
            convertView.setOnClickListener(getItemOnClickListener(position));
        }
        return convertView;
    }

    protected boolean haveCategoryInList(FirebaseRepository firebaseRepository, String deleteCategory){
            ArrayList<AbstractTask> abstractTasks = new ArrayList<>();
            firebaseRepository.readAllTasks(abstractTasks, null);
            abstractTasks.stream().filter(abstractTask -> {
                if(abstractTask instanceof Task) return false;
                DateTask dateTask = (DateTask) abstractTask;
                return dateTask.category.equals(deleteCategory);
            });
            return abstractTasks.size() != 0;
    }

    protected AlertDialog.Builder buildDeleteDialog(FirebaseRepository firebaseRepository, String category){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("You have tasks with this category\n");
        builder.setMessage("All categories in tasks with this category will be replaced \"Without Category\"\n\n" +
                        "Are you sure?");
        builder.setPositiveButton("Ok", (dialog, which) -> {
            ArrayList<AbstractTask> abstractTasks = new ArrayList<>();
            firebaseRepository.readAllTasks(abstractTasks, tasks -> {
            abstractTasks.forEach(abstractTask -> {
                    if(abstractTask == null) return;
                    if(abstractTask instanceof Task) return;
                    DateTask dateTask = (DateTask) abstractTask;
                    if(dateTask.category == null) return;
                    if(dateTask.category.equals(category)){
                        dateTask.category = getContext().getResources().getString(R.string.without_category_text);
                        firebaseRepository.addTask(abstractTask);
                    }
                });
                firebaseRepository.removeCategory(category);
                remove(category);
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        return builder;
    }

    private View getTypeOfView(@NonNull ViewGroup parent, View view, @NonNull String textViewContent, int position){
        if(view == null){
            return SpinnerCategoryItemFactory.produce_deletable(parent, position, categories.size());
        }
        ImageButton imageButton = view.findViewById(R.id.deleteCategoryButton);
        ImageView imageView = view.findViewById(R.id.imageView);

        if((textViewContent.equals(getContext().getResources().getString(R.string.add_new_category_text)) ||
                textViewContent.equals(getContext().getResources().getString(R.string.without_category_text)))
                        && imageButton != null){
            view = SpinnerCategoryItemFactory.produce_deletable(parent, position, categories.size());
        }else if((!textViewContent.equals(getContext().getResources().getString(R.string.add_new_category_text)) ||
                !textViewContent.equals(getContext().getResources().getString(R.string.without_category_text)))
                && imageButton == null) {
            view = SpinnerCategoryItemFactory.produce_deletable(parent, position, categories.size());
        } else if((textViewContent.equals(getContext().getResources().getString(R.string.add_new_category_text)) ||
                textViewContent.equals(getContext().getResources().getString(R.string.without_category_text)))
                && imageButton == null && imageView == null){
            view = SpinnerCategoryItemFactory.produce_deletable(parent, position, categories.size());
        } else if((!textViewContent.equals(getContext().getResources().getString(R.string.add_new_category_text)) ||
                !textViewContent.equals(getContext().getResources().getString(R.string.without_category_text)))
                && imageButton == null && imageView != null){
            view = SpinnerCategoryItemFactory.produce_deletable(parent, position, categories.size());
        }
        return view;
    }
}
