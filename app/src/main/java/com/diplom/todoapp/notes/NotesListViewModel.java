package com.diplom.todoapp.notes;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.firebase.FirebaseRepository;
import com.diplom.todoapp.firebase.InitExpression;
import com.diplom.todoapp.firebase.InitNoteExpression;
import com.diplom.todoapp.notes.models.Note;

import java.util.ArrayList;
import java.util.Objects;

public class NotesListViewModel extends ViewModel {
    public ArrayList<Note> noteList;
    private FirebaseRepository firebase;
    public NotesListViewModel(){
        firebase = FirebaseRepository.getInstance();
        noteList = new ArrayList<>();
    }
    public void loadFirebase(RecyclerView recyclerView, InitNoteExpression initLambda){
        firebase.readAllNotes(noteList, recyclerView, initLambda);
    }
    public void addNote(Note note){
        firebase.addNote(note);
        //taskList.add(abstractTask);
    }
    public void updateNote(Note updatedNote){
        firebase.addNote(updatedNote);
        for(int i = 0; i < noteList.size(); i++){
            if(noteList.get(i).id.equals(updatedNote.id)){

            }
        }
    }
    public void removeById(String id){
        firebase.removeTask(id);
        remove(id);
    }
    public Note getFromId(String id){
        for(Note note: noteList){
            if(Objects.equals(note.id, id)) {
                return note;
            }
        }
        return null;
    }
    public void add(Note note){
        noteList.add(note);
    }
    public void remove(String id){
        for(Note note: noteList){
            if(Objects.equals(note.id, id)) {
                noteList.remove(note);
                return;
            }
        }
    }
    public boolean isEmpty(){
        return noteList.isEmpty();
    }
}
