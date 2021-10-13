package com.learntodroid.simplealarmclock.data;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class FirebaseAlarmImpl implements AlarmNetwork {
    @Nullable
    private static AlarmNetwork INSTANCE = null;
    private final FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private final PublishSubject<List<Alarm>> alarmsPublisher = PublishSubject.create();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private ListenerRegistration getAlarmsListener = null;

    private FirebaseAlarmImpl() {

    }

    @Override
    public void initListen() {
        if (auth.getUid() != null) {
            getAlarmsListener = fb
                    .collection("user")
                    .document(auth.getUid())
                    .collection("alarms")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> new Thread(() -> {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            Alarm alarm;
                            List<Alarm> alarms = new ArrayList<>();
                            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                                alarm = ds.toObject(Alarm.class);
                                if (alarm != null) {
                                    alarms.add(alarm);
                                }
                            }
                            alarmsPublisher.onNext(alarms);
                        }
                    }).start());
        }
    }

    @Override
    public void onClearListener() {
        if (getAlarmsListener != null) {
            getAlarmsListener.remove();
        }
    }

    @NotNull
    public static AlarmNetwork getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseAlarmImpl();
        }
        return INSTANCE;
    }

    @Override
    public Single<String> insert(Alarm alarm) {
        return Single.create(emitter -> AlarmDatabase.databaseWriteExecutor.execute(() -> {
                    if (auth.getUid() != null) {
                        fb
                                .collection("user")
                                .document(auth.getUid())
                                .collection("alarms")
                                .document(String.valueOf(alarm.getAlarmId()))
                                .set(alarm, SetOptions.merge())
                                .addOnSuccessListener(unused -> emitter.onSuccess(String.valueOf(alarm.getAlarmId())))
                                .addOnFailureListener(emitter::onError);
                    } else {
                        emitter.onError(new NullPointerException());
                    }
                }
        ));
    }

    @Override
    public Single<String> update(Alarm alarm) {
        return insert(alarm);
    }

    @Override
    public Single<String> delete(Alarm alarm) {
        return Single.create(emitter -> {
            if (auth.getUid() != null) {
                fb
                        .collection("user")
                        .document(auth.getUid())
                        .collection("alarms")
                        .document(String.valueOf(alarm.getAlarmId()))
                        .delete()
                        .addOnSuccessListener(unused -> emitter.onSuccess(String.valueOf(alarm.getAlarmId())))
                        .addOnFailureListener(emitter::onError);
            } else {
                emitter.onError(new NullPointerException());
            }
        });
    }

    @Override
    public Observable<List<Alarm>> getAlarms() {
        return alarmsPublisher;
    }

    @Override
    public void updateToken() {
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(task -> new Thread(() -> {
//                    if (!task.isSuccessful()) {
//                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                        return;
//                    }
//                    // Get new FCM registration token
//                    String token = task.getResult();
//                    // gui token len db voi
//                    String uid = auth.getUid();
//                    if (uid != null) {
//                        Map<String, String> tokens = new HashMap<>();
//                        tokens.put("deviceToken", token);
//                        fb
//                                .collection("user")
//                                .document(uid)
//                                .set(tokens);
//                    }
//                    Log.i(TAG, token != null ? token : "");
//                }).start());
    }

    @Override
    public void refresh() {
        if (auth.getUid() != null) {
            fb
                    .collection("user")
                    .document(auth.getUid())
                    .collection("alarms")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> new Thread(() -> {
                                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                    Alarm alarm;
                                    List<Alarm> alarms = new ArrayList<>();
                                    for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                                        alarm = ds.toObject(Alarm.class);
                                        if (alarm != null) {
                                            alarms.add(alarm);
                                        }
                                    }
                                    alarmsPublisher.onNext(alarms);
                                }
                            }
                            ).start()
                    );
        }
    }
}
