package com.remember.app.ui.cabinet.memory_pages.events.add_new_event;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AddNewEventPresenter extends BasePresenter<AddNewEventView> {

    @Inject
    ServiceNetwork serviceNetwork;

    public AddNewEventPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void saveEvent(RequestAddEvent requestAddEvent) {
        Disposable subscription = serviceNetwork.saveEvent(requestAddEvent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedEvent);
        unsubscribeOnDestroy(subscription);
    }
}
