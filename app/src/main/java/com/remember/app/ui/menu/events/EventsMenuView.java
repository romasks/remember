package com.remember.app.ui.menu.events;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseEvents;

import java.util.List;

public interface EventsMenuView extends MvpView {

    void onReceivedEvents(List<ResponseEvents> responseEvents);

    void error(Throwable throwable);

}
