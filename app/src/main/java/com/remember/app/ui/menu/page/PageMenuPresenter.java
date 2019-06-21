package com.remember.app.ui.menu.page;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PageMenuPresenter extends BasePresenter<PageMenuView> {

    @Inject
    ServiceNetwork serviceNetwork;

    public PageMenuPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getPages() {
        Disposable subscription = serviceNetwork.getPages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedPages,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }
}
