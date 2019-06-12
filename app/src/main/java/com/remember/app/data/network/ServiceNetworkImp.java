package com.remember.app.data.network;

import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.data.models.RequestAuth;
import com.remember.app.data.models.RequestRegister;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.data.models.ResponseRegister;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
    public Observable<ResponseCemetery> addPage(AddPageModel person, MultipartBody.Part imageUri) {
        RequestBody area = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody secondName = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody thirdName = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody comment = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody birthDate = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody deathDate = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody optradio = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody star = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody flag = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody religion = RequestBody.create(MediaType.parse("text/plain"), "dsgsdg");
        RequestBody image = RequestBody.create(MediaType.parse("text/plain"), imageUri.toString());
        return apiMethods.addPage(area,
                secondName,
                name,
                thirdName,
                comment,
                birthDate,
                deathDate,
                optradio,
                star,
                flag,
                religion,
                image);
    }

    @Override
    public Observable<List<ResponseHandBook>> getReligion() {
        return apiMethods.getReligion();
    }

    @Override
    public Observable<ResponsePages> getPages(int countPage) {
        return apiMethods.getPages(countPage);
    }

    @Override
    public Observable<List<ResponseEpitaphs>> getEpitaphs(int pageId) {
        return apiMethods.getEpitaphs(pageId);
    }

    @Override
    public Observable<RequestAddEpitaphs> saveEpitaph(RequestAddEpitaphs requestAddEpitaphs) {
        return apiMethods.saveEpitaph(requestAddEpitaphs);
    }

    @Override
    public Observable<RequestAddEvent> saveEvent(RequestAddEvent requestAddEvent) {
        return apiMethods.saveEvent(requestAddEvent);
    }

    @Override
    public Observable<List<RequestAddEvent>> getEventsForId(int pageId) {
        return apiMethods.getEventsForId(pageId);
    }

    @Override
    public Observable<List<ResponseEvents>> getEvents() {
        return apiMethods.getEvents();
    }

    @Override
    public Observable<ResponseAuth> singInAuth(String email, String password) {
        RequestAuth requestAuth = new RequestAuth();
        requestAuth.setEmail(email);
        requestAuth.setPassword(password);
        return apiMethods.singInAuth(requestAuth);
    }

    @Override
    public Observable<ResponseRegister> registerLogin(String nickName, String email) {
        RequestRegister requestRegister = new RequestRegister();
        requestRegister.setEmail(email);
        requestRegister.setName(nickName);
        return apiMethods.registerLogin(requestRegister);
    }
}
