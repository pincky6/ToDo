package com.diplom.todoapp.login;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentResetPasswordBinding;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.utils.EditorsUtil;

public class ResetPasswordFragment extends Fragment {
    FragmentResetPasswordBinding binding = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        EditorsUtil.initTextWatchers(binding.gmailTextText);
        initButton();
        initToolbar();
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

    private void initToolbar(){
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(v -> findNavController(getView()).popBackStack());
    }
    private void initButton(){
        binding.sendButton.setOnClickListener(v -> {
            if(binding.gmailTextText.getText().toString().isEmpty()){
                EditorsUtil.setErrorBackground(binding.gmailTextText);
                return;
            }
            FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
            firebaseRepository.resetPasswordFromGmail(binding, binding.gmailTextText.getText().toString());
        });
    }
}
