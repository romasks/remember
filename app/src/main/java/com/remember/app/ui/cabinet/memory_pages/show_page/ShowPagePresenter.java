package com.remember.app.ui.cabinet.memory_pages.show_page;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.base.BasePresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class ShowPagePresenter extends BasePresenter<ShowPageView> {

    ShowPagePresenter() {
        Remember.applicationComponent.inject(this);
    }

    public List<ResponseImagesSlider> images = new ArrayList<>();

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

    void deleteSliderPhoto(Integer id){
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.deleteSliderPhoto(id)
                .subscribe(
                        getViewState()::onDeleteSliderPhoto,
                        getViewState()::onDeleteSliderPhotoError
                );
        unsubscribeOnDestroy(subscription);
    }
}
