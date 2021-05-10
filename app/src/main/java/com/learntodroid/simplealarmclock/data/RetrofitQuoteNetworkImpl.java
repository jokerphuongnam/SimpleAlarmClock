package com.learntodroid.simplealarmclock.data;

import org.jetbrains.annotations.NotNull;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RetrofitQuoteNetworkImpl implements QuoteNetwork {

    private static QuoteNetwork INSTANCE = null;

    public static QuoteNetwork getInstance(){
        if(INSTANCE == null){
            INSTANCE = new RetrofitQuoteNetworkImpl();
        }
        return INSTANCE;
    }

    private RetrofitQuoteNetworkImpl() {

    }

    @Override
    public Single<Quote> getQuote() {
        return getHandle().getQuote().map(quoteResponse -> {
            return quoteResponse.body();
        });
    }

    interface RetrofitHandel{
        @GET("api/quote/random/1")
        Single<Response<Quote>> getQuote();
    }

    @NotNull
    @SuppressWarnings("FieldCanBeLocal")
    private final String BASE_URL = "http://lamapi.somee.com/";

    private RetrofitHandel handle;

    @NotNull
    private RetrofitHandel getHandle(){
        if (handle == null) {
            handle = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(RetrofitSingleTone.getOkHttpClient())
                    .build()
                    .create(RetrofitHandel.class);
        }
        return handle;
    }
}
