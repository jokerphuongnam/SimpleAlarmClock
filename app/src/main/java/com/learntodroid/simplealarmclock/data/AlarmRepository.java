package com.learntodroid.simplealarmclock.data;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface AlarmRepository {
    Single<String> insert(Alarm alarm);
    Single<String> update(Alarm alarm);
    Single<List<Alarm>> delete(Alarm alarm);
    Single<List<Alarm>> getAlarms();
}
