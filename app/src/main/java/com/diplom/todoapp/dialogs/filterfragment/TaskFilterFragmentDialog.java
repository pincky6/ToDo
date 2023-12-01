package com.diplom.todoapp.dialogs.filterfragment;

import static androidx.navigation.ViewKt.findNavController;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.diplom.todoapp.databinding.FragmentEventFilterDialogBinding;

public class TaskFilterFragmentDialog extends DialogFragment {
    private FragmentEventFilterDialogBinding binding;
    public static final String FILTER_KEY = "FILTER_KEY";
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
        setCheckboxes(mask);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
}
