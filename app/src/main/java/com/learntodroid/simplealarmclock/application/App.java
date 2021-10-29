package com.learntodroid.simplealarmclock.application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.firestore.FirebaseFirestore;
import com.learntodroid.simplealarmclock.fcm.RemoteServiceManager;

public class App extends Application {
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";

    private static boolean firstTime = true;
    public static boolean isFirstTime() {
        return firstTime;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestore.setLoggingEnabled(true);
        createNotificationChannnel();
        RemoteServiceManager.startRemoteService(getApplicationContext());
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

    public static void setFirstTime(boolean firstTime) {
        App.firstTime = firstTime;
    }
}
