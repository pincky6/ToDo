package com.diplom.todoapp.utils;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.diplom.todoapp.R;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.SuccsessFlag;

import java.util.Calendar;
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
    public static String getSuccsessStringFromString(@NonNull String flagString){
        switch (flagString){
            case "NOT_IN_PROGRESS":
                return "Not In Progress";
            case "IN_PROGRESS":
                return "In Progress";
            case "NOT_DONE":
                return "Not Done";
            case "DONE":
                return "Done";
        }
        return "Not In Progress";
    }
    public static SuccsessFlag getFlagFromDate(@NonNull Date date){
        Date todayDate = new Date();
        if(date.before(todayDate)) return SuccsessFlag.NOT_IN_PROGRESS;
        if(date.equals(todayDate)) return SuccsessFlag.IN_PROGRESS;
        if(date.after(todayDate)) return SuccsessFlag.NOT_DONE;
        return SuccsessFlag.NOT_IN_PROGRESS;
    }

    public static String getStringFlagFromDate(@NonNull Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        if(cal1.compareTo(cal2) == 1) return "NOT_DONE";
        if(cal1.compareTo(cal2) == 0) return "IN_PROGRESS";
        if(cal1.compareTo(cal2) == -1) return "NOT_IN_PROGRESS";
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

    public static int getBackgroundResourceFromSuccsessFlagString(String succsessFlagString){
        SuccsessFlag flag = getFlagFromString(succsessFlagString);
        switch (flag){
            case NOT_IN_PROGRESS:
                return R.drawable.status_not_in_progress_border_color;
            case IN_PROGRESS:
                return R.drawable.status_in_progress_border_color;
            case DONE:
                return R.drawable.status_done_border_color;
            case NOT_DONE:
                return R.drawable.status_not_done_border_color;
        }
        return R.drawable.status_not_in_progress_border_color;
    }
}
