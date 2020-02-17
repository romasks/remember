package com.remember.app.ui.cabinet.memory_pages.events;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class EventsPresenter extends BasePresenter<EventsView> {

    public EventsPresenter() {
        Remember.applicationComponent.inject(this);
    }

    void getEvents(int pageId) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getEventsForId(pageId)
            .subscribe(getViewState()::onReceivedEvent);
        unsubscribeOnDestroy(subscription);
    }
}
