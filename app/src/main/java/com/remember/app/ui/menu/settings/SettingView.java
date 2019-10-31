package com.remember.app.ui.menu.settings;

import com.arellomobile.mvp.MvpView;

public interface SettingView extends MvpView {

    void error(Throwable throwable);

    void onSaved(Object o);

    void onSavedImage(Object o);
}
