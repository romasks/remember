package com.remember.app.ui.cabinet.memory_pages.add_page;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AddPagePresenter extends BasePresenter<AddPageView> {

    public AddPagePresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    @Inject
    ServiceNetwork serviceNetwork;

    public void addPage(AddPageModel person, File imageUri) {
        Disposable subscription = serviceNetwork.addPage(person, imageUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedPage);
        unsubscribeOnDestroy(subscription);
    }

    private void onError(Throwable throwable){
        Log.e("AddPagePresenter", "exception", throwable);
    }
    public void getReligion() {
        Disposable subscription = serviceNetwork.getReligion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onGettedInfo);
        unsubscribeOnDestroy(subscription);
    }

    public void editPage(AddPageModel person, Integer id, File imageFile) {
        Disposable subscription = serviceNetwork.editPage(person, id, imageFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onEdited,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }
}
