package com.remember.app.ui.auth;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
}
