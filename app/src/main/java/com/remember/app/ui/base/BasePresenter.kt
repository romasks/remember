package com.remember.app.ui.base

import androidx.annotation.NonNull
import com.arellomobile.mvp.MvpPresenter
import com.remember.app.NetworkStatus
import com.remember.app.data.network.ServiceNetwork
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

open class BasePresenter<V : BaseView> : MvpPresenter<V>() {

    @Inject
    protected lateinit var serviceNetwork: ServiceNetwork

    private val compositeSubscription = CompositeDisposable()

    protected fun unsubscribeOnDestroy(@NonNull subscription: Disposable) {
        compositeSubscription.add(subscription)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.clear()
    }

    protected open fun isOffline(): Boolean {
        if (NetworkStatus.isOffline()) {
            viewState.onErrorOffline()
            return true
        }
        return false
    }

}
