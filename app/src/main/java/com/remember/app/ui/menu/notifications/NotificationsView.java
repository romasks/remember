package com.remember.app.ui.menu.notifications;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.NotificationModelNew;

import java.util.List;

public interface NotificationsView extends MvpView {

    void onNotificationsLoaded(List<? extends NotificationModelNew> notifications);

}
