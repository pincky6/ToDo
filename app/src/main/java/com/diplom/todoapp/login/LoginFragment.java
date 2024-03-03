package com.diplom.todoapp.login;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.settings.SettingsFragment;
import com.diplom.todoapp.settings.SettingsRepository;
import com.diplom.todoapp.utils.EditorsUtil;
import com.diplom.todoapp.databinding.FragmentLoginBinding;
import com.diplom.todoapp.firebase.FirebaseRepository;

import java.util.Set;

public class LoginFragment extends Fragment {
    private FirebaseRepository firebase = null;
    FragmentLoginBinding binding = null;
    SettingsRepository repository;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = FirebaseRepository.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        EditorsUtil.initTextWatchers(binding.emailTextEdit, binding.passwordEditText);
        if(firebase.checkUserExist() && firebase.isVerified()){
            firebase.initDatabase();
            findNavController(getView()).navigate(
                    LoginFragmentDirections.showBottomFragment()
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        repository = SettingsRepository.getInstance(getContext());
        EditorsUtil.initTextWatchers(binding.emailTextEdit, binding.passwordEditText);
        initButtons();
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

    private void initButtons(){
        binding.signInButton.setOnClickListener(v -> {
            if(EditorsUtil.checkEditors(binding.emailTextEdit, binding.passwordEditText)){
                EditorsUtil.setErrorBackground(binding.emailTextEdit, binding.passwordEditText);
                return;
            }
            firebase.signInWithGmailAndPassword(binding, binding.emailTextEdit.getText().toString().trim(),
                                                binding.passwordEditText.getText().toString().trim());
        });
        binding.signUpButton.setOnClickListener(v -> findNavController(binding.getRoot()).navigate(
                LoginFragmentDirections.showRegisterFragment()
        ));
        binding.resetButton.setOnClickListener(v -> findNavController(binding.getRoot()).navigate(
                LoginFragmentDirections.showRessetPassword()
        ));
    }
}
