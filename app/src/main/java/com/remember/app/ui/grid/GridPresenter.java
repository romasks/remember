package com.remember.app.ui.grid;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class GridPresenter extends BasePresenter<GridView> {

    @Inject
    ServiceNetwork serviceNetwork;

    public GridPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getImages() {
        Disposable subscription = serviceNetwork.getImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedImages);
        unsubscribeOnDestroy(subscription);
    }
}
