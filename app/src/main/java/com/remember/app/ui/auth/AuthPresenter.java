package com.remember.app.ui.auth;

import com.arellomobile.mvp.InjectViewState;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSocialAuth;
import com.remember.app.data.models.ResponseVkResponse;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.remember.app.data.Constants.PREFS_KEY_ACCESS_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;

@InjectViewState
public class AuthPresenter extends BasePresenter<AuthView> {

    @Inject
    ServiceNetwork serviceNetwork;

    public AuthPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void singInAuth(String login, String password) {
        Disposable subscription = serviceNetwork.singInAuth(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onLogged,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    public void getInfoUser() {
        VKRequest request = new VKRequest("account.getProfileInfo");
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                ResponseVkResponse responseVk = new Gson().fromJson(response.json.toString(), ResponseVkResponse.class);
                getViewState().onRecievedInfo(responseVk.getResponse());
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

    public void signInVk() {
        Disposable subscription = serviceNetwork.signInVk(Prefs.getString(PREFS_KEY_EMAIL, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onLoggedSocial,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    public void signInTwitter() {
        Disposable subscription = serviceNetwork.signInVk(Prefs.getString(PREFS_KEY_EMAIL, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onLoggedSocial,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    public void signInFacebook() {
        RequestSocialAuth request = new RequestSocialAuth(
                Prefs.getString(PREFS_KEY_EMAIL, ""),
                Prefs.getString(PREFS_KEY_ACCESS_TOKEN, ""),
                "fb"
        );
        Disposable subscription = serviceNetwork.signInSocial(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onLoggedSocial,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    public void restorePassword(String email) {
        Disposable subscription = serviceNetwork.restorePassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onRestored,
                        getViewState()::errorRestored);
        unsubscribeOnDestroy(subscription);
    }
}
