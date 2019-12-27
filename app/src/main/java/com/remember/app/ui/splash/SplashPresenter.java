package com.remember.app.ui.splash;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

    SplashPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

}
