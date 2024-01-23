package com.diplom.todoapp.utils;

import static com.diplom.todoapp.App.CHANNEL_ID;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.diplom.todoapp.App;
import com.diplom.todoapp.MainActivity;
import com.diplom.todoapp.NotificationReceiver;
import com.diplom.todoapp.R;
import com.diplom.todoapp.eventtask.TaskFragment;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Reminders;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.TimeZone;

public class NotificationsUtil {
    public static void createNotification(Context context, AbstractTask abstractTask) {
        if(ReminderUtil.getReminderEnum(abstractTask.reminder).equals(Reminders.DONT_REMIND)){
            return;
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = CalendarUtil.getCalendar(abstractTask.dateStart);
        if(ReminderUtil.getReminderEnum(abstractTask.reminder).equals(Reminders.MINUTES_5_BEFORE)){
            calendar.add(Calendar.MINUTE, -5);
        }
        else
        {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        String idString = abstractTask.id.split("--")[1];
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", abstractTask.title);
        intent.putExtra("describe", abstractTask.describe);
        intent.putExtra("id", idString);
        try {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NotificationReceiver.getId(idString).intValue(), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() ,pendingIntent);
        }
        catch (NoSuchAlgorithmException e){
            return;
        }
    }

    public static void deleteNotification(Context context, AbstractTask abstractTask){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        try {
            int requestCode = NotificationReceiver.getId(abstractTask.id.split("--")[1]).intValue();
            notificationIntent.putExtra("requestCode",requestCode);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
        catch (NoSuchAlgorithmException e){
            return;
        }
    }
}
