package com.remember.app.data.network;

import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.CreateEventRequest;
import com.remember.app.data.models.EditEventRequest;
import com.remember.app.data.models.EpitNotificationModel;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventNotificationModel;
import com.remember.app.data.models.EventResponse;
import com.remember.app.data.models.MemoryPageModel;
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
import com.remember.app.data.models.ResponseUserInfo;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Response;

public interface ServiceNetwork {

    Observable<List<ResponseHandBook>> getCities();

    Observable<List<ResponseCemetery>> getCemetery(int id);

    Observable<ResponseCemetery> addPage(AddPageModel person, File imageUri);

    Observable<List<ResponseHandBook>> getReligion();

    Observable<List<MemoryPageModel>> getPages();

    Observable<List<ResponseEpitaphs>> getEpitaphs(int pageId);

    Observable<RequestAddEpitaphs> saveEpitaph(RequestAddEpitaphs requestAddEpitaphs);

    //    Observable<RequestAddEvent> saveEvent(RequestAddEvent requestAddEvent);
    Observable<RequestAddEvent> saveEvent(CreateEventRequest createEventRequest, File image);

    Observable<RequestAddEvent> editEvent(EditEventRequest editEventRequest, File image);

    Observable<List<RequestAddEvent>> getEventsForId(int pageId);

    Observable<List<ResponseEvents>> getEvents();

    Observable<List<EventResponse>> getEventsFeed();

    //    Observable<ResponseEvents> getEvent(int id);
    Observable<ResponseEvents> getEvent(int id);

    Observable<EventModel> getDeadEvent(int id);

    Observable<List<EventNotificationModel>> getEventNotifications(String type);

    Observable<List<EpitNotificationModel>> getEpitNotifications();

    Observable<ResponseAuth> singInAuth(String login, String password);

    Observable<Response<ResponseRegister>> registerLogin(String nickName, String email);

    Observable<ResponsePages> getImages(int pageNumber, boolean isStar, boolean flag, String status);

    Observable<MemoryPageModel> editPage(AddPageModel person, Integer id, File imageFile);

    Observable<List<MemoryPageModel>> searchLastName(String lastName);

    Observable<Object> send(RequestQuestion requestQuestion);

    Observable<MemoryPageModel> getImageAfterSave(Integer id);

    Observable<ResponseUserInfo> getInfo();

    Observable<ResponseSettings> getUserSettings();

    Observable<Object> saveSettings(RequestSettings requestSettings);

    Observable<ResponseSocialAuth> signInSocial(RequestSocialAuth request);

    Observable<List<MemoryPageModel>> getAllPages();

    Observable<Object> saveImageSetting(File imageFile);

    Observable<RequestAddEpitaphs> editEpitaph(RequestAddEpitaphs requestAddEpitaphs, Integer id);

    Observable<Object> deleteEpitaph(Integer id);

    Observable<ResponseRestorePassword> restorePassword(String email);

    Observable<List<MemoryPageModel>> searchPageAllDead(RequestSearchPage requestSearchPage);

    Observable<List<ResponseEvents>> searchEventReligious(String date, int selectedIndex);

    Observable<Object> savePhoto(File imageFile, String string, Integer id);

    Observable<List<ResponseImagesSlider>> getImagesSlider(Integer id);

    Observable<Object> deletePage(Integer id);

    Observable<Object> deleteEvent(Integer id);

    Observable<Object> changePassword(RequestBody requestBody);

    Observable<Object> deleteSliderPhoto(Integer id);

    Observable<Object> sendDeviceID(RequestBody id);
}
