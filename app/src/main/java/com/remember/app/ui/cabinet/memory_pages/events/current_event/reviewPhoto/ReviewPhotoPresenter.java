package com.remember.app.ui.cabinet.memory_pages.events.current_event.reviewPhoto;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class ReviewPhotoPresenter extends BasePresenter<ReviewView> {

    ReviewPhotoPresenter() {
        Remember.applicationComponent.inject(this);
    }

    void deletePhoto(int eventID, int photoID) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.deleteEventPhoto(eventID, photoID)
                .subscribe(getViewState()::onDeleteSliderPhoto, getViewState()::onDeleteSliderPhotoError);
        unsubscribeOnDestroy(subscription);
    }
}
