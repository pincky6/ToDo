package com.diplom.todoapp.eventtask.eventtaskrecyclerview.models;

public enum Priority {
    LOW(4),
    MIDDLE(8),
    HIGH(16);
    private final int i;
    Priority(int i) {
        this.i = i;
    }
    public int getPriority(){
        return i;
    }

}
