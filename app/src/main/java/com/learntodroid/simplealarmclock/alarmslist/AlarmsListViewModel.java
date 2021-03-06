package com.learntodroid.simplealarmclock.alarmslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.learntodroid.simplealarmclock.data.Alarm;
import com.learntodroid.simplealarmclock.data.AlarmRepository;
import com.learntodroid.simplealarmclock.data.DefaultAlarmRepositoryImpl;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlarmsListViewModel extends AndroidViewModel {
    private final AlarmRepository alarmRepository;
    final MutableLiveData<List<Alarm>> alarmsLiveData = new MutableLiveData<>();
    private Disposable subscribeGetList = null;
    private Disposable subscribeDelete = null;
    private final CompositeDisposable composite = new CompositeDisposable();


    public AlarmsListViewModel(@NonNull Application application) {
        super(application);
//        alarmRepository = new FirebaseAlarmRepositoryImpl(application);
        alarmRepository = new DefaultAlarmRepositoryImpl(application);
        if(subscribeGetList != null){
            subscribeGetList.dispose();
        }
        subscribeGetList = alarmRepository.getAlarms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(alarmsLiveData::setValue, throwable -> {
                    throw throwable;
                });
        composite.add(subscribeGetList);
    }

    public void update(Alarm alarm) {
        alarmRepository.update(alarm);
    }

    public void delete(Alarm alarm) {
        alarm.cancelAlarm(getApplication());
        if(subscribeDelete != null){
            subscribeDelete.dispose();
        }
        subscribeDelete = alarmRepository.delete(alarm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(alarmsLiveData::setValue, throwable -> {
                    throw throwable;
                });
        composite.add(subscribeDelete);
    }

    public LiveData<List<Alarm>> getAlarmsLiveData() {
        return alarmsLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        composite.dispose();
    }
}
