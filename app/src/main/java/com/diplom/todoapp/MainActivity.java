package com.diplom.todoapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.diplom.todoapp.eventtask.CalendarSingletone;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}