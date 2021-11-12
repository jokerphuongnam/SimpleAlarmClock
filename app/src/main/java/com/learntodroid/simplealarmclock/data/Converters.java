package com.learntodroid.simplealarmclock.data;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public CacheAlarm.TYPE_CACHE toTYPE_CACHE(String value) {
        return CacheAlarm.TYPE_CACHE.valueOf(value);
    }

    @TypeConverter
    public String fromTYPE_CACHE(@NonNull CacheAlarm.TYPE_CACHE value) {
        return value.name();
    }
}
