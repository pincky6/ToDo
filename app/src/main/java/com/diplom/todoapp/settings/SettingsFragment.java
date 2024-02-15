package com.diplom.todoapp.settings;

import static androidx.navigation.ViewKt.findNavController;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.App;
import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentSettingsBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.CategorySpinnerAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.DeletableCategorySpinnerAdapter;
import com.diplom.todoapp.firebase.FirebaseRepository;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding;
    SettingsRepository settings;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        settings = SettingsRepository.getInstance(getContext());
        initThemeModeSpinner();
        initCategorySpinner();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void initCategorySpinner(){
        FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
        if(firebaseRepository == null) return;
        ArrayList<String> categories = (ArrayList<String>) firebaseRepository.getCategories().clone();
        categories.add(getResources().getString(R.string.without_category_text));
        categories.add(getResources().getString(R.string.add_new_category_text));
        binding.categoriesSpinner.setAdapter(new DeletableCategorySpinnerAdapter(getContext(), binding.categoriesSpinner,0, categories));
    }
    private void initThemeModeSpinner(){
        String[] themes = new String[]{"Light Theme", "Dark Theme"};
        ArrayAdapter<String> themeAdapter =
                new ArrayAdapter<>(getContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        themes);
        binding.themeMode.setAdapter(themeAdapter);
        binding.themeMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("SELECTED THEME", parent.getAdapter().getItem(position).toString());
                if(parent.getAdapter().getItem(position).toString().equals("Light Theme")){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    settings.setThemeMode(0, getContext());
                } else if(parent.getAdapter().getItem(position).toString().equals("Dark Theme")){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    settings.setThemeMode(1, getContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.themeMode.setSelection(settings.themeMode);
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(v -> {
            findNavController(getView()).popBackStack();
        });
    }
}
