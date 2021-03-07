package com.learntodroid.simplealarmclock.data;

import android.app.Application;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class FirebaseAlarmRepositoryImpl implements AlarmNetwork {
    private AlarmDao alarmDao;
    private Single<List<Alarm>> alarmsLiveData;
    private FirebaseFirestore fb;

    public FirebaseAlarmRepositoryImpl(Application application) {
        AlarmDatabase db = AlarmDatabase.getDatabase(application);
        alarmDao = db.alarmDao();
        fb = FirebaseFirestore.getInstance();
        new Thread(() -> {
            alarmDao.deleteAll();
        }).start();
        fb.collection("alarms").addSnapshotListener((queryDocumentSnapshots, e) -> new Thread(() -> {
            Alarm alarm;
            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        Log.i("insert", "-----------------");
                        alarm = dc.getDocument().toObject(Alarm.class);
                        alarmDao.insert(alarm);
                        break;
                    case MODIFIED:
                        Log.i("update", "-----------------");
                        alarm = dc.getDocument().toObject(Alarm.class);
                        alarmDao.update(alarm);
                        break;
                    case REMOVED:
                        Log.i("remove", "-----------------");
                        alarm = dc.getDocument().toObject(Alarm.class);
                        alarmDao.delete(alarm);
                        break;
                    default:
                        break;
                }
            }
        }).start());
        alarmsLiveData = alarmDao.getAlarms();

    }

    @Override
    public Single<String> insert(Alarm alarm) {
        fb.collection("alarms").document(String.valueOf(alarm.getAlarmId())).set(alarm, SetOptions.merge());

//        AlarmDatabase.databaseWriteExecutor.execute(() -> {
//            alarmDao.insert(alarm);
//        });
        return null;
    }

    @Override
    public Single<String> update(Alarm alarm) {
        fb.collection("alarms").document(String.valueOf(alarm.getAlarmId())).set(alarm, SetOptions.merge());
//        AlarmDatabase.databaseWriteExecutor.execute(() -> {
//            alarmDao.update(alarm);
//        });
        return null;
    }

    @Override
    public Single<String> delete(Alarm alarm) {
        fb.collection("alarms").document(String.valueOf(alarm.getAlarmId())).delete();
        return null;
    }

    @Override
    public Single<List<Alarm>> getAlarms() {
        return alarmsLiveData;
    }
}
