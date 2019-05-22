package com.remember.app.ui.cabinet.memory_pages.place;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PlacePresenter extends BasePresenter<PlaceView> {

    @Inject
    ServiceNetwork serviceNetwork;

    public PlacePresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getCities() {
        Disposable subscription = serviceNetwork.getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onUpdatedCities);
        unsubscribeOnDestroy(subscription);
    }

    public void getCemetery(int id) {
        Disposable subscription = serviceNetwork.getCemetery(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onUpdatedCemetery);
        unsubscribeOnDestroy(subscription);
    }
}
