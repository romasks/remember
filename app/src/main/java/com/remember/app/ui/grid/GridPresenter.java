package com.remember.app.ui.grid;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

import static com.remember.app.data.Constants.IMAGES_STATUS_APPROVED;

@InjectViewState
public class GridPresenter extends BasePresenter<GridView> {

    GridPresenter() {
        Remember.applicationComponent.inject(this);
    }

    void getImages(int pageNumber) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getImages(pageNumber, true, true, IMAGES_STATUS_APPROVED)
            .subscribe(
                getViewState()::onReceivedImages,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }

    public void search(RequestSearchPage requestSearchPage) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.searchPageAllDead(requestSearchPage)
            .subscribe(
                getViewState()::onSearchedPages,
                getViewState()::onError
            );
        unsubscribeOnDestroy(subscription);
    }
}
