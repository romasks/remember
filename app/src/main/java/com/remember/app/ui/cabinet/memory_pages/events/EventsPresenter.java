package com.remember.app.ui.cabinet.memory_pages.events;

import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

public class EventsPresenter extends BasePresenter<EventsView> {

    public EventsPresenter() {
        Remember.getApplicationComponent().inject(this);
    }
}
