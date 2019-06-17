package com.remember.app.ui.cabinet.main;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
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

    public MainPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getReligion() {
        Disposable subscription = serviceNetwork.getReligion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(list -> list)
                .map(ResponseHandBook::getName)
                .toList()
                .subscribe(getViewState()::onReceivedReligions);
        unsubscribeOnDestroy(subscription);
    }

    public void searchLastName(String lastName) {
        Disposable subscription = serviceNetwork.searchLastName(lastName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSearchedLastNames);
        unsubscribeOnDestroy(subscription);
    }
}
