package com.remember.app.ui.auth;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseRestorePassword;
import com.remember.app.data.models.ResponseSocialAuth;
import com.remember.app.data.models.ResponseVk;

public interface AuthView extends MvpView {

    void onLogged(ResponseAuth responseAuth);

    void error(Throwable throwable);

    void onReceivedInfo(ResponseVk response);

    void onLoggedSocial(ResponseSocialAuth responseSocialAuth);

    void onRestored(ResponseRestorePassword responseRestorePassword);

    void errorRestored(Throwable throwable);
}
