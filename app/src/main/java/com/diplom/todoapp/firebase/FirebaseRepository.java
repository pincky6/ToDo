package com.diplom.todoapp.firebase;


import static androidx.navigation.ViewKt.findNavController;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.databinding.FragmentLoginBinding;
import com.diplom.todoapp.databinding.FragmentRegisterBinding;

import com.diplom.todoapp.databinding.FragmentResetPasswordBinding;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Holiday;

import com.diplom.todoapp.login.*;
import com.diplom.todoapp.notes.models.Note;
import com.diplom.todoapp.utils.EditorsUtil;
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
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> repeats = new ArrayList<>();

    public ArrayList<String> getRepeats() {
        return repeats;
    }

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
                            LoginFragmentDirections.showBottomFragment()
//                            LoginFragmentDirections.showEventTaskFragment()
                    );
                }
            }
            else
            {
                EditorsUtil.setErrorState(binding.passwordEditText);
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
                        LoginFragmentDirections.showBottomFragment()
                );
            }
            else {
                EditorsUtil.setErrorState(binding.passwordEditText);
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
                String[] defaultCategories = {"Study", "Work", "Holiday"};
                String[] defaultRepeats = {"Don\'t Repeat", "Every Day", "Every Week", "Every Year"};
                for(String category: defaultCategories){
                    addCategory(category);
                    categories.add(category);
                }
                for(String repeat: defaultRepeats){
                    database.child("repeats").child(repeat).setValue(repeat);
                    repeats.add(repeat);
                }
                findNavController(binding.getRoot()).popBackStack();
            }
            else {
                EditorsUtil.setErrorState(binding.passwordEditText);
                Toast.makeText(binding.getRoot().getContext(),
                        "Something was wrong. Try sign up latter or check gmail and password",
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(),
                e.getMessage(),
                Toast.LENGTH_LONG).show());
    }
    public void resetPasswordFromGmail(FragmentResetPasswordBinding binding, String gmail){
        auth.sendPasswordResetEmail(gmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(binding.getRoot().getContext(), "Check your gmail", Toast.LENGTH_SHORT).show();
                        findNavController(binding.getRoot()).popBackStack();
                    } else {
                        Toast.makeText(binding.getRoot().getContext(), "Something was wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public String generateKey(){
        return database.push().getKey();
    }
    public void getTaskFromKey(String key, OnDataReceivedListener dataReceivedListener){
        database.child("tasks").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AbstractTask abstractTask;
                String dataType = key.split("-")[0];
                if(dataType.equals("Task")) {
                    abstractTask =
                            snapshot.getValue(Holiday.class);
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
//                String[] defaultCategories = {"Study", "Work", "Holiday"};
//                String[] defaultRepeats = {"Every year", "Every week", "Every Day", "Dont Repeat"};
//                for(String category: defaultCategories){
//                    addCategory(category);
//                    categories.add(category);
//                }
//                for(String repeat: defaultRepeats){
//                    database.child("repeats").child(repeat).setValue(repeat);
//                    repeats.add(repeat);
//                }
                for(DataSnapshot tasksSnapshot: dataSnapshot.getChildren()) {
                    if (tasksSnapshot.getKey() == null) continue;
                    if(tasksSnapshot.getKey().equals("categories")) readAllCategories(tasksSnapshot);
                    if(tasksSnapshot.getKey().equals("repeats")) readAllRepeats(tasksSnapshot);
                    if (!tasksSnapshot.getKey().equals("tasks")) continue;
                    for (DataSnapshot snapshot : tasksSnapshot.getChildren()) {
                        if (snapshot.getKey() == null) return;
                        String[] strs = snapshot.getKey().split("-");
                        String key = strs[0];
                        if (key.equals("Task")) {
                            Holiday holiday = snapshot.getValue(Holiday.class);
                            holiday.id = snapshot.getKey();
                            if (!holiday.succsessFlag.equals("DONE")) {
                                holiday.succsessFlag = SuccsessFlagUtil.getStringFlagFromDate(holiday.dateStart);
                                addTask(holiday);
                            }
                            taskList.add(holiday);
                        } else if (key.equals("DateTask")) {
                            DateTask dateTask = snapshot.getValue(DateTask.class);
                            dateTask.id = snapshot.getKey();
                            if (!dateTask.succsessFlag.equals("DONE")) {
                                dateTask.succsessFlag = SuccsessFlagUtil.getStringFlagFromDate(dateTask.dateStart);
                                addTask(dateTask);
                            }
                            taskList.add(dateTask);
                        }
                    }
                }
                if(initLambda != null)
                    initLambda.init(taskList);
                if(recyclerView == null || recyclerView.getAdapter() == null) return;
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
                for(DataSnapshot tasksSnapshot: dataSnapshot.getChildren()){
                    if(tasksSnapshot.getKey() == null) continue;
                    if(tasksSnapshot.getKey().equals("categories")) readAllCategories(tasksSnapshot);
                    if(tasksSnapshot.getKey().equals("repeats")) readAllRepeats(tasksSnapshot);
                    if(!tasksSnapshot.getKey().equals("tasks")) continue;
                    for(DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                        if(snapshot.getKey() == null) return;
                        String[] strs = snapshot.getKey().split("-");
                        String key = strs[0];
                        if (key.equals("Task")) {
                            Holiday holiday = snapshot.getValue(Holiday.class);
                            holiday.id = snapshot.getKey();
                            taskList.add(holiday);
                        } else if (key.equals("DateTask")) {
                            DateTask dateTask = snapshot.getValue(DateTask.class);
                            dateTask.id = snapshot.getKey();
                            taskList.add(dateTask);
                        }
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
    public void readAllCategories(DataSnapshot snapshot){
        for(DataSnapshot category: snapshot.getChildren()){
            if(category.getKey() == null) return;
            if(categories.contains(category.getKey())) continue;
            categories.add((String) category.getValue());
        }
    }
    public void addTask(AbstractTask object){
        if(object.id.isEmpty()) database.child("tasks").child("id-null").setValue(object);
        database.child("tasks").child( object.id ).setValue(object);
    }
    public void removeTask(String id){
        database.child("tasks").child(id).removeValue();
    }
    public ArrayList<String> getCategories(){
        return categories;
    }
    public void addCategory(String category) {
        if(categories.contains(category)) return;
        categories.add(category);
        database.child("categories").child(category).setValue(category);
    }
    public void removeCategory(String category) {
        if(!categories.contains(category)) return;
        categories.remove(category);
        database.child("categories").child(category).removeValue();
    }
    public void readAllRepeats(DataSnapshot snapshot){
        for(DataSnapshot repeat: snapshot.getChildren()){
            if(repeat.getKey() == null) return;
            if(repeats.contains(repeat.getKey())) continue;
            repeats.add((String) repeat.getValue());
        }
    }
    public void addNote(Note object){
        if(object.id.isEmpty()) database.child("notes").child("id-null").setValue(object);
        database.child("notes").child( object.id ).setValue(object);
    }
    public void removeNote(String id){
        database.child("notes").child(id).removeValue();
    }
    public void readAllNotes(ArrayList<Note> noteList, InitExpression initLambda){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot tasksSnapshot: dataSnapshot.getChildren()){
                    if(tasksSnapshot.getKey() == null) continue;
                    if(tasksSnapshot.getKey().equals("categories")) readAllCategories(tasksSnapshot);
                    if(tasksSnapshot.getKey().equals("repeats")) readAllRepeats(tasksSnapshot);
                    if(!tasksSnapshot.getKey().equals("notes")) continue;
                    for(DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                        if(snapshot.getKey() == null) return;
                        String[] strs = snapshot.getKey().split("-");
                        String key = strs[0];
                        Note note = snapshot.getValue(Note.class);
                        note.id = snapshot.getKey();
                        noteList.add(note);
                    }
                }
//                if(initLambda != null)
//                    initLambda.init(takList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readAllNotes(ArrayList<Note> noteList, RecyclerView recyclerView, InitNoteExpression initLambda){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot tasksSnapshot: dataSnapshot.getChildren()) {
                    if (tasksSnapshot.getKey() == null) continue;
                    if(tasksSnapshot.getKey().equals("categories")) readAllCategories(tasksSnapshot);
                    if(tasksSnapshot.getKey().equals("repeats")) readAllRepeats(tasksSnapshot);
                    if (!tasksSnapshot.getKey().equals("notes")) continue;
                    for (DataSnapshot snapshot : tasksSnapshot.getChildren()) {
                        if(snapshot.getKey() == null) return;
                        String[] strs = snapshot.getKey().split("-");
                        String key = strs[0];
                        Note note = snapshot.getValue(Note.class);
                        note.id = snapshot.getKey();
                        noteList.add(note);
                    }
                }
                if(initLambda != null)
                    initLambda.init(noteList);
                if(recyclerView == null || recyclerView.getAdapter() == null) return;
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getNoteFromKey(String key, OnDataReceivedListener dataReceivedListener){
        database.child("notes").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Note note;
                note = snapshot.getValue(Note.class);

                if (note != null) {
                    dataReceivedListener.onDataReceived(note);
                } else {
                    dataReceivedListener.onError(new IllegalArgumentException("Database Error"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
