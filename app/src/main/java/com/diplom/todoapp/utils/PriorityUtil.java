package com.diplom.todoapp.utils;

import android.graphics.Color;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Priority;

public class PriorityUtil {
    public static Priority getPriorityEnum(String priority){
        switch (priority){
            case "Low":
                return Priority.LOW;
            case "Middle":
                return Priority.MIDDLE;
            case "High":
                return Priority.HIGH;
        }
        return Priority.LOW;
    }
    public static int getPriorityColor(Priority priority){
        switch (priority.getPriority()){
            case 4:
                return Color.GREEN;
            case 8:
                return Color.YELLOW;
            case 16:
                return Color.RED;
        }
        return 0;
    }
    public static int getPriorityColor(String priority){
        switch (priority){
            case "Low":
                return Color.GREEN;
            case "Middle":
                return Color.YELLOW;
            case "High":
                return Color.RED;
        }
        return Color.GREEN;
    }
    public static int getPriorityIndex(Priority priority){
        switch (priority){
            case LOW:
                return 0;
            case MIDDLE:
                return 1;
            case HIGH:
                return 2;
        }
        return 1;
    }
}
