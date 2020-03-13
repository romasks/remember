package com.remember.app.ui.auth;

import com.arellomobile.mvp.InjectViewState;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSocialAuth;
import com.remember.app.data.models.ResponseVkResponse;
import com.remember.app.ui.base.BasePresenter;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import io.reactivex.disposables.Disposable;

import static com.remember.app.data.Constants.PREFS_KEY_ACCESS_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;

@InjectViewState
public class AuthPresenter extends BasePresenter<AuthView> {

    AuthPresenter() {
        Remember.applicationComponent.inject(this);
    }

    void singInAuth(String login, String password) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.singInAuth(login, password)
            .subscribe(
                getViewState()::onLogged,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }

    void getInfoUser() {
        if (isOffline()) return;
        VKRequest request = new VKRequest("account.getProfileInfo");
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                ResponseVkResponse responseVk = new Gson().fromJson(response.json.toString(), ResponseVkResponse.class);
                getViewState().onReceivedInfo(responseVk.getResponse());
            }

            @Override
            public void onError(VKError error) {
                System.out.println();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                System.out.println();
            }
        });
    }

    void signInVk() {
        if (isOffline()) return;
        RequestSocialAuth request = new RequestSocialAuth(
            Prefs.getString(PREFS_KEY_EMAIL, ""),
            Prefs.getString(PREFS_KEY_ACCESS_TOKEN, ""),
            "vk"
        );
        Disposable subscription = serviceNetwork.signInSocial(request)
            .subscribe(
                getViewState()::onLoggedSocial,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }

    void signInFacebook() {
        if (isOffline()) return;
        RequestSocialAuth request = new RequestSocialAuth(
            Prefs.getString(PREFS_KEY_EMAIL, ""),
            Prefs.getString(PREFS_KEY_ACCESS_TOKEN, ""),
            "fb"
        );
        Disposable subscription = serviceNetwork.signInSocial(request)
            .subscribe(
                getViewState()::onLoggedSocial,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }

    void signInOk() {
        if (isOffline()) return;
        RequestSocialAuth request = new RequestSocialAuth(
            "",
            Prefs.getString(PREFS_KEY_ACCESS_TOKEN, ""),
            "ok"
        );
        Disposable subscription = serviceNetwork.signInSocial(request)
            .subscribe(
                getViewState()::onLoggedSocial,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }

    void signInMailRu() {
        if (isOffline()) return;
        RequestSocialAuth request = new RequestSocialAuth(
                "",
                Prefs.getString(PREFS_KEY_ACCESS_TOKEN, ""),
                "mailru"
        );
        Disposable subscription = serviceNetwork.signInSocial(request)
                .subscribe(
                        getViewState()::onLoggedSocial,
                        getViewState()::onError
                );
        unsubscribeOnDestroy(subscription);
    }

    void restorePassword(String email) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.restorePassword(email)
            .subscribe(
                getViewState()::onRestored,
                getViewState()::errorRestored
            );
        unsubscribeOnDestroy(subscription);
    }

    @Override
    public boolean isOffline() {
        return super.isOffline();
    }
}
