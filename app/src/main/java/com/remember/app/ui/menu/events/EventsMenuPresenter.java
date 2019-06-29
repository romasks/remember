package com.remember.app.ui.menu.events;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class EventsMenuPresenter extends BasePresenter<EventsMenuView> {

    @Inject
    ServiceNetwork serviceNetwork;

    public EventsMenuPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getEvents() {
        Disposable subscription = serviceNetwork.getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedEvents,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    public void searchEventReligios(String date, int selectedIndex) {
        Disposable subscription = serviceNetwork.searchEventReligios(date, selectedIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedEvents,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }
}
