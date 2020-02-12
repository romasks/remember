package com.remember.app.data.network;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class RxSchedulers {

    private final Scheduler ioScheduler;
    private final Scheduler androidScheduler;

    @Inject
    public RxSchedulers() {
        ioScheduler = Schedulers.io();
        androidScheduler = AndroidSchedulers.mainThread();
    }

    public <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(ioScheduler).observeOn(androidScheduler);
    }

}
