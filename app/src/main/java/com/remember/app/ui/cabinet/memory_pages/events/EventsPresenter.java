package com.remember.app.ui.cabinet.memory_pages.events;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class EventsPresenter extends BasePresenter<EventsView> {

    @Inject
    ServiceNetwork serviceNetwork;

    public EventsPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getEvents(int pageId) {
        Disposable subscription = serviceNetwork.getEventsForId(pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedEvent);
        unsubscribeOnDestroy(subscription);
    }
}
