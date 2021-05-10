package com.learntodroid.simplealarmclock.data;

import io.reactivex.rxjava3.core.Single;

public interface QuoteNetwork {
    Single<Quote> getQuote();
}
