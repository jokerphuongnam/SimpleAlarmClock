package com.learntodroid.simplealarmclock.data;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.learntodroid.simplealarmclock.broadcastreceiver.NetworkChangeReceiver;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;

public class DefaultAlarmRepositoryImpl implements AlarmRepository {
    private final AlarmNetwork network;
    private final AlarmLocal local;
    private final CacheAlarmLocal cacheLocal;
    private final Context context;

    private static DefaultAlarmRepositoryImpl instance = null;

    private DefaultAlarmRepositoryImpl(Application application) {
        network = NetworkDI.getAlarmNetwork();
        local = AlarmDatabase.getDatabase(application).alarmDao();
        cacheLocal = AlarmDatabase.getDatabase(application).cacheAlarmDao();
        context = application.getBaseContext();
    }

    public static DefaultAlarmRepositoryImpl getInstance(Application application) {
        if (instance == null) {
            instance = new DefaultAlarmRepositoryImpl(application);
        }
        return instance;
    }

    @Override
    public void initListen() {
        network.initListen();
    }

    @Override
    public void onClearListener() {
        network.onClearListener();
        cacheLocal.deleteAll();
    }

    @Override
    public Single<String> insert(Alarm alarm) {
        if (NetworkChangeReceiver.isOnline(context)) {
            return network.insert(alarm);
        }
        return cacheLocal.insert(alarm).map(s -> {
            local.insert(alarm);
            return s;
        });
    }

    @Override
    public Single<String> update(Alarm alarm) {
        if (NetworkChangeReceiver.isOnline(context)) {
            return network.update(alarm);
        }
        return cacheLocal.update(alarm).map(s -> {
            local.update(alarm);
            return s;
        });
    }

    @Override
    public Single<String> delete(Alarm alarm) {
        if (NetworkChangeReceiver.isOnline(context)) {
            return network.delete(alarm);
        }
        return cacheLocal.delete(alarm).map(s -> {
            local.delete(alarm);
            return s;
        });
    }

    @Override
    public Observable<List<Alarm>> getAlarms() {
        if (NetworkChangeReceiver.isOnline(context)) {
            return network.getAlarms().flatMap((Function<List<Alarm>, ObservableSource<List<Alarm>>>) alarms -> {
                local.deleteAll();
                local.insert(alarms.toArray(new Alarm[0]));
                return local.getAlarms();
            });
        }
        return local.getAlarms();
    }

    @Override
    public void updateToken() {
        network.updateToken();
    }

    @Override
    public void refresh() throws NetworkChangeReceiver.NoConnectInternet {
        if (NetworkChangeReceiver.isOnline(context)) {
            network.refresh();
        }
        throw new NetworkChangeReceiver.NoConnectInternet();
    }
}
