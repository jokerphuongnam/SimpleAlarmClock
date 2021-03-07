package com.learntodroid.simplealarmclock.data;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class RetrofitAlarmImpl implements AlarmNetwork {
    @Nullable
    private static RetrofitAlarmImpl instance = null;

    public static RetrofitAlarmImpl getInstance() {
        if (instance == null) {
            instance = new RetrofitAlarmImpl();
        }
        return instance;
    }

    private final Locale locale;

    private RetrofitAlarmImpl() {
        locale = Locale.getDefault();
    }

    @Override
    public Single<String> insert(Alarm alarm) {
        return getHandle().insert(
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
        return getHandle().delete(alarm.getAlarmId());
    }

    @Override
    public Single<List<Alarm>> getAlarms() {
        return getHandle().getAlarms();
    }

    interface RetrofitHandle {
        @FormUrlEncoded
        @POST("new")
        Single<String> insert(
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

        @POST("cancel/{id}")
        Single<String> delete(@Path("id") long alarmId);

        @GET("list")
        Single<List<Alarm>> getAlarms();
    }

    @NotNull
    @SuppressWarnings("FieldCanBeLocal")
    private final String BASE_URL = "https://remotealarmapi.herokuapp.com/";
    @Nullable
    private RetrofitHandle handle = null;

    @NotNull
    private RetrofitHandle getHandle() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        if (handle == null) {
            handle = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
                    .create(RetrofitHandle.class);
        }
        return handle;
    }
}
