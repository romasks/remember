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
public class RegisterPresenter extends BasePresenter<RegisterView> {

    @Inject
    ServiceNetwork serviceNetwork;

    RegisterPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void registerLogin(String nickName, String email) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.registerLogin(nickName, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onRegistered,
                        getViewState()::onError);
        unsubscribeOnDestroy(subscription);
    }

}
