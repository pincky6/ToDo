package com.diplom.todoapp.notes.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class NoteElement implements Serializable {
    public String type;
    public String element;
    public  NoteElement(){
        type = NoteType.TEXT_TYPE;
        element = "";
    }
    public NoteElement(String type, String element){
        this.type = type;
        this.element = element;
    }
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
    }
}
