package com.remember.app.ui.settings.notification;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

@InjectViewState
public class NotificationFragmentPresenter extends BasePresenter<NotificationFragmentView> {

    public NotificationFragmentPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

}
