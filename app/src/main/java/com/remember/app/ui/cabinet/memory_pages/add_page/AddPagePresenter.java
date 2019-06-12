package com.remember.app.ui.cabinet.memory_pages.add_page;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;
import com.remember.app.ui.cabinet.memory_pages.PageView;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

@InjectViewState
public class AddPagePresenter extends BasePresenter<AddPageView> {

    public AddPagePresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    @Inject
    ServiceNetwork serviceNetwork;

    public void addPage(AddPageModel person, MultipartBody.Part imageUri) {
        Disposable subscription = serviceNetwork.addPage(person, imageUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedPage);
        unsubscribeOnDestroy(subscription);
    }

    public void getReligion() {
        Disposable subscription = serviceNetwork.getReligion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onGetedInfo);
        unsubscribeOnDestroy(subscription);
    }
}
