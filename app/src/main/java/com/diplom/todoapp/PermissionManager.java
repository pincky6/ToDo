package com.diplom.todoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PermissionManager {
    private static final String TAG = "PermissionManager";
    private static final int REQUEST_CODE = 10;
    private WeakReference<Activity> activityWeakReference;
    private Runnable runnable;
    private List<String> permissionsNotGranted;

    private int checkPermission(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission);
    }

    public void requestPermissions(Activity activity, String[] permissions, Runnable runThenPermissionGranted) {
        permissionsNotGranted = new ArrayList<>();
        for (String permission : permissions) {
            if (checkPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }
        if (!permissionsNotGranted.isEmpty()) {
            runnable = runThenPermissionGranted;
            activityWeakReference = new WeakReference<>(activity);
            ActivityCompat.requestPermissions(activity, permissionsNotGranted.toArray(new String[0]), REQUEST_CODE);
        } else {
            if (runThenPermissionGranted != null) {
                runThenPermissionGranted.run();
            }
        }
    }

    private void buildDialog(AlertDialog.Builder dialogBuilder, final Activity activity, final String permission) {
        dialogBuilder.setTitle(permission + " permission is not granted");
        dialogBuilder.setMessage("This permission is not granted.\n" +
                "Please grant permission in settings on your phone and try again");
        dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivityForResult(intent, REQUEST_CODE);
            }
        });
        dialogBuilder.create().show();
    }

    private void handleDeniedPermissions(Activity activity, String[] permissions, int[] grantResult, List<String> deniedPermissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResult[i] == PackageManager.PERMISSION_DENIED &&
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                    buildDialog(new AlertDialog.Builder(activity), activity, permissions[i]);
                } else {
                    deniedPermissions.add(permissions[i]);
                }
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        if (requestCode != REQUEST_CODE)
            return;
        if(activityWeakReference == null) return;
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            List<String> deniedPermissions = new ArrayList<>();
            handleDeniedPermissions(activity, permissions, grantResult, deniedPermissions);
            if (deniedPermissions.isEmpty()) {
                if (runnable != null) {
                    runnable.run();
                }
            }
            activityWeakReference = null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode) {
        if (requestCode != REQUEST_CODE)
            return;
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            List<String> deniedPermissions = new ArrayList<>();
            if (resultCode == Activity.RESULT_CANCELED) {
                for (String permission : permissionsNotGranted) {
                    if (checkPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                        deniedPermissions.add(permission);
                    }
                }
            }
            if (deniedPermissions.isEmpty()) {
                if (runnable != null) {
                    runnable.run();
                }
                permissionsNotGranted.clear();
            }
            activityWeakReference = null;
        }
    }
}
