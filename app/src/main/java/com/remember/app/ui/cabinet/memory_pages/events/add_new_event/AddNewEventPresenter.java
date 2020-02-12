package com.remember.app.ui.cabinet.memory_pages.events.add_new_event;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.CreateEventRequest;
import com.remember.app.data.models.EditEventRequest;
import com.remember.app.ui.base.BasePresenter;

import java.io.File;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class AddNewEventPresenter extends BasePresenter<AddNewEventView> {

    AddNewEventPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void saveEvent(CreateEventRequest createEventRequest, File image) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.saveEvent(createEventRequest, image)
            .subscribe(
                getViewState()::onSavedEvent,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }

    void editEvent(EditEventRequest editEventRequest, File image) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.editEvent(editEventRequest, image)
            .subscribe(
                getViewState()::onSavedEvent,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }
}
