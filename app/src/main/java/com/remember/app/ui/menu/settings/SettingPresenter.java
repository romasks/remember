package com.remember.app.ui.menu.settings;

import androidx.lifecycle.MutableLiveData;

import com.arellomobile.mvp.InjectViewState;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.remember.app.Remember;
import com.remember.app.data.models.ErrorResponse;
import com.remember.app.data.models.RequestSettings;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.base.BasePresenter;

import java.io.File;

import javax.inject.Singleton;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.HttpException;

@Singleton
@InjectViewState
public class SettingPresenter extends BasePresenter<SettingView> {

    private RequestSettings requestSettings;

    private MutableLiveData<ResponseSettings> settingsLiveData = new MutableLiveData<>();

    public SettingPresenter() {
        Remember.applicationComponent.inject(this);
        requestSettings = new RequestSettings();
    }

    RequestSettings getRequestSettings() {
        return requestSettings;
    }

    public void getInfo() {
        if (isOffline()) return;
        Disposable subscription = getServiceNetwork().getUserSettings()
                .subscribe(
                        value -> {
                            settingsLiveData.setValue(value);
                            fillRequestSettings(value);
                        },
                        getViewState()::error
                );
        unsubscribeOnDestroy(subscription);
    }

    void saveSettings() {
        if (isOffline()) return;
        Disposable subscription = getServiceNetwork().saveSettings(requestSettings)
                .subscribe(
                        getViewState()::onSaved,
                        getViewState()::error
                );
        unsubscribeOnDestroy(subscription);
    }

    void saveImageSetting(File imageFile) {
        if (isOffline()) return;
        Disposable subscription = getServiceNetwork().saveImageSetting(imageFile)
                .subscribe(
                        getViewState()::onSavedImage,
                        getViewState()::error
                );
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


    public void changePassword(String oldPassword, String newPassword) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("old_pass", oldPassword);
//        jsonObject.put("new_pass", password);
        JsonObject postParam = new JsonObject();
        postParam.addProperty("old_pass", oldPassword);
        postParam.addProperty("new_pass", newPassword);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), postParam.toString());
        if (isOffline()) return;
        Disposable subscription = getServiceNetwork().changePassword(body)
                .subscribe(
                        getViewState()::onSaved,
                        getViewState()::error
                );
        unsubscribeOnDestroy(subscription);
    }

    public ErrorResponse parseError(Throwable throwable) {
        ErrorResponse errorResponse = new ErrorResponse();
        if (throwable instanceof HttpException) {
            HttpException error = (HttpException) throwable;
            errorResponse = new Gson().fromJson(error.response().errorBody().toString(), ErrorResponse.class);
        }
        return errorResponse;
    }
}
