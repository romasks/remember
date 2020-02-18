package com.remember.app.ui.menu.notifications;

import com.remember.app.data.models.NotificationModelNew;
import com.remember.app.ui.base.BaseView;

import java.util.List;

public interface NotificationsView extends BaseView {

    void onNotificationsLoaded(List<? extends NotificationModelNew> notifications);

    void onError(Throwable throwable);
}
