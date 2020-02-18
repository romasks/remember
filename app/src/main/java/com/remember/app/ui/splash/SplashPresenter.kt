package com.remember.app.ui.splash

import com.arellomobile.mvp.InjectViewState
import com.remember.app.Remember
import com.remember.app.ui.base.BasePresenter

@InjectViewState
class SplashPresenter : BasePresenter<SplashView>() {

    init {
        Remember.applicationComponent.inject(this)
    }

}
