package com.remember.app.ui.cabinet.memory_pages.add_page;

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

    AddPagePresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    @Inject
    ServiceNetwork serviceNetwork;

    void addPage(AddPageModel person, File imageUri) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.addPage(person, imageUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedPage,
                        getViewState()::onErrorSave);
        unsubscribeOnDestroy(subscription);
    }

    void getReligion() {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getReligion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onGetInfo,
                        getViewState()::onError);
        unsubscribeOnDestroy(subscription);
    }

    void editPage(AddPageModel person, Integer id, File imageFile) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.editPage(person, id, imageFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onEdited,
                        getViewState()::onErrorSave);
        unsubscribeOnDestroy(subscription);
    }
}
