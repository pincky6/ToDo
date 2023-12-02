package com.diplom.todoapp.eventtask.filter;


public enum TASK_MASK{
    TASK(1),
    DATE_TASK(2),
    LOW_PRIORITY(4),
    MIDDLE_PRIORITY(8),
    HIGH_PRIORITY(16);
    private final int i;
    TASK_MASK(int i) {
        this.i = i;
    }
    public int get(){
        return i;
    }
}