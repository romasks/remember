package com.remember.app.ui.cabinet.events;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class EventsPresenter extends BasePresenter<EventView> {

    public EventsPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getEvents() {
        Disposable subscription = getServiceNetwork().getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedEvents);
        unsubscribeOnDestroy(subscription);
    }

}
