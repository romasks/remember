package com.remember.app.ui.menu.settings.data;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSettings;
import com.remember.app.ui.base.BasePresenter;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PersonalDataFragmentPresenter extends BasePresenter<PersonalDataFragmentView> {

    PersonalDataFragmentPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getInfo() {
        Disposable subscription = getServiceNetwork().getInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedInfo,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    void saveSettings(RequestSettings requestSettings) {
        Disposable subscription = getServiceNetwork().saveSettings(requestSettings)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSaved,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    void saveImageSetting(File imageFile) {
        Disposable subscription = getServiceNetwork().saveImageSetting(imageFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedImage);
        unsubscribeOnDestroy(subscription);
    }
}
