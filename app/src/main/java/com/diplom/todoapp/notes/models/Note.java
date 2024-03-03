package com.diplom.todoapp.notes.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Note implements Serializable {
    public String id;
    public String title;
    public ArrayList<NoteElement> elements;
    public Date createDate;
    public String category;
    public boolean secure;
    public Note(){
        title = "";
        elements = new ArrayList<>();
        createDate = new Date();
        secure = false;
        category = "Without Category";
    }
    public Note(String id, String title, ArrayList<NoteElement> elements, Date createDate, String category, boolean secure){
        this.id = id;
        this.title = title;
        this.elements = elements;
        this.createDate = createDate;
        this.secure = secure;
        this.category = category;
    }
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
    }
}
