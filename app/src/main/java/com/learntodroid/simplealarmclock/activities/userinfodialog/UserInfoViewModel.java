package com.learntodroid.simplealarmclock.activities.userinfodialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.learntodroid.simplealarmclock.data.AlarmRepository;
import com.learntodroid.simplealarmclock.data.DefaultAlarmRepositoryImpl;

public class UserInfoViewModel extends AndroidViewModel {
    private final AlarmRepository repository;

    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        repository = DefaultAlarmRepositoryImpl.getInstance(application);
    }

    public void onClearListener(){
        repository.onClearListener();
    }
}
