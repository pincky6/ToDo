package com.diplom.todoapp.utils;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.SuccsessFlag;

import java.util.Date;

public class SuccsessFlagUtil {
    public static String getStringFromFlag(SuccsessFlag flag){
        switch (flag){
            case NOT_IN_PROGRESS:
                return "NOT_IN_PROGRESS";
            case IN_PROGRESS:
                return "IN_PROGRESS";
            case NOT_DONE:
                return "NOT_DONE";
            case DONE:
                return "DONE";
        }
        return "NOT_IN_PROGRESS";
    }
    public static SuccsessFlag getFlagFromString(@NonNull String flagString){
        switch (flagString){
            case "NOT_IN_PROGRESS":
                return SuccsessFlag.NOT_IN_PROGRESS;
            case "IN_PROGRESS":
                return SuccsessFlag.IN_PROGRESS;
            case "NOT_DONE":
                return SuccsessFlag.NOT_DONE;
            case "DONE":
                return SuccsessFlag.DONE;
        }
        return SuccsessFlag.NOT_IN_PROGRESS;
    }
    public static SuccsessFlag getFlagFromDate(@NonNull Date date){
        Date todayDate = new Date();
        if(date.before(todayDate)) return SuccsessFlag.NOT_IN_PROGRESS;
        if(date.equals(todayDate)) return SuccsessFlag.IN_PROGRESS;
        if(date.after(todayDate)) return SuccsessFlag.NOT_DONE;
        return SuccsessFlag.NOT_IN_PROGRESS;
    }

    public static String getStringFlagFromDate(@NonNull Date date){
        Date todayDate = new Date();
        if(date.before(todayDate)) return "NOT_DONE";
        if(date.equals(todayDate)) return "IN_PROGRESS";
        if(date.after(todayDate)) return "NOT_IN_PROGRESS";
        return "NOT_IN_PROGRESS";
    }

    public static int getColorFromSuccsessFlagString(String succsessFlagString){
        SuccsessFlag flag = getFlagFromString(succsessFlagString);
        switch (flag){
            case NOT_IN_PROGRESS:
                return Color.GRAY;
            case IN_PROGRESS:
                return Color.YELLOW;
            case DONE:
                return Color.GREEN;
            case NOT_DONE:
                return Color.RED;
        }
        return Color.GRAY;
    }
}
