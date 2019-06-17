package com.remember.app.data.network;

import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.data.models.RequestQuestion;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.data.models.ResponseRegister;

import java.io.File;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

public interface ServiceNetwork {

    Observable<List<ResponseHandBook>> getCities();

    Observable<List<ResponseCemetery>> getCemetery(int id);

    Observable<ResponseCemetery> addPage(AddPageModel person, File imageUri);

    Observable<List<ResponseHandBook>> getReligion();

    Observable<List<MemoryPageModel>> getPages();

    Observable<List<ResponseEpitaphs>> getEpitaphs(int pageId);

    Observable<RequestAddEpitaphs> saveEpitaph(RequestAddEpitaphs requestAddEpitaphs);

    Observable<RequestAddEvent> saveEvent(RequestAddEvent requestAddEvent);

    Observable<List<RequestAddEvent>> getEventsForId(int pageId);

    Observable<List<ResponseEvents>> getEvents();

    Observable<ResponseAuth> singInAuth(String login, String password);

    Observable<ResponseRegister> registerLogin(String nickName, String email);

    Observable<List<MemoryPageModel>> getImages();

    Observable<ResponsePages> editPage(AddPageModel person, Integer id, File imageFile);

    Observable<List<MemoryPageModel>> searchLastName(String lastName);

    Observable<Object> send(RequestQuestion requestQuestion);
}
