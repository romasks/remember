package com.remember.app.ui.grid;

import com.arellomobile.mvp.InjectViewState;
import com.google.gson.JsonObject;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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

    void sendDeviceID(String id){
        if (isOffline()) return;
        JsonObject postParam = new JsonObject();
        postParam.addProperty("platform", "google_play");
        postParam.addProperty("id", id);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), postParam.toString());
        Disposable subscription = serviceNetwork.sendDeviceID(body)
                .subscribe(
                        getViewState()::onStatus,
                        getViewState()::onErrorSendID
                );
        unsubscribeOnDestroy(subscription);
    }

    void refreshToken(){
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.refreshToken()
                .subscribe(
                        getViewState()::refreshToken,
                        getViewState()::onErrorRefresh
                );
        unsubscribeOnDestroy(subscription);
    }
}
