package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.EventModel;


public interface CurrentEventView extends MvpView {

    void onReceivedEvent(EventModel requestEvent);

}
