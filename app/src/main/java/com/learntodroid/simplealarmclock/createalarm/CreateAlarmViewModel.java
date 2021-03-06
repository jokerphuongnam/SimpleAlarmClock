package com.learntodroid.simplealarmclock.createalarm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.learntodroid.simplealarmclock.data.Alarm;
import com.learntodroid.simplealarmclock.data.AlarmRepository;
import com.learntodroid.simplealarmclock.data.DefaultAlarmRepositoryImpl;
import com.learntodroid.simplealarmclock.data.RetrofitAlarmImpl;

public class CreateAlarmViewModel extends AndroidViewModel {
    private final AlarmRepository alarmRepository;

    public CreateAlarmViewModel(@NonNull Application application) {
        super(application);
//        alarmRepository = new FirebaseAlarmRepositoryImpl(application);
        alarmRepository = new DefaultAlarmRepositoryImpl(application);
    }

    public void insert(Alarm alarm) {
        alarmRepository.insert(alarm);
    }
}
