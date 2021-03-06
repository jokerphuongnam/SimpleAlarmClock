package com.learntodroid.simplealarmclock.service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import com.learntodroid.simplealarmclock.data.Alarm;
import com.learntodroid.simplealarmclock.data.AlarmRepository;
import com.learntodroid.simplealarmclock.data.DefaultAlarmRepositoryImpl;
import com.learntodroid.simplealarmclock.data.RetrofitAlarmImpl;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RescheduleAlarmsService extends LifecycleService {

    private AlarmRepository alarmRepository;

    @Override
    public void onCreate() {
        super.onCreate();
//        alarmRepository = new FirebaseAlarmRepositoryImpl(getApplication());
        alarmRepository = new DefaultAlarmRepositoryImpl(getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        alarmRepository.getAlarms().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(alarms -> {
            for (Alarm a : alarms) {
                if (a.isStarted()) {
                    a.schedule(getApplicationContext());
                }
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
