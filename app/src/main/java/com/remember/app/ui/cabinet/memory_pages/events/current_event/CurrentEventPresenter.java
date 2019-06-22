package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

@InjectViewState
public class CurrentEventPresenter extends BasePresenter<CurrentEventView> {

    public CurrentEventPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

}
