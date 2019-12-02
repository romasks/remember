package com.remember.app.ui.cabinet.memory_pages;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PagePresenter extends BasePresenter<PageView> {

    @Inject
    ServiceNetwork serviceNetwork;

    PagePresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getPages() {
        Disposable subscription = serviceNetwork.getPages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedPages);
        unsubscribeOnDestroy(subscription);
    }
}
