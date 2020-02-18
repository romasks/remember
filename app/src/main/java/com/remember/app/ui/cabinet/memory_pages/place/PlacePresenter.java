package com.remember.app.ui.cabinet.memory_pages.place;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class PlacePresenter extends BasePresenter<PlaceView> {

    PlacePresenter() {
        Remember.applicationComponent.inject(this);
    }

    void getCities() {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getCities()
            .subscribe(getViewState()::onUpdatedCities);
        unsubscribeOnDestroy(subscription);
    }

    void getCemetery(int id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getCemetery(id)
            .subscribe(getViewState()::onUpdatedCemetery);
        unsubscribeOnDestroy(subscription);
    }
}
