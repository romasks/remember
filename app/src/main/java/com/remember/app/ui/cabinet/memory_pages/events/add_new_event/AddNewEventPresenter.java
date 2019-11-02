package com.remember.app.ui.cabinet.memory_pages.events.add_new_event;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.CreateEventRequest;
import com.remember.app.data.models.EditEventRequest;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AddNewEventPresenter extends BasePresenter<AddNewEventView> {

    @Inject
    ServiceNetwork serviceNetwork;

    AddNewEventPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    //    public void saveEvent(RequestAddEvent requestAddEvent) {
    public void saveEvent(CreateEventRequest createEventRequest, File image) {
        Disposable subscription = serviceNetwork.saveEvent(createEventRequest, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedEvent,
                        getViewState()::onError);
        unsubscribeOnDestroy(subscription);
    }

    public void editEvent(EditEventRequest editEventRequest, File image) {
        Disposable subscription = serviceNetwork.editEvent(editEventRequest, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedEvent,
                        getViewState()::onError);
        unsubscribeOnDestroy(subscription);
    }
}
