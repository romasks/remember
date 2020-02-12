package com.remember.app.ui.cabinet.events;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class EventsPresenter extends BasePresenter<EventView> {

    public EventsPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getEvents() {
        if (isOffline()) return;
        Disposable subscription = getServiceNetwork().getEventsFeed()
            .subscribe(
                getViewState()::onReceivedEvents,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }

    void getEvent(int id) {
        if (isOffline()) return;
        Disposable subscription = getServiceNetwork().getEvent(id)
            .subscribe(
                getViewState()::onReceivedDeadEvent,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }
}
