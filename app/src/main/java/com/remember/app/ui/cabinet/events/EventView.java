package com.remember.app.ui.cabinet.events;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseEvents;

import java.util.List;


public interface EventView extends MvpView {

    void onReceivedEvents(List<ResponseEvents> responseEvents);

    void onError(Throwable throwable);

    void onReceivedEvent(ResponseEvents responseEvents);
}
