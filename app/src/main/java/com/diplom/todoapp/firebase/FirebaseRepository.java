package com.diplom.todoapp.firebase;


import static androidx.navigation.ViewKt.findNavController;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.FragmentLoginBinding;
import com.diplom.todoapp.databinding.FragmentRegisterBinding;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Task;
import com.diplom.todoapp.login.LoginFragmentDirections;

import com.diplom.todoapp.utils.SuccsessFlagUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
        auth.signInWithEmailAndPassword(gmail, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(!isVerified()) {
                    Toast.makeText(binding.getRoot().getContext(), "Check your gmail", Toast.LENGTH_SHORT).show();
                    sendVerification(binding);
                }
                else {
                    initDatabase();
                    findNavController(binding.getRoot()).navigate(
                            LoginFragmentDirections.showEventTaskFragment()
                    );
                }
            }
            else
            {
                Toast.makeText(binding.getRoot().getContext(),
                        "Something was wrong. Try sign up latter or check gmail and password",
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(),
                e.getMessage(),
                Toast.LENGTH_LONG).show());
    }

    private void sendVerification(FragmentLoginBinding binding){
        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task ->
                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()){
                findNavController(binding.getRoot()).navigate(
                        LoginFragmentDirections.showEventTaskFragment()
                );
            }
            else {
                Toast.makeText(binding.getRoot().getContext(),
                        "Something was wrong. Try sign up latter or check gmail and password",
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(),
                e.getMessage(),
                Toast.LENGTH_LONG).show()));
    }

    public void createUserWithGmailAndPassword(FragmentRegisterBinding binding, String gmail, String password){
        auth.createUserWithEmailAndPassword(gmail, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(binding.getRoot().getContext(), "You register", Toast.LENGTH_SHORT).show();
                findNavController(binding.getRoot()).popBackStack();
            }
            else {
                Toast.makeText(binding.getRoot().getContext(),
                        "Something was wrong. Try sign up latter or check gmail and password",
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(),
                e.getMessage(),
                Toast.LENGTH_LONG).show());
    }

    public String generateKey(){
        return database.push().getKey();
    }
    public void addTask(AbstractTask object){
        database.child( object.id ).setValue(object);
    }
    public void getTaskFromKey(String key, OnDataReceivedListener dataReceivedListener){
        database.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AbstractTask abstractTask;
                String dataType = key.split("-")[0];
                if(dataType.equals("Task")) {
                    abstractTask =
                            snapshot.getValue(Task.class);
                }
                else
                {
                    abstractTask = snapshot.getValue(DateTask.class);
                }
                if (abstractTask != null) {
                    dataReceivedListener.onDataReceived(abstractTask);
                } else {
                    dataReceivedListener.onError(new IllegalArgumentException("Database Error"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readAllTasks(ArrayList<AbstractTask> taskList, RecyclerView recyclerView, InitExpression initLambda){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.getKey() == null) return;
                    String[] strs = snapshot.getKey().split("-");
                    String key = strs[0];
                    if(key.equals("Task")){
                        Task task = snapshot.getValue(Task.class);
                        task.id = snapshot.getKey();
                        if(!task.succsessFlag.equals("DONE")) {
                            task.succsessFlag = SuccsessFlagUtil.getStringFlagFromDate(task.dateStart);
                            addTask(task);
                        }
                        taskList.add(task);
                    }
                    else if(key.equals("DateTask")){
                        DateTask dateTask = snapshot.getValue(DateTask.class);
                        dateTask.id = snapshot.getKey();
                        if(!dateTask.succsessFlag.equals("DONE")) {
                            dateTask.succsessFlag = SuccsessFlagUtil.getStringFlagFromDate(dateTask.dateStart);
                            addTask(dateTask);
                        }
                        taskList.add(dateTask);
                    }
                }
                if(initLambda != null)
                    initLambda.init(taskList);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readAllTasks(ArrayList<AbstractTask> taskList, InitExpression initLambda){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.getKey() == null) return;
                    String[] strs = snapshot.getKey().split("-");
                    String key = strs[0];
                    if(key.equals("Task")){
                        Task task = snapshot.getValue(Task.class);
                        task.id = snapshot.getKey();
                        taskList.add(task);
                    }
                    else if(key.equals("DateTask")){
                        DateTask dateTask = snapshot.getValue(DateTask.class);
                        dateTask.id = snapshot.getKey();
                        taskList.add(dateTask);
                    }
                }
                if(initLambda != null)
                    initLambda.init(taskList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void removeTask(String id){
        database.child(id).removeValue();
    }
}
