package com.diplom.todoapp.firebase;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

public interface OnDataReceivedListener {
    public void onDataReceived(Object data);
    public void onError(IllegalArgumentException databaseError);
}