package com.remember.app.ui.cabinet.events;

import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventResponse;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.base.BaseView;

import java.util.List;


public interface EventView extends BaseView {

    void onReceivedEvents(List<EventResponse> responseEvents);

    void onError(Throwable throwable);

    void onReceivedEvent(ResponseEvents responseEvents);

    void onReceivedDeadEvent(EventModel eventModel);
}
