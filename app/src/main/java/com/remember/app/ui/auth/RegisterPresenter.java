package com.remember.app.ui.auth;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.presenter.InjectPresenter;
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

    public RegisterPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void registerLogin(String nickName, String email) {
        Disposable subscription = serviceNetwork.registerLogin(nickName, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onRegistered,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

}
