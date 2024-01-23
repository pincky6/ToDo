package com.diplom.todoapp.utils;

import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.SuccsessFlag;

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
    public static SuccsessFlag getFlagFromString(String flagString){
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
}
