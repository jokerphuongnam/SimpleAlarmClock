package com.learntodroid.simplealarmclock.fcm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.learntodroid.simplealarmclock.broadcastreceiver.AlarmBroadcastReceiver;
import com.learntodroid.simplealarmclock.data.Alarm;

import java.util.Map;
import java.util.Random;

import static android.content.ContentValues.TAG;

/*
* Cac ham ...online chi nhan tin hieu, data da dc web luu vao firestore
* */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class RemoteService extends FirebaseMessagingService {
    public RemoteService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> datas = remoteMessage.getData();
        Log.i(TAG, "da nhan dc tin nhan");
        String type = datas.get("type"); // message co type
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Da nhan dc",Toast.LENGTH_SHORT).show();
            }
        });
        int id = Integer.parseInt(datas.get("alarmId")); // id
        String title;

        int hour;
        int minute;
        switch (type) {
            case "update":
                title = datas.get("title");
                 hour = Integer.parseInt(datas.get("hour"));
                 minute = Integer.parseInt(datas.get("minute"));
                 id = Integer.parseInt(datas.get("alarmId"));
                cancelAlarmOnline(id);
                scheduleAlarmOnline(id, hour,minute,title);
                break;
            case "cancel":
                id = Integer.parseInt(datas.get("alarmId"));
                cancelAlarmOnline(id);
                break;
            case "insert":
                title = datas.get("title");
                hour = Integer.parseInt(datas.get("hour"));
                minute = Integer.parseInt(datas.get("minute"));
                scheduleAlarmOnline(id, hour,minute,title);
                break;
            default:
        }
    }


    void cancelAlarmOnline(int alarmId) {
        Context context = getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
       // this.started = false; web da update vao firestore

        Log.i("cancel", "cancel alarm");
    }
    void scheduleAlarmOnline(int alarmId,int hour, int minute, String title) {
       // int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                hour,
                minute,
                title,
                System.currentTimeMillis(),
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false
        );

        // web da insert vao firestore


        // schedule bao thuc
        alarm.schedule(getApplicationContext());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        System.out.println("---------------------------get token");
        System.out.println(s);
    }
}
