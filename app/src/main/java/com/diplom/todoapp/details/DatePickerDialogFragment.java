package com.diplom.todoapp.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerDialogFragment extends DialogFragment {
    public static final String DATE_PICKER_KEY = "DATE_PICKER_KEY";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    Date resultDay = new GregorianCalendar(year, month, dayOfMonth).getTime();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DATE_PICKER_KEY, resultDay);
                    getParentFragmentManager().setFragmentResult(DATE_PICKER_KEY, bundle);
                },
                initialYear,
                initialMonth,
                initialDay
        );
    }
}
