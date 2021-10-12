package com.learntodroid.simplealarmclock.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.learntodroid.simplealarmclock.data.AlarmRepository;
import com.learntodroid.simplealarmclock.data.DefaultAlarmRepositoryImpl;

public class LaunchViewModel extends AndroidViewModel {
    private final AlarmRepository repository;

    public LaunchViewModel(@NonNull Application application) {
        super(application);
        repository = DefaultAlarmRepositoryImpl.getInstance(application);
    }

    public void register(){
        repository.initListen();
    }
}
