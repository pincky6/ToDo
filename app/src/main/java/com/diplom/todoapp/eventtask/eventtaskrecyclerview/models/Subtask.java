package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

import java.io.Serializable;

public class Subtask implements Serializable {
    public boolean complete;
    public String subtaskString;
    public Subtask(){
        this.complete = false;
        this.subtaskString = "";
    }
    public Subtask(boolean complete, String subtaskString){
        this.complete = complete;
        this.subtaskString = subtaskString;
    }
}
