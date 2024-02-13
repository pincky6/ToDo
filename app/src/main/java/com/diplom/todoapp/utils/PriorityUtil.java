package com.diplom.todoapp.utils;

import android.graphics.Color;

import com.diplom.todoapp.R;
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
                return Color.rgb(103, 174, 126);
            case 8:
                return Color.rgb(236, 211, 127);
            case 16:
                return Color.rgb(221,66,46);
        }
        return Color.rgb(103, 174, 126);
    }
    public static int getPriorityBorderResource(Priority priority){
        switch (priority.getPriority()){
            case 4:
                return R.drawable.border_low_priority;
            case 8:
                return R.drawable.border_middle_priority;
            case 16:
                return R.drawable.border_high_priority;
        }
        return 0;
    }
    public static int getPriorityColor(String priority){
        switch (priority){
            case "Low":
                return Color.rgb(103, 174, 126);
            case "Middle":
                return Color.rgb(236, 211, 127);
            case "High":
                return Color.rgb(221,66,46);
        }
        return Color.rgb(103, 174, 126);
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
