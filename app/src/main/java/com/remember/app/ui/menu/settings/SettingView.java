package com.remember.app.ui.menu.settings;

import com.remember.app.ui.base.BaseView;

public interface SettingView extends BaseView {

    void error(Throwable throwable);

    void onSaved(Object o);

    void onSavedImage(Object o);
}
