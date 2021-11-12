package com.learntodroid.simplealarmclock.data;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface CacheAlarmLocal {
    Single<String> insert(Alarm... alarms);

    Single<String> update(Alarm... alarms);

    Single<String> delete(Alarm... alarms);

    void deleteAll();

    List<CacheAlarm> getCacheAlarms();
}
