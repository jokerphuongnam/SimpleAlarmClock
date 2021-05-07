package com.learntodroid.simplealarmclock.fcm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.learntodroid.simplealarmclock.broadcastreceiver.AlarmBroadcastReceiver;
import com.learntodroid.simplealarmclock.data.Alarm;

import java.util.Map;

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
        String type = datas.get("type"); // message co type
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
        // gui broadcast cap nhat lai
        Intent intent = new Intent();
        intent.setAction("com.khang.simplealarmclock");
        sendBroadcast(intent);
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
//        Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
    }
}
