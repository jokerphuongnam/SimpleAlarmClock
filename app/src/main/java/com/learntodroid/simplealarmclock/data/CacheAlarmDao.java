package com.learntodroid.simplealarmclock.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface CacheAlarmDao extends CacheAlarmLocal {

    @Override
    default Single<String> insert(Alarm... alarms) {
        return Single.create((emitter) -> {
            CacheAlarm[] cacheAlarms = Arrays.stream(alarms).map((alarm) -> CacheAlarm.insert(
                    alarm.getAlarmId(),
                    alarm.getHour(),
                    alarm.getMinute(),
                    alarm.getTitle(),
                    alarm.getCreated(),
                    alarm.isStarted(),
                    alarm.isRecurring(),
                    alarm.isMonday(),
                    alarm.isTuesday(),
                    alarm.isWednesday(),
                    alarm.isThursday(),
                    alarm.isFriday(),
                    alarm.isSaturday(),
                    alarm.isSunday()
            )).toArray(CacheAlarm[]::new);
            insert(cacheAlarms);
            emitter.onSuccess("");
        });
    }

    @Override
    default Single<String> update(Alarm... alarms) {
        return Single.create((emitter) -> {
            CacheAlarm[] cacheAlarms = Arrays.stream(alarms).map((alarm) -> CacheAlarm.update(
                    alarm.getAlarmId(),
                    alarm.getHour(),
                    alarm.getMinute(),
                    alarm.getTitle(),
                    alarm.getCreated(),
                    alarm.isStarted(),
                    alarm.isRecurring(),
                    alarm.isMonday(),
                    alarm.isTuesday(),
                    alarm.isWednesday(),
                    alarm.isThursday(),
                    alarm.isFriday(),
                    alarm.isSaturday(),
                    alarm.isSunday()
            )).toArray(CacheAlarm[]::new);
            insert(cacheAlarms);
            emitter.onSuccess("");
        });
    }

    @Override
    default Single<String> delete(Alarm... alarms) {
        return Single.create((emitter) -> {
            CacheAlarm[] cacheAlarms = Arrays.stream(alarms).map((alarm) -> CacheAlarm.delete(
                    alarm.getAlarmId()
            )).toArray(CacheAlarm[]::new);
            insert(cacheAlarms);
            emitter.onSuccess("");
        });
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CacheAlarm... alarms);

    @Override
    @Query("DELETE FROM CACHE_ALARM_TABLE")
    void deleteAll();

    @Override
    @Query("SELECT * FROM CACHE_ALARM_TABLE")
    List<CacheAlarm> getCacheAlarms();
}
