package com.remember.app.ui.cabinet.events;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

@InjectViewState
public class EventsPresenter extends BasePresenter<EventView> {

    public EventsPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getEvents() {

    }

}
