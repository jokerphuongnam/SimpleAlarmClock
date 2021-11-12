package com.learntodroid.simplealarmclock.broadcastreceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.learntodroid.simplealarmclock.data.AlarmDatabase;
import com.learntodroid.simplealarmclock.data.AlarmNetwork;
import com.learntodroid.simplealarmclock.data.CacheAlarm;
import com.learntodroid.simplealarmclock.data.CacheAlarmLocal;
import com.learntodroid.simplealarmclock.data.NetworkDI;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private AlarmNetwork network;
    private CacheAlarmLocal cacheLocal;

    public static boolean isOnline(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (network == null) {
            network = NetworkDI.getAlarmNetwork();
        }
        if (cacheLocal == null) {
            cacheLocal = AlarmDatabase.getDatabase(context.getApplicationContext()).cacheAlarmDao();
        }
        if (isOnline(context)) {
            new Thread(() -> {
                List<CacheAlarm> cacheAlarms = cacheLocal.getCacheAlarms();
                cacheAlarms.stream().map((cacheAlarm) -> {
                    switch (cacheAlarm.getTypeCache()) {
                        case INSERT:
                            network.insert(cacheAlarm.toAlarm())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe();
                            break;
                        case UPDATE:
                            network.update(cacheAlarm.toAlarm())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe();
                            break;
                        case DELETE:
                            network.delete(cacheAlarm.toAlarm())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe();
                            break;
                    }
                    return null;
                }).collect(Collectors.toList());
                cacheLocal.deleteAll();
            }).start();
        }
    }

    public static class NoConnectInternet extends Exception {

    }
}
