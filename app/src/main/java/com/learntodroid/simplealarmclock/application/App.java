package com.learntodroid.simplealarmclock.application;

import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.firebase.firestore.FirebaseFirestore;
import com.learntodroid.simplealarmclock.fcm.RemoteService;

public class App extends Application {
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestore.setLoggingEnabled(true);
        createNotificationChannnel();
        if (!isMyServiceRunning(RemoteService.class)) {
            startService(new Intent(getApplicationContext(), RemoteService.class));
        }
       // scheduleAlarmOnline(22,56,"title");
    }

    private void createNotificationChannnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

//    void scheduleAlarmOnline(int hour, int minute, String title) {
//        int alarmId = new Random().nextInt(Integer.MAX_VALUE);
//        Log.i("-----------", "inside function");
//        Alarm alarm = new Alarm(
//                alarmId,
//                hour,
//                minute,
//                title,
//                System.currentTimeMillis(),
//                true,
//                false,
//                false,
//                false,
//                false,
//                false,
//                false,
//                false,
//                false
//        );
//        // createAlarmViewModel.insert(alarm);
//        alarm.schedule(getApplicationContext());
//    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private static boolean firstTime = true;
    public static  boolean isFirstTime() {
        return  firstTime;
    }
    public static void setFirstTime(boolean firstTime) {
        App.firstTime = firstTime;
    }
}
