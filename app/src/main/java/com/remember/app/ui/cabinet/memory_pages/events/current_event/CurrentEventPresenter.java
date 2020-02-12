package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class CurrentEventPresenter extends BasePresenter<CurrentEventView> {

    CurrentEventPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getDeadEvent(int id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getDeadEvent(id)
            .subscribe(getViewState()::onReceivedEvent);
        unsubscribeOnDestroy(subscription);
    }
}
