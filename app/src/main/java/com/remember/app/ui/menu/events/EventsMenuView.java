package com.remember.app.ui.menu.events;

import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.base.BaseView;

import java.util.List;

public interface EventsMenuView extends BaseView {

    void onReceivedEvents(List<ResponseEvents> responseEvents);

    void error(Throwable throwable);

}
