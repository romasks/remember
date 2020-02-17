package com.remember.app.ui.auth;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class RegisterPresenter extends BasePresenter<RegisterView> {

    RegisterPresenter() {
        Remember.applicationComponent.inject(this);
    }

    void registerLogin(String nickName, String email) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.registerLogin(nickName, email)
            .subscribe(
                getViewState()::onRegistered,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }

}
