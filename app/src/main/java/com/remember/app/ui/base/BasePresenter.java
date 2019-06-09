package com.remember.app.ui.base;

import androidx.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.remember.app.data.network.ServiceNetwork;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter<V extends MvpView> extends MvpPresenter<V> {

    @Inject
    ServiceNetwork serviceNetwork;

    private CompositeDisposable compositeSubscription = new CompositeDisposable();

    public void unsubscribeOnDestroy(@NonNull Disposable subscription) {
        compositeSubscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    public ServiceNetwork getServiceNetwork() {
        return serviceNetwork;
    }

}
