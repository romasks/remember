package com.remember.app.ui.cabinet.memory_pages.events;

import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.base.BaseView;

import java.util.List;

public interface EventsView extends BaseView {

    void onReceivedEvent(List<RequestAddEvent> requestAddEvent);

}
