

package com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.diplom.todoapp.R;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders.CategoryItemFactory;
import com.diplom.todoapp.firebase.FirebaseRepository;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class CategorySpinnerAdapter extends ArrayAdapter<String> {
    protected LayoutInflater inflater;
    protected Spinner spinner;
    protected ArrayList<String> categories;
    protected static final int ITEM_ADD_NEW_CATEGORY = 0;
    protected static final int ITEM_CATEGORY = 1;
    public CategorySpinnerAdapter(@NonNull Context context, Spinner spinner, int resource, ArrayList<String> categories) {
        super(context, resource);
        inflater = LayoutInflater.from(context);
        this.categories = categories;
        this.spinner = spinner;
    }
    @Override
    public int getItemViewType(int position){
        if(position == categories.size() - 1) return ITEM_ADD_NEW_CATEGORY;
        return ITEM_CATEGORY;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void add(String item) {
        categories.add(categories.size() - 2, item);
        FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
        if (firebaseRepository == null) return;
        firebaseRepository.addCategory(item);
        notifyDataSetChanged();
    }

    @Override
    public int getPosition(String item) {
        if(categories == null) return 0;
        return categories.indexOf(item);
    }
    @Override
    public String getItem(int position) {
        if(categories == null || categories.size() == 0) return "";
        return categories.get(position);
    }
    @Override
    public void clear(){
        categories = null;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(categories == null) return 0;
        return categories.size();
    }
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        if(position == 0 && getCount() == 0) return new TextView(parent.getContext());
        TextView view = new TextView(parent.getContext());
        view.setText(categories.get(position));
        return view;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        if(categories == null) return convertView;
        int type = getItemViewType(position);
        String initCategory = getItem(position);
        convertView = getTypeOfView(parent, convertView, initCategory, position);
        if(type == 0) {
            TextView textView = convertView.findViewById(R.id.spinnerItem);
            textView.setText(initCategory);
            convertView.setOnClickListener(getAddItemOnClickListener());
        } else {
            TextView textView = convertView.findViewById(R.id.spinnerItem);
            textView.setText(initCategory);
            convertView.setOnClickListener(getItemOnClickListener(position));
        }
        return convertView;
    }

    protected View.OnClickListener getItemOnClickListener(int position){
        return v -> {
            spinner.setSelection(position, true);
            try {
                Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
                method.setAccessible(true);
                method.invoke(spinner);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
    protected View.OnClickListener getAddItemOnClickListener(){
        return v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Enter new category");
            EditText input = new EditText(v.getContext());
            builder.setView(input);
            builder.setPositiveButton("Ok", (dialog, which) -> {
                String enteredText = input.getText().toString();
                add(enteredText);
                dialog.cancel();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        };
    }

    private View getTypeOfView(@NonNull ViewGroup parent, View view, @NonNull String textViewContent, int position){
        if(view == null){
            return CategoryItemFactory.produce(parent, position, categories.size());
        }
        ImageView imageView = view.findViewById(R.id.imageView);
        if((textViewContent.equals(getContext().getResources().getString(R.string.add_new_category_text)) ||
                textViewContent.equals(getContext().getResources().getString(R.string.without_category_text)))
                && imageView == null){
            view = CategoryItemFactory.produce(parent, position, categories.size());
        }else if((!textViewContent.equals(getContext().getResources().getString(R.string.add_new_category_text)) ||
                !textViewContent.equals(getContext().getResources().getString(R.string.without_category_text))) && imageView != null) {
            view = CategoryItemFactory.produce(parent, position, categories.size());
        }
        return view;
    }
}