package com.remember.app.ui.menu.events;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class EventsMenuPresenter extends BasePresenter<EventsMenuView> {

    EventsMenuPresenter() {
        Remember.applicationComponent.inject(this);
    }

    void getEvents() {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getEvents()
            .subscribe(
                getViewState()::onReceivedEvents,
                getViewState()::error
            );
        unsubscribeOnDestroy(subscription);
    }

    void searchEventReligious(String date, int selectedIndex) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.searchEventReligious(date, selectedIndex)
            .subscribe(
                getViewState()::onReceivedEvents,
                getViewState()::error
            );
        unsubscribeOnDestroy(subscription);
    }
}
