package com.diplom.todoapp.firebase;


import static androidx.navigation.ViewKt.findNavController;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.diplom.todoapp.databinding.FragmentLoginBinding;
import com.diplom.todoapp.databinding.FragmentRegisterBinding;
import com.diplom.todoapp.login.LoginFragmentDirections;
import com.diplom.todoapp.login.RegisterFragmentDirections;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRepository {
    private static FirebaseRepository firebaseRepository = null;

    private FirebaseAuth auth;
    private DatabaseReference database;
    private FirebaseRepository(){
        auth = FirebaseAuth.getInstance();
    }
    public static FirebaseRepository getInstance(){
        if(firebaseRepository == null){
            firebaseRepository = new FirebaseRepository();
        }
        return firebaseRepository;
    }

    public boolean checkUserExist(){
        return auth.getCurrentUser() != null;
    }
    public boolean isVerified(){
        return auth.getCurrentUser().isEmailVerified();
    }
    public void signOut(){
        auth.signOut();
    }
    public void initDatabase(){
        if(checkUserExist()){
            String userId = auth.getCurrentUser().getEmail().replace('.', '-');
            database = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        }
    }
    public void signInWithGmailAndPassword(FragmentLoginBinding binding, String gmail, String password){
        auth.signInWithEmailAndPassword(gmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(!isVerified()) {
                        Toast.makeText(binding.getRoot().getContext(), "Check your gmail", Toast.LENGTH_SHORT).show();
                        sendVerification(binding);
                    }
                    else
                    {
                        initDatabase();
                        findNavController(binding.getRoot()).navigate(
                                LoginFragmentDirections.actionLoginFragmentToEventTaskManager()
                        );
                    }
                }
                else
                {
                    Toast.makeText(binding.getRoot().getContext(),
                            "Something was wrong. Try sign up latter or check gmail and password",
                            Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(binding.getRoot().getContext(),
                        e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendVerification(FragmentLoginBinding binding){
        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            findNavController(binding.getRoot()).navigate(
                                    LoginFragmentDirections.actionLoginFragmentToEventTaskManager()
                            );
                        }
                        else
                        {
                            Toast.makeText(binding.getRoot().getContext(),
                                    "Something was wrong. Try sign up latter or check gmail and password",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(binding.getRoot().getContext(),
                                e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void createUserWithGmailAndPassword(FragmentRegisterBinding binding, String gmail, String password){
        auth.createUserWithEmailAndPassword(gmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(binding.getRoot().getContext(), "You register", Toast.LENGTH_SHORT).show();
                    findNavController(binding.getRoot()).navigate(
                            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    );
                }
                else
                {
                    Toast.makeText(binding.getRoot().getContext(),
                            "Something was wrong. Try sign up latter or check gmail and password",
                            Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(binding.getRoot().getContext(),
                        e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    public void addTask(String key, Object task){
        database.child(key).setValue(task);
    }
}
