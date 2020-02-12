package com.remember.app.ui.base;

import androidx.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;
import com.remember.app.NetworkStatus;
import com.remember.app.data.network.ServiceNetwork;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter<V extends BaseView> extends MvpPresenter<V> {

    @Inject
    protected ServiceNetwork serviceNetwork;

    private CompositeDisposable compositeSubscription = new CompositeDisposable();

    protected void unsubscribeOnDestroy(@NonNull Disposable subscription) {
        compositeSubscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    protected ServiceNetwork getServiceNetwork() {
        return serviceNetwork;
    }

    protected boolean isOffline() {
        if (NetworkStatus.isOffline()) {
            getViewState().onErrorOffline();
            return true;
        }
        return false;
    }

}
