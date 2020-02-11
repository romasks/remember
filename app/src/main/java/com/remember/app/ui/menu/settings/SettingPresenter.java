package com.remember.app.ui.menu.settings;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestSettings;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.base.BasePresenter;

import java.io.File;

import javax.inject.Singleton;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
@InjectViewState
public class SettingPresenter extends BasePresenter<SettingView> {

    private RequestSettings requestSettings;

    private MutableLiveData<ResponseSettings> settingsLiveData = new MutableLiveData<>();

    SettingPresenter() {
        Remember.getApplicationComponent().inject(this);
        requestSettings = new RequestSettings();
    }

    RequestSettings getRequestSettings() {
        return requestSettings;
    }

    void getInfo() {
        if (isOffline()) return;
        Disposable subscription = getServiceNetwork().getUserSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                            settingsLiveData.setValue(value);
                            fillRequestSettings(value);
                        },
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    void saveSettings() {
        if (isOffline()) return;
        Disposable subscription = getServiceNetwork().saveSettings(requestSettings)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSaved,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    void saveImageSetting(File imageFile) {
        if (isOffline()) return;
        Disposable subscription = getServiceNetwork().saveImageSetting(imageFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedImage,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }

    private void fillRequestSettings(ResponseSettings value) {
        requestSettings
                .name(value.getName())
                .surname(value.getSurname())
                .middleName(value.getThirdname())
                .email(value.getEmail())
                .nickname(value.getNickname())
                .location(String.valueOf(value.getLocation()))
                .phone(value.getPhone())
                .enableNotifications(value.getNotificationsEnabled())
                .commemorationDays(value.getIdNotice())
                .amountDays(value.getAmountDays());
    }

    MutableLiveData<ResponseSettings> getSettingsLiveData() {
        return settingsLiveData;
    }
}
