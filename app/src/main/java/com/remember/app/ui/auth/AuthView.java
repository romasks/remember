package com.remember.app.ui.auth;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseAuth;

public interface AuthView extends MvpView {

    void onLogged(ResponseAuth responseAuth);

    void error(Throwable throwable);
}
