package com.remember.app.ui.cabinet.memory_pages;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class PagePresenter extends BasePresenter<PageView> {

    PagePresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getPages() {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getPages()
            .subscribe(getViewState()::onReceivedPages);
        unsubscribeOnDestroy(subscription);
    }
}
