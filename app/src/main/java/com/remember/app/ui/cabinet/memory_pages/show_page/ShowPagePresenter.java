package com.remember.app.ui.cabinet.memory_pages.show_page;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ShowPagePresenter extends BasePresenter<ShowPageView> {

    @Inject
    ServiceNetwork serviceNetwork;

    ShowPagePresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getImageAfterSave(Integer id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getImageAfterSave(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedImage,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    void savePhoto(File imageFile, String string, Integer id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.savePhoto(imageFile, string, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedImage,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    void getImagesSlider(Integer id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getImagesSlider(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onImagesSlider,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }
}
