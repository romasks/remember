package com.remember.app.ui.cabinet.memory_pages.add_page;

import android.graphics.Bitmap;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;
import com.remember.app.ui.cabinet.memory_pages.PageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    public void getReligion() {
        Disposable subscription = serviceNetwork.getReligion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onGetedInfo);
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
