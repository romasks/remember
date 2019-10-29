package com.remember.app.ui.auth;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseRegister;

import retrofit2.Response;

public interface RegisterView extends MvpView {

    void onRegistered(Response<ResponseRegister> responseRegisterResponse);

    void onError(Throwable throwable);
}
