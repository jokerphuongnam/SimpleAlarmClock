package com.learntodroid.simplealarmclock.data;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface AlarmLocal {
    void insert(Alarm... alarm);

    void update(Alarm alarm);

    void delete(Alarm alarm);

    void deleteAll();

    Single<List<Alarm>> getAlarms();
}
