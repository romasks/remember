package com.remember.app.ui.splash;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

    @Inject
    ServiceNetwork serviceNetwork;

    public SplashPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

}
