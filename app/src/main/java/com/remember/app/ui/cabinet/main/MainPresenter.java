package com.remember.app.ui.cabinet.main;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {

    @Inject
    ServiceNetwork serviceNetwork;

    MainPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getReligion() {
        Disposable subscription = serviceNetwork.getReligion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(list -> list)
                .map(ResponseHandBook::getName)
                .toList()
                .subscribe(getViewState()::onReceivedReligions);
        unsubscribeOnDestroy(subscription);
    }

    void searchLastName(RequestSearchPage requestSearchPage) {
        Disposable subscription = serviceNetwork.searchPageAllDead(requestSearchPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSearchedLastNames);
        unsubscribeOnDestroy(subscription);
    }

    void getInfo() {
        Disposable subscription = getServiceNetwork().getInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedInfo);
        unsubscribeOnDestroy(subscription);
    }
}
