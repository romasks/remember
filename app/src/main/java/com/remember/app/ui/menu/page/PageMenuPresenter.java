package com.remember.app.ui.menu.page;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class PageMenuPresenter extends BasePresenter<PageMenuView> {

    PageMenuPresenter() {
        Remember.applicationComponent.inject(this);
    }

    void getImages(int pageNumber) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getImages(pageNumber, false, false, "")
            .subscribe(
                getViewState()::onReceivedPages,
                getViewState()::error
            );
        unsubscribeOnDestroy(subscription);
    }

    public void search(RequestSearchPage requestSearchPage) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.searchPageAllDead(requestSearchPage)
            .subscribe(
                getViewState()::onSearchedPages,
                getViewState()::error
            );
        unsubscribeOnDestroy(subscription);
    }
}
