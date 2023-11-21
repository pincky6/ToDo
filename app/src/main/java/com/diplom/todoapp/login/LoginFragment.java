package com.diplom.todoapp.login;

import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diplom.todoapp.EditorsUtil;
import com.diplom.todoapp.R;
import com.diplom.todoapp.databinding.FragmentLoginBinding;
import com.diplom.todoapp.dialogs.fragments.DateTaskDetailFragmentDirections;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.google.android.material.appbar.MaterialToolbar;

public class LoginFragment extends Fragment {
    private FirebaseRepository firebase = null;
    FragmentLoginBinding binding = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = FirebaseRepository.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebase.checkUserExist() && firebase.isVerified()){
            firebase.initDatabase();
            findNavController(getView()).navigate(
                    LoginFragmentDirections.actionLoginFragmentToEventTaskManager()
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
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
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EditorsUtil.checkEditors(binding.emailTextEdit, binding.passwordEditText)){
                    EditorsUtil.setErrorBackground(binding.emailTextEdit, binding.passwordEditText);
                    return;
                }
                firebase.signInWithGmailAndPassword(binding, binding.emailTextEdit.getText().toString(),
                                                    binding.passwordEditText.getText().toString());
            }
        });
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(binding.getRoot()).navigate(
                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                );
            }
        });
    }
}
