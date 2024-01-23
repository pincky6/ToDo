package com.diplom.todoapp;

import static com.diplom.todoapp.App.CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.diplom.todoapp.utils.NotificationsUtil;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {
    PermissionManager permissionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionManager = new PermissionManager();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.SET_ALARM) != PackageManager.PERMISSION_GRANTED){
//            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
//            startActivity(intent);
            permissionManager.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.SET_ALARM},
                    null);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionManager.onActivityResult(requestCode, resultCode);

    }
}


//    Intent intent = new Intent(this, MainActivity.class);
//    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addNextIntentWithParentStack(intent);
//                PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
//        broadcastIntent.putExtra("message", editTextMessage.getText().toString());
//        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
//        .setSmallIcon(R.drawable.ic_launcher_foreground)
//        .setContentTitle(editTextTitle.getText().toString())
//        .setContentText(editTextMessage.getText().toString())
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//        .setColor(Color.BLUE)
//        .setAutoCancel(true)
//        .setOnlyAlertOnce(true)
//        .setContentIntent(contentIntent)
//        //.addAction(R.drawable.ic_launcher_foreground, editTextTitle.getText().toString(), actionIntent)
//        .build();
//
//        notificationManagerCompat.notify(1, notification);