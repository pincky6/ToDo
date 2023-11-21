package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

public enum Priority {
    LOW(1),
    MIDDLE(2),
    HIGH(4);
    private final int i;
    Priority(int i) {
        this.i = i;
    }
    public int getPriority(){
        return i;
    }

}
