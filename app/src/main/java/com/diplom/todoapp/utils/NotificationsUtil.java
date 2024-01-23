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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.TimeZone;

public class NotificationsUtil {
    public static void createNotification(Context context, AbstractTask abstractTask) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 4); // Час
        calendar.set(Calendar.MINUTE, 54);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String idString = abstractTask.id.split("--")[1];
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", abstractTask.title);
        intent.putExtra("describe", abstractTask.describe);
        intent.putExtra("id", idString);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT  | PendingIntent.FLAG_IMMUTABLE);

//        alarmManager.setTimeZone("Russia/Moskow");
        Log.d("TIMEZONE", TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)+" Timezone id :: " +TimeZone.getDefault().getID());
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() ,pendingIntent);
    }
}
