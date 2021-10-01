package com.learntodroid.simplealarmclock.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface AlarmDao extends AlarmLocal {
    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Alarm... alarm);

    @Override
    @Update
    void update(Alarm alarm);

    @Override
    @Delete
    void delete(Alarm alarm);

    @Override
    @Query("DELETE FROM alarm_table")
    void deleteAll();

    @Override
    @Query("SELECT * FROM alarm_table ORDER BY created ASC")
    Observable<List<Alarm>> getAlarms();
}