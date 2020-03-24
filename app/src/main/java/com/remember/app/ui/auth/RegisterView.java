package com.remember.app.ui.auth;

import com.remember.app.data.models.ResponseRegister;
import com.remember.app.ui.base.BaseView;

import retrofit2.Response;

public interface RegisterView extends BaseView {

    void onRegistered(Response<ResponseRegister> responseRegisterResponse);

    void onError(Throwable throwable);
}
