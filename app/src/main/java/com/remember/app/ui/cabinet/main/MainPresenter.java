package com.remember.app.ui.cabinet.main;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {

    MainPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getReligion() {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getReligion()
            .flatMapIterable(list -> list)
            .map(ResponseHandBook::getName)
            .toList()
            .subscribe(
                getViewState()::onReceivedReligions,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }

    void searchLastName(RequestSearchPage requestSearchPage) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.searchPageAllDead(requestSearchPage)
            .subscribe(
                getViewState()::onSearchedLastNames,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }

    void getInfo() {
        if (isOffline()) return;
        Disposable subscription = getServiceNetwork().getInfo()
            .subscribe(
                getViewState()::onReceivedInfo,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }
}
