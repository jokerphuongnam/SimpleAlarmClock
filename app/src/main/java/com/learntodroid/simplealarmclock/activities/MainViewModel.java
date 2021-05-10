package com.learntodroid.simplealarmclock.activities;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.learntodroid.simplealarmclock.data.Quote;
import com.learntodroid.simplealarmclock.data.QuoteNetwork;
import com.learntodroid.simplealarmclock.data.RetrofitQuoteNetworkImpl;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    public MainViewModel(){
        quoteNetwork = RetrofitQuoteNetworkImpl.getInstance();
    }


    private QuoteNetwork quoteNetwork;
    private Disposable disposable;

    private SingleObserver<Quote> observer = new SingleObserver<Quote>() {
        @Override
        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
            disposable = d;
        }

        @Override
        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Quote quote) {
            if (disposable != null) {
                disposable.dispose();
            }
            quoteLiveData.postValue(String.valueOf(quote.getText().replaceAll("/N", "\n").replaceAll("/n", "\n")));
        }

        @Override
        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
            if (disposable != null) {
                disposable.dispose();
            }
            e.printStackTrace();
        }
    };

    private MutableLiveData<String> quoteLiveData;

    public MutableLiveData<String> getQuoteLiveData() {
        if(quoteLiveData == null ){
            quoteLiveData = new MutableLiveData<>();
        }
        getQuote();
        return quoteLiveData;
    }

    void getQuote(){
        quoteNetwork.getQuote().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    @Override
    protected void onCleared() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
