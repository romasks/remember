package com.remember.app.data.network;

import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.Response;

public class ServiceNetworkImp implements ServiceNetwork {

    private ApiMethods apiMethods;

    @Inject
    public ServiceNetworkImp(ApiMethods apiMethods) {
        this.apiMethods = apiMethods;
    }


    @Override
    public Observable<List<ResponseHandBook>> getCities() {
        return apiMethods.getCities();
    }

    @Override
    public Observable<List<ResponseCemetery>> getCemetery(int id) {
        return apiMethods.getCemetery(id);
    }

    @Override
    public Observable<ResponseCemetery> addPage(AddPageModel person) {
        return apiMethods.addPage(person);
    }
}
