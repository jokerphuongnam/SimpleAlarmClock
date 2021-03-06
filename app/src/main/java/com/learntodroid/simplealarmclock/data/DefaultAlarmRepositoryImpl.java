package com.learntodroid.simplealarmclock.data;

import android.app.Application;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DefaultAlarmRepositoryImpl implements AlarmRepository {
    private final AlarmNetwork network;
    private final AlarmLocal local;

    public DefaultAlarmRepositoryImpl(Application application) {
        network = RetrofitAlarmImpl.getInstance();
        local = AlarmDatabase.getDatabase(application).alarmDao();
    }

    @Override
    public Single<List<Alarm>> insert(Alarm alarm) {
        AlarmDatabase.databaseWriteExecutor.execute(() -> {
            local.insert(alarm);
        });
        return network.insert(alarm);
    }

    @Override
    public Single<List<Alarm>> update(Alarm alarm) {
        AlarmDatabase.databaseWriteExecutor.execute(() -> {
            local.update(alarm);
        });
        return network.update(alarm);
    }

    @Override
    public Single<List<Alarm>> delete(Alarm alarm) {
//        SystemClock.sleep(1000);
        return Single.create(emitter -> {
            AlarmDatabase.databaseWriteExecutor.execute(() -> {
                local.delete(alarm);
                Disposable subscribe = local.getAlarms().subscribe((alarms, throwable) -> emitter.onSuccess(alarms));
                SystemClock.sleep(500);
                subscribe.dispose();
            });
            network.delete(alarm);
        });
    }

    @Override
    public Single<List<Alarm>> getAlarms() {
        return Single.create(emitter -> {
            final CompositeDisposable subscribe = new CompositeDisposable();
            subscribe.add(local.getAlarms().subscribe(localAlarms -> {
                AlarmDatabase.databaseWriteExecutor.execute(() -> {
                    local.deleteAll();
                    if (localAlarms == null || localAlarms.isEmpty()) {
                        subscribe.add(network.getAlarms().subscribe(networkAlarm -> {
                            if (networkAlarm == null || networkAlarm.isEmpty()) {
                                emitter.onSuccess(new ArrayList<>());
                                return;
                            }
                            emitter.onSuccess(networkAlarm);
                            networkAlarm.forEach(local::insert);
                        }));
                    }
                });
            }));
            SystemClock.sleep(500);
            subscribe.dispose();
        });
    }
}
