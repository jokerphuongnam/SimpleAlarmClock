package com.learntodroid.simplealarmclock.fcm;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public class RemoteServiceManager {
    public static void startRemoteService(@NonNull Context context) {
        if (!isMyServiceRunning(context, RemoteService.class)) {
            context.startService(new Intent(context, RemoteService.class));
        }
    }

    private static boolean isMyServiceRunning(@NonNull Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}