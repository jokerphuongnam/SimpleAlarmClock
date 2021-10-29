package com.learntodroid.simplealarmclock.data;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface AlarmRepository {
    void initListen();
    void onClearListener();
    Single<String> insert(Alarm alarm);
    Single<String> update(Alarm alarm);
    Single<String> delete(Alarm alarm);
    Observable<List<Alarm>> getAlarms();
    void updateToken();
    void refresh();
}
