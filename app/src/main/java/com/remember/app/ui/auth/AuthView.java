package com.remember.app.ui.auth;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseRestorePassword;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.data.models.ResponseVk;

import java.util.List;

public interface AuthView extends MvpView {

    void onLogged(ResponseAuth responseAuth);

    void error(Throwable throwable);

    void onRecievedInfo(ResponseVk response);

    void onLogged(ResponseSettings responseSettings);

    void onRestored(ResponseRestorePassword responseRestorePassword);

    void errorRestored(Throwable throwable);
}
