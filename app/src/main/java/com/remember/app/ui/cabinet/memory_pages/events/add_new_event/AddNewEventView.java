package com.remember.app.ui.cabinet.memory_pages.events.add_new_event;

import com.remember.app.data.models.EditEventRequest;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.base.BaseView;

public interface AddNewEventView extends BaseView {

    void onSavedEvent(RequestAddEvent requestAddEvent);

    void onEditEvent(EditEventRequest editEventRequest);

    void onError(Throwable throwable);
}
