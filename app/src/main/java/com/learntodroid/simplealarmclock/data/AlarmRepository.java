package com.learntodroid.simplealarmclock.data;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface AlarmRepository {
    Single<List<Alarm>> insert(Alarm alarm);
    Single<List<Alarm>> update(Alarm alarm);
    Single<List<Alarm>> delete(Alarm alarm);
    Single<List<Alarm>> getAlarms();
}
