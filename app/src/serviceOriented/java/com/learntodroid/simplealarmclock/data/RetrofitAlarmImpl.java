package com.learntodroid.simplealarmclock.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import static android.content.ContentValues.TAG;

public class RetrofitAlarmImpl implements AlarmNetwork {
    @Nullable
    private static AlarmNetwork INSTANCE = null;

    public static AlarmNetwork getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitAlarmImpl();
        }
        return INSTANCE;
    }

    private final Locale locale;

    @Override
    public void initListen() {

    }

    @Override
    public void onClearListener() {

    }

    private RetrofitAlarmImpl() {
        locale = Locale.getDefault();
    }

    @Override
    public Single<String> insert(Alarm alarm) {
        return getHandle().insert(
                FirebaseAuth.getInstance().getUid(),
                String.format(locale,"%02d:%02d", alarm.getHour(), alarm.getMinute()),
                alarm.getTitle(),
                alarm.isRecurring(),
                alarm.isMonday(),
                alarm.isTuesday(),
                alarm.isWednesday(),
                alarm.isThursday(),
                alarm.isFriday(),
                alarm.isSaturday(),
                alarm.isSunday()
        );
    }

    @Override
    public Single<String> update(Alarm alarm) {
        return getHandle().update(
                FirebaseAuth.getInstance().getUid(),
                alarm.getAlarmId(),
                String.format(locale,"%02d:%02d", alarm.getHour(), alarm.getMinute()),
                alarm.getTitle(),
                alarm.isRecurring(),
                alarm.isMonday(),
                alarm.isTuesday(),
                alarm.isWednesday(),
                alarm.isThursday(),
                alarm.isFriday(),
                alarm.isSaturday(),
                alarm.isSunday()
        );
    }

    @Override
    public Single<String> delete(Alarm alarm) {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid",FirebaseAuth.getInstance().getUid());
        map.put("aid",String.valueOf(alarm.getAlarmId()));
        return getHandle().delete(map);
    }

    @Override
    public Observable<List<Alarm>> getAlarms() {
        return getHandle().getAlarms(FirebaseAuth.getInstance().getUid()).toObservable();
    }

    @Override
    public void updateToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    String token = task.getResult();
                    // gui token len db voi
                    getHandle().updateToken(FirebaseAuth.getInstance().getUid(),token)
                            .subscribeOn(Schedulers.io())
                            .subscribe((s, throwable) -> {});
                    Log.i(TAG, token);
                });

    }

    interface RetrofitHandle {
        @FormUrlEncoded
        @POST("updateToken")
        Single<String> updateToken(
                @Field("uid") String uid,
                @Field("token") String token
        );

        @FormUrlEncoded
        @POST("new")
        Single<String> insert(
                @Field("uid") String uid,
                @Field("time") String time,
                @Field("title") String title,
                @Field("onoffswitch") boolean recurring,
                @Field("monday_cb") boolean monday,
                @Field("tuesday_cb") boolean tuesday,
                @Field("wednesday_cb") boolean wednesday,
                @Field("thursday_cb") boolean thursday,
                @Field("friday_cb") boolean friday,
                @Field("saturday_cb") boolean saturday,
                @Field("sunday_cb") boolean sunday_cb
        );

        @FormUrlEncoded
        @POST("update")
        Single<String> update(
                @Field("uid") String uid,
                @Field("aid") int id,
                @Field("time") String time,
                @Field("title") String title,
                @Field("onoffswitch") boolean recurring,
                @Field("monday_cb") boolean monday,
                @Field("tuesday_cb") boolean tuesday,
                @Field("wednesday_cb") boolean wednesday,
                @Field("thursday_cb") boolean thursday,
                @Field("friday_cb") boolean friday,
                @Field("saturday_cb") boolean saturday,
                @Field("sunday_cb") boolean sunday_cb
        );

        @GET("cancel")
        Single<String> delete(@QueryMap Map<String, String> options);

        @GET("list")
        Single<List<Alarm>> getAlarms(@Query("uid") String uid);
    }

    @NotNull
    @SuppressWarnings("FieldCanBeLocal")
    private final String BASE_URL = "https://remotealarmapi.herokuapp.com/";
    @Nullable
    private RetrofitHandle handle = null;

    @NotNull
    private RetrofitHandle getHandle() {
        if (handle == null) {
            handle = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(RetrofitSingleTone.getOkHttpClient())
                    .build()
                    .create(RetrofitHandle.class);
        }
        return handle;
    }

    @Override
    public void refresh() {

    }
}
