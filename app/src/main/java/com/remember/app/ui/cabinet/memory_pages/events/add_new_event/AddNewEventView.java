package com.remember.app.ui.cabinet.memory_pages.events.add_new_event;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.EditEventRequest;
import com.remember.app.data.models.RequestAddEvent;

public interface AddNewEventView extends MvpView {

    void onSavedEvent(RequestAddEvent requestAddEvent);

    void onEditEvent(EditEventRequest editEventRequest);

    void onError(Throwable throwable);
}
