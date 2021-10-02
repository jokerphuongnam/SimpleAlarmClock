package com.learntodroid.simplealarmclock.alarmslist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.learntodroid.simplealarmclock.data.Alarm;
import com.learntodroid.simplealarmclock.data.AlarmRepository;
import com.learntodroid.simplealarmclock.data.DefaultAlarmRepositoryImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlarmsListViewModel extends AndroidViewModel {
    private final AlarmRepository alarmRepository;
    private final MutableLiveData<List<Alarm>> alarmsLiveData = new MutableLiveData<>();
    private Disposable subscribeGetList = null;
    private Disposable subscribeDelete = null;
    private final CompositeDisposable composite = new CompositeDisposable();

    private final MutableLiveData<String> noticeLiveData = new MutableLiveData<>();
    private Disposable updateDispose;


    public AlarmsListViewModel(@NonNull Application application) {
        super(application);
        alarmRepository = new DefaultAlarmRepositoryImpl(application);
    }

    public void getAlarm() {
        if (subscribeGetList != null) {
            subscribeGetList.dispose();
        }
        subscribeGetList = alarmRepository.getAlarms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(alarmsLiveData::setValue, throwable -> {
                    throwable.printStackTrace();
                    throw throwable;
                });

        composite.add(subscribeGetList);
    }

    public void updateToken() {
        alarmRepository.updateToken();
    }

    public void update(Alarm alarm) {
        if (updateDispose != null) {
            updateDispose.dispose();
        }
        updateDispose = alarmRepository.update(alarm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(noticeLiveData::setValue, throwable -> {
                    throwable.printStackTrace();
                    throw throwable;
                });
        composite.add(updateDispose);
    }

    public void delete(@NotNull Alarm alarm) {
        alarm.cancelAlarm(getApplication());
        if (subscribeDelete != null) {
            subscribeDelete.dispose();
        }
        subscribeDelete = alarmRepository.delete(alarm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(noticeLiveData::setValue, throwable -> {
                    throwable.printStackTrace();
                    throw throwable;
                });
        composite.add(subscribeDelete);
    }

    @NotNull
    public LiveData<List<Alarm>> getAlarmsLiveData() {
        return alarmsLiveData;
    }

    @NotNull
    public LiveData<String> getNoticeLiveData(){
        return noticeLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        composite.dispose();
    }
}
