package com.remember.app.ui.cabinet.memory_pages.show_page;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

import java.io.File;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class ShowPagePresenter extends BasePresenter<ShowPageView> {

    ShowPagePresenter() {
        Remember.applicationComponent.inject(this);
    }

    void getImageAfterSave(Integer id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getImageAfterSave(id)
            .subscribe(
                getViewState()::onReceivedImage,
                getViewState()::error
            );
        unsubscribeOnDestroy(subscription);
    }

    void savePhoto(File imageFile, String string, Integer id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.savePhoto(imageFile, string, id)
            .subscribe(
                getViewState()::onSavedImage,
                getViewState()::error
            );
        unsubscribeOnDestroy(subscription);
    }

    void getImagesSlider(Integer id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getImagesSlider(id)
            .subscribe(
                getViewState()::onImagesSlider,
                getViewState()::error
            );
        unsubscribeOnDestroy(subscription);
    }
}
