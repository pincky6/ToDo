package com.diplom.todoapp.eventtask.filter;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentEventFilterDialogBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.CategoryAdapter;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

public class TaskFilterFragmentDialog extends DialogFragment {
    private FragmentEventFilterDialogBinding binding;
    private ArrayList<String> filterCategories = new ArrayList<>();
    public static final String FILTER_KEY = "FILTER_KEY";
    public static final  String ARRAY_FILTER_KEY = "ARRAY_FILTER_KEY";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventFilterDialogBinding.inflate(inflater, container, false);
        Bundle args = getArguments();
        Integer mask = (Integer)args.get("mask");
        filterCategories = (ArrayList<String>) args.get("categories");

        if(mask != null) {
            setCheckboxes(mask);
        }
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.filter_background);
        initRecyclerView();
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(filterCategories != null) {
            filterCategories.clear();
        }
        binding.okButton.setOnClickListener(v -> {
            boolean taskCheck = binding.taskCheckBox.isChecked();
            boolean dateTaskCheck = binding.dateTaskCheckBox.isChecked();
            boolean lowPriorityCheck = binding.lowPriorityCheckBox.isChecked();
            boolean middlePriorityCheck = binding.middlePriorityCheckBox.isChecked();
            boolean highPriorityCheck = binding.highPriorityCheckBox.isChecked();
            Integer mask = 0;
            mask |= taskCheck ? 1 : 0;
            mask |= dateTaskCheck ? 2 : 0;
            mask |= lowPriorityCheck ? 4 : 0;
            mask |= middlePriorityCheck ? 8 : 0;
            mask |= highPriorityCheck ? 16 : 0;

            Bundle bundle = new Bundle();
            bundle.putSerializable(FILTER_KEY, mask);
            bundle.putStringArrayList(ARRAY_FILTER_KEY, filterCategories);
            getParentFragmentManager().setFragmentResult(FILTER_KEY, bundle);
            dismiss();
        });
        binding.cancelButton.setOnClickListener(v -> {
            dismiss();
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void setCheckboxes(Integer mask){
        binding.taskCheckBox.setChecked((mask & 1) != 0);
        binding.dateTaskCheckBox.setChecked((mask & 2) != 0);
        binding.lowPriorityCheckBox.setChecked((mask & 4) != 0);
        binding.middlePriorityCheckBox.setChecked((mask & 8) != 0);
        binding.highPriorityCheckBox.setChecked((mask & 16) != 0);
    }
    private void initRecyclerView(){
        FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
        if(firebaseRepository == null) return;
        ArrayList<String> categories = firebaseRepository.getCategories();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(new CategoryAdapter(categories,
                category-> {
                    if(!filterCategories.contains(category)) filterCategories.add(category);
                },
                category -> {
                    if(filterCategories.contains(category)) filterCategories.remove(category);
                }));
    }
}
