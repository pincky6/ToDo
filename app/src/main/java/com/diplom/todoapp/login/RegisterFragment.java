package com.diplom.todoapp.login;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.utils.EditorsUtil;
import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentRegisterBinding;
import com.diplom.todoapp.firebase.FirebaseRepository;

public class RegisterFragment extends Fragment {
    FirebaseRepository firebaseRepository;
    FragmentRegisterBinding binding = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseRepository = FirebaseRepository.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        EditorsUtil.initTextWatchers(binding.emailTextEdit, binding.passwordEditText);
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EditorsUtil.checkEditors(binding.emailTextEdit, binding.passwordEditText)){
                    EditorsUtil.setErrorBackground(binding.emailTextEdit, binding.passwordEditText);
                    return;
                }
                firebaseRepository.createUserWithGmailAndPassword(binding,
                        binding.emailTextEdit.getText().toString().trim(),
                        binding.passwordEditText.getText().toString().trim());
            }
        });
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

    public void initToolbar(){
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(getView()).popBackStack();
            }
        });
    }
}
