package com.remember.app.ui.grid;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class GridPresenter extends BasePresenter<GridView> {

    GridPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getImages(int count) {
        Disposable subscription = getServiceNetwork().getImages(count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedImages);
        unsubscribeOnDestroy(subscription);
    }

    public void search(RequestSearchPage requestSearchPage) {
        Disposable subscription = getServiceNetwork().searchPageAllDead(requestSearchPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSearchedPages);
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
