package com.remember.app.data.network;

import com.remember.app.data.models.ResponseCities;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ServiceNetworkImp implements ServiceNetwork {

    private ApiMethods apiMethods;

    @Inject
    public ServiceNetworkImp(ApiMethods apiMethods) {
        this.apiMethods = apiMethods;
    }


    @Override
    public Observable<List<ResponseCities>> getCities() {
        return apiMethods.getCities();
    }
}
