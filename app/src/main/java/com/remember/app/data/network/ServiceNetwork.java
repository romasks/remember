package com.remember.app.data.network;

import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.EpitNotificationModel;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventNotificationModel;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.PageEditedResponse;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.data.models.RequestQuestion;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.RequestSettings;
import com.remember.app.data.models.RequestSocialAuth;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.data.models.ResponseRegister;
import com.remember.app.data.models.ResponseRestorePassword;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.data.models.ResponseSocialAuth;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public interface ServiceNetwork {

    Observable<List<ResponseHandBook>> getCities();

    Observable<ResponseCemetery> getCemetery(int id);

    Observable<ResponseCemetery> addPage(AddPageModel person, File imageUri);

    Observable<List<ResponseHandBook>> getReligion();

    Observable<List<MemoryPageModel>> getPages();

    Observable<List<ResponseEpitaphs>> getEpitaphs(int pageId);

    Observable<RequestAddEpitaphs> saveEpitaph(RequestAddEpitaphs requestAddEpitaphs);

    Observable<RequestAddEvent> saveEvent(RequestAddEvent requestAddEvent);

    Observable<List<RequestAddEvent>> getEventsForId(int pageId);

    Observable<List<ResponseEvents>> getEvents();

    Observable<EventModel> getEvent(int id);

    Observable<List<EventNotificationModel>> getEventNotifications(String token, String type);

    Observable<List<EpitNotificationModel>> getEpitNotifications(String token);

    Observable<ResponseAuth> singInAuth(String login, String password);

    Observable<Response<ResponseRegister>> registerLogin(String nickName, String email);

    Observable<ResponsePages> getImages(int count);

//    Observable<ResponsePages> editPage(AddPageModel person, Integer id, File imageFile);
    Observable<PageEditedResponse> editPage(AddPageModel person, Integer id, File imageFile);

    Observable<List<MemoryPageModel>> searchLastName(String lastName);

    Observable<Object> send(RequestQuestion requestQuestion);

    Observable<MemoryPageModel> getImageAfterSave(Integer id);

    Observable<ResponseSettings> getInfo(String token);

    Observable<Object> saveSettings(RequestSettings requestSettings);

    Observable<ResponseSocialAuth> signInVk(String email);

    Observable<ResponseSocialAuth> signInSocial(RequestSocialAuth request);

    Observable<List<MemoryPageModel>> getAllPages();

    Observable<Object> saveImageSetting(File imageFile);

    Observable<RequestAddEpitaphs> editEpitaph(RequestAddEpitaphs requestAddEpitaphs, Integer id);

    Observable<ResponseRestorePassword> restorePassword(String email);

    Observable<List<MemoryPageModel>> searchPageAllDead(RequestSearchPage requestSearchPage);

    Observable<List<ResponseEvents>> searchEventReligios(String date, int selectedIndex);

    Observable<Object> savePhoto(File imageFile, String string, Integer id);

    Observable<List<ResponseImagesSlider>> getImagesSlider(Integer id);
}
