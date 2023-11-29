package com.diplom.todoapp.dialogs;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;

public interface OnDataReceivedListener {
    public void onDataReceived(AbstractTask data);
    public void onError(IllegalArgumentException databaseError);
}