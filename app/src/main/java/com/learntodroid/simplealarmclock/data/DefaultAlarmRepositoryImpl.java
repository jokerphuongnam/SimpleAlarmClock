package com.learntodroid.simplealarmclock.data;

import android.app.Application;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;

public class DefaultAlarmRepositoryImpl implements AlarmRepository {
    private final AlarmNetwork network;
    private final AlarmLocal local;

    private static DefaultAlarmRepositoryImpl instance = null;
    public static DefaultAlarmRepositoryImpl getInstance(Application application){
        if(instance == null) {
            instance = new DefaultAlarmRepositoryImpl(application);
        }
        return  instance;
    }

    private DefaultAlarmRepositoryImpl(Application application) {
        network = FirebaseAlarmImpl.getInstance();
        local = AlarmDatabase.getDatabase(application).alarmDao();
    }

    @Override
    public void initListen() {
        network.initListen();
    }

    @Override
    public void onClearListener() {
        network.onClearListener();
    }

    @Override
    public Single<String> insert(Alarm alarm) {
        return network.insert(alarm);
    }

    @Override
    public Single<String> update(Alarm alarm) {
        return network.update(alarm);
    }

    @Override
    public Single<String> delete(Alarm alarm) {
        return network.delete(alarm);
    }

    @Override
    public Observable<List<Alarm>> getAlarms() {
        return network.getAlarms().flatMap((Function<List<Alarm>, ObservableSource<List<Alarm>>>) alarms -> {
            local.deleteAll();
            local.insert(alarms.toArray(new Alarm[0]));
            return local.getAlarms();
        });
    }

    @Override
    public void updateToken() {
        network.updateToken();
    }

    @Override
    public void refresh() {
        network.refresh();
    }
}
