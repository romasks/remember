package com.remember.app.ui.settings.data;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseSettings;

public interface PersonalDataFragmentView extends MvpView {

    void onReceivedInfo(ResponseSettings responseSettings);

    void error(Throwable throwable);
}
