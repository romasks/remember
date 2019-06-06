package com.remember.app.data.network;

import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.data.models.ResponseRegister;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface ServiceNetwork {

    Observable<List<ResponseHandBook>> getCities();

    Observable<List<ResponseCemetery>> getCemetery(int id);

    Observable<ResponseCemetery> addPage(AddPageModel person);

    Observable<List<ResponseHandBook>> getReligion();

    Observable<ResponsePages> getPages(int countPage);

    Observable<List<ResponseEpitaphs>> getEpitaphs(int pageId);

    Observable<RequestAddEpitaphs> saveEpitaph(RequestAddEpitaphs requestAddEpitaphs);

    Observable<RequestAddEvent> saveEvent(RequestAddEvent requestAddEvent);

    Observable<List<RequestAddEvent>> getEvents(int pageId);

    Observable<ResponseAuth> singInAuth(String login, String password);

    Observable<ResponseRegister> registerLogin(String nickName, String email);
}
