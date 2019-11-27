package com.remember.app.ui.grid;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.remember.app.data.Constants.IMAGES_STATUS_APPROVED;

@InjectViewState
public class GridPresenter extends BasePresenter<GridView> {

    GridPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getImages(int count) {
        Disposable subscription = getServiceNetwork().getImages(count, true, true, IMAGES_STATUS_APPROVED)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedImages,
                        getViewState()::onError);
        unsubscribeOnDestroy(subscription);
    }

    public void search(RequestSearchPage requestSearchPage) {
        Disposable subscription = getServiceNetwork().searchPageAllDead(requestSearchPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSearchedPages,
                        getViewState()::onError);
        unsubscribeOnDestroy(subscription);
    }

    void getInfo() {
        Disposable subscription = getServiceNetwork().getInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedInfo,
                        getViewState()::onError);
        unsubscribeOnDestroy(subscription);
    }
}
