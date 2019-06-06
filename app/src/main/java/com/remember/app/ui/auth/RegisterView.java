package com.remember.app.ui.auth;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseRegister;

public interface RegisterView extends MvpView {

    void onRegistered(ResponseRegister responseRegister);

    void error(Throwable throwable);
}
