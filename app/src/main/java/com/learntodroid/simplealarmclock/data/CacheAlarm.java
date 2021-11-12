package com.learntodroid.simplealarmclock.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Contract;

@Entity(tableName = "cache_alarm_table")
public class CacheAlarm {
    @PrimaryKey
    private int alarmId;

    private int hour;
    private int minute;
    private boolean started, recurring;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private String title;

    private long created;
    private TYPE_CACHE typeCache;

    private CacheAlarm(int alarmId, TYPE_CACHE typeCache) {
        this.alarmId = alarmId;
        this.typeCache = typeCache;
    }

    public CacheAlarm(int alarmId, int hour, int minute, String title, long created, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, TYPE_CACHE typeCache) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.title = title;
        this.created = created;
        this.started = started;
        this.recurring = recurring;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.typeCache = typeCache;
    }

    public Alarm toAlarm() {
        return new Alarm(alarmId, hour, minute,title, created, started, recurring, monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    private CacheAlarm() {
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public TYPE_CACHE getTypeCache() {
        return typeCache;
    }

    public void setTypeCache(TYPE_CACHE typeCache) {
        this.typeCache = typeCache;
    }

    @NonNull
    @Contract("_, _, _, _, _, _, _, _, _, _, _, _, _, _ -> new")
    public static CacheAlarm insert(int alarmId, int hour, int minute, String title, long created, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        return new CacheAlarm(alarmId, hour, minute,title, created, started, recurring, monday, tuesday, wednesday, thursday, friday, saturday, sunday, TYPE_CACHE.INSERT);
    }

    @NonNull
    @Contract("_, _, _, _, _, _, _, _, _, _, _, _, _, _ -> new")
    public static CacheAlarm update(int alarmId, int hour, int minute, String title, long created, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        return new CacheAlarm(alarmId, hour, minute,title, created, started, recurring, monday, tuesday, wednesday, thursday, friday, saturday, sunday, TYPE_CACHE.UPDATE);
    }

    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static CacheAlarm delete(int alarmId) {
        return  new CacheAlarm(alarmId, TYPE_CACHE.DELETE);
    }

    public enum TYPE_CACHE {
        INSERT,
        UPDATE,
        DELETE
    }
}
