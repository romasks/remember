package com.remember.app.ui.auth;

import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseRestorePassword;
import com.remember.app.data.models.ResponseSocialAuth;
import com.remember.app.data.models.ResponseVk;
import com.remember.app.ui.base.BaseView;

public interface AuthView extends BaseView {

    void onLogged(ResponseAuth responseAuth);

    void onError(Throwable throwable);

    void onReceivedInfo(ResponseVk response);

    void onLoggedSocial(ResponseSocialAuth responseSocialAuth);

    void onRestored(ResponseRestorePassword responseRestorePassword);

    void errorRestored(Throwable throwable);
}
