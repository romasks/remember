package com.remember.app.ui.cabinet.memory_pages.events;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.RequestAddEvent;

import java.util.List;

import io.reactivex.Observable;

public interface EventsView extends MvpView {

    void onReceivedEvent( List<RequestAddEvent> requestAddEvent);

}
