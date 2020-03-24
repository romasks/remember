package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import com.remember.app.data.models.EventModel;
import com.remember.app.ui.base.BaseView;

public interface CurrentEventView extends BaseView {

    void onReceivedEvent(EventModel requestEvent);

}
