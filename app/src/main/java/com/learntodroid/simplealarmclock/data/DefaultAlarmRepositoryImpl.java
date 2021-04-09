package com.learntodroid.simplealarmclock.data;

import android.app.Application;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
    public Single<String> insert(Alarm alarm) {
        return network.insert(alarm);
    }

    @Override
    public Single<String> update(Alarm alarm) {
        return network.update(alarm);
    }

    @Override
    public Single<List<Alarm>> delete(Alarm alarm) {
        return Single.create(emitter -> {
            AlarmDatabase.databaseWriteExecutor.execute(() -> {
                local.delete(alarm);
                Disposable subscribe = local.getAlarms().subscribe((alarms, throwable) -> emitter.onSuccess(alarms));
                SystemClock.sleep(500);
                subscribe.dispose();
            });
            network.delete(alarm).subscribe((s, throwable) -> {});

        });
    }

    @Override
    public Single<List<Alarm>> getAlarms() {
        return Single.create(emitter -> {
            final CompositeDisposable subscribe = new CompositeDisposable();
            subscribe.add(local.getAlarms().subscribe(localAlarms -> {
                AlarmDatabase.databaseWriteExecutor.execute(() -> {
                    local.deleteAll();
                    subscribe.add(network.getAlarms().subscribe(networkAlarm -> {
                        if (networkAlarm == null || networkAlarm.isEmpty()) {
                            emitter.onSuccess(new ArrayList<>());
                        } else {
                            emitter.onSuccess(networkAlarm);
                            networkAlarm.forEach(local::insert);
                        }
                    }));
                });
            }));
            SystemClock.sleep(500);
            subscribe.dispose();
        });
    }

    @Override
    public void updateToken() {
        network.updateToken();
    }
}
