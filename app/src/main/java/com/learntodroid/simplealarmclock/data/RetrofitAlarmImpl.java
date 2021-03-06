package com.learntodroid.simplealarmclock.data;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    private RetrofitAlarmImpl() {
    }

    @Override
    public Single<List<Alarm>> insert(Alarm alarm) {
        return getHandle().insert(alarm);
    }

    @Override
    public Single<List<Alarm>> update(Alarm alarm) {
        return getHandle().update(alarm);
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
        @POST()
        Single<List<Alarm>> insert(Alarm alarm);

        @FormUrlEncoded
        @PUT()
        Single<List<Alarm>> update(Alarm alarm);

        @POST("cancel/{id}")
        Single<String> delete(@Path("id") long alarmId);

        @GET("list")
        Single<List<Alarm>> getAlarms();
    }

    @NotNull
    static private final String BASE_URL = "https://remotealarmapi.herokuapp.com/";
    @Nullable
    static private RetrofitHandle handle = null;

    @NotNull
    private static RetrofitHandle getHandle() {
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
