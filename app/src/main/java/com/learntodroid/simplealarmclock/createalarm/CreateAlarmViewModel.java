package com.learntodroid.simplealarmclock.createalarm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.learntodroid.simplealarmclock.data.Alarm;
import com.learntodroid.simplealarmclock.data.AlarmRepository;
import com.learntodroid.simplealarmclock.data.DefaultAlarmRepositoryImpl;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateAlarmViewModel extends AndroidViewModel {
    private final AlarmRepository alarmRepository;
    private final CompositeDisposable composite = new CompositeDisposable();

    public CreateAlarmViewModel(@NonNull Application application) {
        super(application);
//        alarmRepository = new FirebaseAlarmRepositoryImpl(application);
        alarmRepository = new DefaultAlarmRepositoryImpl(application);
    }

    private Disposable insertDisposable;
    private MutableLiveData<String> noticeLiveData = new MutableLiveData<>();

    public void insert(Alarm alarm) {
        if (insertDisposable != null) {
            insertDisposable.dispose();
        }
        insertDisposable = alarmRepository.insert(alarm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((s, throwable) -> noticeLiveData.setValue(s));
        composite.add(insertDisposable);
    }

    private Disposable updateDispose;
    public void update(Alarm alarm) {
        if (updateDispose != null) {
            updateDispose.dispose();
        }
        updateDispose = alarmRepository.update(alarm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((s, throwable) -> {
                    noticeLiveData.setValue(s);
                });
        composite.add(updateDispose);
    }

    @NotNull
    public LiveData<String> getNoticeLiveData() {
        return noticeLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        composite.dispose();
    }
}
