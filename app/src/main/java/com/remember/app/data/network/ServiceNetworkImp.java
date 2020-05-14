package com.remember.app.data.network;

import com.google.gson.JsonObject;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.data.models.AddComment;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.AddPhoto;
import com.remember.app.data.models.AddVideo;
import com.remember.app.data.models.DeleteVideo;
import com.remember.app.data.models.EventComments;
import com.remember.app.data.models.CreateEventRequest;
import com.remember.app.data.models.EditEventRequest;
import com.remember.app.data.models.EpitNotificationModel;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventNotificationModel;
import com.remember.app.data.models.EventResponse;
import com.remember.app.data.models.EventSliderPhotos;
import com.remember.app.data.models.EventVideos;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.data.models.RequestQuestion;
import com.remember.app.data.models.RequestRegister;
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
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static com.remember.app.data.Constants.PREFS_KEY_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.ui.utils.StringUtils.getVideoIdFromUrl;

public class ServiceNetworkImp implements ServiceNetwork {

    private RxSchedulers rxSchedulers;
    private ApiMethods apiMethods;

    @Inject
    ServiceNetworkImp(RxSchedulers rxSchedulers, ApiMethods apiMethods) {
        this.rxSchedulers = rxSchedulers;
        this.apiMethods = apiMethods;
    }

    @Override
    public Observable<List<ResponseHandBook>> getCities() {
        return apiMethods.getCities()
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<ResponseCemetery>> getCemetery(int id) {
        return apiMethods.getCemetery(id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<ResponseHandBook>> getReligion() {
        return apiMethods.getReligion()
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<MemoryPageModel>> getPages() {
        return apiMethods.getPages(Prefs.getString(PREFS_KEY_USER_ID, "0"))
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<ResponseEpitaphs>> getEpitaphs(int pageId) {
        return apiMethods.getEpitaphs(pageId)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<RequestAddEpitaphs> saveEpitaph(RequestAddEpitaphs requestAddEpitaphs) {
        return apiMethods.saveEpitaph(requestAddEpitaphs)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<RequestAddEvent> saveEvent(CreateEventRequest createEventRequest, File imageFile) {
        RequestBody pageId = RequestBody.create(MultipartBody.FORM, createEventRequest.getPageId());
        RequestBody date = RequestBody.create(MultipartBody.FORM, createEventRequest.getDate());
        RequestBody name = RequestBody.create(MultipartBody.FORM, createEventRequest.getName());
        RequestBody flag = RequestBody.create(MultipartBody.FORM, createEventRequest.getFlag());
        RequestBody uvShow = RequestBody.create(MultipartBody.FORM, createEventRequest.getUvShow());
        RequestBody description = RequestBody.create(MultipartBody.FORM, createEventRequest.getDescription());

        MultipartBody.Part fileToUploadTransfer = null;
        if (imageFile != null) {
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            fileToUploadTransfer = MultipartBody.Part.createFormData("picture", imageFile.getName(), mFile);
        }

        String token = "Bearer " + Prefs.getString("TOKEN", "");

        return apiMethods.saveEvent(token, pageId, date, name, flag, uvShow, description, fileToUploadTransfer)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<RequestAddEvent> editEvent(EditEventRequest editEventRequest, File image) {
        RequestBody pageId = RequestBody.create(MultipartBody.FORM, editEventRequest.getPageId());
        RequestBody date = RequestBody.create(MultipartBody.FORM, editEventRequest.getDate());
        RequestBody name = RequestBody.create(MultipartBody.FORM, editEventRequest.getName());
        RequestBody flag = RequestBody.create(MultipartBody.FORM, editEventRequest.getFlag());
        RequestBody uvShow = RequestBody.create(MultipartBody.FORM, editEventRequest.getUvShow());
        RequestBody description = RequestBody.create(MultipartBody.FORM, editEventRequest.getDescription());


        MultipartBody.Part fileToUploadTransfer = null;
        if (image != null) {
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
            fileToUploadTransfer = MultipartBody.Part.createFormData("picture", image.getName(), mFile);
        }

        String token = "Bearer " + Prefs.getString("TOKEN", "");
        return apiMethods.editEvent(token, pageId, date, name, flag, uvShow, description, fileToUploadTransfer,
                editEventRequest.getEventId()
        )
                .compose(rxSchedulers.applySchedulers());

    }

    @Override
    public Observable<List<RequestAddEvent>> getEventsForId(int pageId) {
        return apiMethods.getEventsForId(pageId)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<ResponseEvents>> getEvents() {
        return apiMethods.getEvents()
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<EventResponse>> getEventsFeed() {
        String token = "Bearer " + Prefs.getString("TOKEN", "");
        String eventsType = "DEAD_EVENTS";
        return apiMethods.getEventsFeed(token, eventsType)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<ResponseEvents> getEvent(int id) {
        return apiMethods.getEvent(id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<EventModel> getDeadEvent(int id) {
        return apiMethods.getDeadEvent(id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<EventNotificationModel>> getEventNotifications(String filterType) {
        return apiMethods.getEventNotifications("Bearer " + Prefs.getString("TOKEN", ""), filterType);
    }

    @Override
    public Observable<List<EpitNotificationModel>> getEpitNotifications() {
        return apiMethods.getEpitNotifications("Bearer " + Prefs.getString("TOKEN", ""));
    }

    @Override
    public Observable<ResponseAuth> singInAuth(String email, String password) {
        return apiMethods.singInAuth(email, password)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Response<ResponseRegister>> registerLogin(String nickName, String email) {
        RequestRegister requestRegister = new RequestRegister();
        requestRegister.setEmail(email);
        requestRegister.setName(nickName);
        return apiMethods.registerLogin(requestRegister)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<ResponsePages> getImages(int pageNumber, boolean isStar, boolean flag, String status) {
        return apiMethods.getImages(pageNumber, status, flag, isStar)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<MemoryPageModel>> searchLastName(String lastName) {
        return apiMethods.searchLastName(lastName)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> send(RequestQuestion requestQuestion) {
        return apiMethods.send(requestQuestion)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<MemoryPageModel> getImageAfterSave(Integer id) {
        return apiMethods.getImageAfterSave(id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<ResponseUserInfo> getInfo() {
        return apiMethods.getInfo("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""))
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<ResponseSettings> getUserSettings() {
        return apiMethods.getUserSettings("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""))
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> saveSettings(RequestSettings requestSettings) {
        return apiMethods.saveSettings("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), requestSettings)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<ResponseSocialAuth> signInSocial(RequestSocialAuth request) {
        return apiMethods.signInSocial(request)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<MemoryPageModel>> getAllPages() {
        return apiMethods.getAllPages()
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> saveImageSetting(File imageFile) {
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part fileToUploadTransfer = MultipartBody.Part.createFormData(
                "picture",
                imageFile.getName(),
                mFile
        );
        return apiMethods.savePhotoSettings(fileToUploadTransfer, "Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""))
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<RequestAddEpitaphs> editEpitaph(RequestAddEpitaphs requestAddEpitaphs, Integer id) {
        return apiMethods.editEpitaph(requestAddEpitaphs, id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> deleteEpitaph(Integer id) {
        return apiMethods.deleteEpitaph("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<ResponseRestorePassword> restorePassword(String email) {
        return apiMethods.restorePassword(email)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<MemoryPageModel>> searchPageAllDead(RequestSearchPage requestSearchPage) {
        return apiMethods.searchPageAllDead(
                requestSearchPage.getName(),
                requestSearchPage.getSecondName(),
                requestSearchPage.getThirdName(),
                requestSearchPage.getDateBegin(),
                requestSearchPage.getDateEnd(),
                requestSearchPage.getCity(),
                requestSearchPage.getStatus(),
                requestSearchPage.isFlag()
        )
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<ResponseEvents>> searchEventReligious(String date, int selectedIndex) {
        String religia;
        switch (selectedIndex) {
            case 0:
                religia = "Православие";
                break;
            case 1:
                religia = "Католицизм";
                break;
            case 2:
                religia = "Ислам";
                break;
            case 3:
                religia = "Иудаизм";
                break;
            case 4:
                religia = "Буддизм";
                break;
            case 5:
                religia = "Индуизм";
                break;
            case 6:
                religia = "Другая религия";
                break;
            case 7:
            default:
                religia = "Религия";
                break;
        }
        String resultDate;
        if (!date.isEmpty()) {
            //resultDate = date.substring(0, date.length() - 5);
            resultDate = date;
        } else {
            resultDate = "";
        }
        System.out.println("date = " + resultDate);
        System.out.println("religia = " + religia);

        if (resultDate.equals("")) {
            return apiMethods.searchEventReligiosOnlyWithReligia(religia)
                    .compose(rxSchedulers.applySchedulers());
        } else if (selectedIndex == 7) {
            return apiMethods.searchEventReligiosOnlyWithDate(resultDate)
                    .compose(rxSchedulers.applySchedulers());
        } else {
            return apiMethods.searchEventReligios(resultDate, religia)
                    .compose(rxSchedulers.applySchedulers());
        }
    }

    @Override
    public Observable<Object> savePhoto(File imageFile, String string, Integer id) {
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part fileToUploadTransfer = MultipartBody.Part.createFormData(
                "picture",
                imageFile.getName(),
                mFile
        );
        MultipartBody.Part imageCut = MultipartBody.Part.createFormData("picture_cut", imageFile.getName(), mFile);
        String token = "Bearer " + Prefs.getString(PREFS_KEY_TOKEN, "");
        return apiMethods.savePhoto(token, string, id, fileToUploadTransfer, imageCut)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<List<ResponseImagesSlider>> getImagesSlider(Integer id) {
        return apiMethods.getAllPhotosForPage(id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<ResponseCemetery> addPage(AddPageModel person, File imageFile) {
        RequestBody area = RequestBody.create(MultipartBody.FORM, person.getArea());
        RequestBody birthDate = RequestBody.create(MultipartBody.FORM, person.getBirthDate());
        RequestBody cemeteryName = RequestBody.create(MultipartBody.FORM, person.getCemeteryName());
        RequestBody city = RequestBody.create(MultipartBody.FORM, person.getCity());
        RequestBody comment = RequestBody.create(MultipartBody.FORM, person.getComment());
        RequestBody coords = RequestBody.create(MultipartBody.FORM, person.getCoords());
        RequestBody deathDate = RequestBody.create(MultipartBody.FORM, person.getDeathDate());
        RequestBody district = RequestBody.create(MultipartBody.FORM, person.getDistrict());
        RequestBody flag = RequestBody.create(MultipartBody.FORM, person.getFlag());
        RequestBody grave = RequestBody.create(MultipartBody.FORM, person.getGraveId());
        RequestBody sector = RequestBody.create(MultipartBody.FORM, person.getSector());
        RequestBody name = RequestBody.create(MultipartBody.FORM, person.getName());
        RequestBody optradio = RequestBody.create(MultipartBody.FORM, person.getOptradio());
        RequestBody religion = RequestBody.create(MultipartBody.FORM, person.getReligion());
        RequestBody secondNam = RequestBody.create(MultipartBody.FORM, person.getSecondName());
        RequestBody spotId = RequestBody.create(MultipartBody.FORM, person.getSpotId());
        RequestBody star = RequestBody.create(MultipartBody.FORM, person.getStar());
        RequestBody thirdName = RequestBody.create(MultipartBody.FORM, person.getThirdName());
        RequestBody userId = RequestBody.create(MultipartBody.FORM, person.getUserId());
        MultipartBody.Part fileToUploadTransfer = null;

        if (imageFile != null) {
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            fileToUploadTransfer = MultipartBody.Part.createFormData("picture_data", imageFile.getName(), mFile);
        }
        String token = "Bearer " + Prefs.getString(PREFS_KEY_TOKEN, "");
        return apiMethods.addPage(token, area, birthDate, cemeteryName, city, sector, comment, coords, deathDate,
                district, flag, grave, name, optradio, religion, secondNam, spotId, star, thirdName, userId,
                fileToUploadTransfer
        )
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<MemoryPageModel> editPage(AddPageModel person, Integer id, File imageFile) {
        RequestBody area = RequestBody.create(MultipartBody.FORM, person.getArea());
        RequestBody birthDate = RequestBody.create(MultipartBody.FORM, person.getBirthDate());
        RequestBody cemeteryName = RequestBody.create(MultipartBody.FORM, person.getCemeteryName());
        RequestBody city = RequestBody.create(MultipartBody.FORM, person.getCity());
        RequestBody comment = RequestBody.create(MultipartBody.FORM, person.getComment());
        RequestBody coords = RequestBody.create(MultipartBody.FORM, person.getCoords());
        RequestBody deathDate = RequestBody.create(MultipartBody.FORM, person.getDeathDate());
        RequestBody district = RequestBody.create(MultipartBody.FORM, person.getDistrict());
        RequestBody flag = RequestBody.create(MultipartBody.FORM, person.getFlag());
        RequestBody grave = RequestBody.create(MultipartBody.FORM, person.getGraveId());
        RequestBody sector = RequestBody.create(MultipartBody.FORM, person.getSector());
        RequestBody name = RequestBody.create(MultipartBody.FORM, person.getName());
        RequestBody optradio = RequestBody.create(MultipartBody.FORM, person.getOptradio());
        RequestBody religion = RequestBody.create(MultipartBody.FORM, person.getReligion());
        RequestBody secondNam = RequestBody.create(MultipartBody.FORM, person.getSecondName());
        RequestBody spotId = RequestBody.create(MultipartBody.FORM, person.getSpotId());
        RequestBody star = RequestBody.create(MultipartBody.FORM, person.getStar());
        RequestBody thirdName = RequestBody.create(MultipartBody.FORM, person.getThirdName());
        RequestBody userId = RequestBody.create(MultipartBody.FORM, person.getUserId());

        RequestBody mFile = null;
        MultipartBody.Part fileToUploadTransfer = null;

        if (imageFile != null) {
            mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            fileToUploadTransfer = MultipartBody.Part.createFormData("picture_data", imageFile.getName(), mFile);
            String token = "Bearer " + Prefs.getString(PREFS_KEY_TOKEN, "");
            return apiMethods.editPage(token, area, birthDate, cemeteryName, city, sector, comment, coords, deathDate,
                    district, flag, grave, name, optradio, religion, secondNam, spotId, star, thirdName, userId,
                    fileToUploadTransfer, id
            )
                    .compose(rxSchedulers.applySchedulers());
        } else {
            String token = "Bearer " + Prefs.getString(PREFS_KEY_TOKEN, "");
            return apiMethods.editPageWithoutImage(token, area, birthDate, cemeteryName, city, sector, comment, coords,
                    deathDate, district, flag, grave, name, optradio, religion, secondNam, spotId, star, thirdName,
                    userId, id
            )
                    .compose(rxSchedulers.applySchedulers());
        }
    }

    @Override
    public Observable<Object> deletePage(Integer id){
        return apiMethods.deletePage("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> deleteEvent(Integer id){
        return apiMethods.deleteEvent("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> changePassword(RequestBody requestBody) {
        return apiMethods.changePassword("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), requestBody)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> deleteSliderPhoto(Integer id){
        return apiMethods.deleteSliderPhoto("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> sendDeviceID(RequestBody id){
        return apiMethods.sendDeviceID(id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<ArrayList<EventComments>> getEventComments(int id) {
        return apiMethods.getEventComments("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> addComment(int id, AddComment body) {
        return apiMethods.addComment("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id, body)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> editComment(int id, int commentId, AddComment body) {
        return apiMethods.editComment("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id,commentId, body)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> deleteComment(int id, int commentId) {
        return apiMethods.deleteComment("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id,commentId)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<ArrayList<EventVideos>> getEventVideo(int id) {
        return apiMethods.getEventVideo("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> addVideo(int id, AddVideo body) {
        return apiMethods.addVideo("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id, body)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> deleteVideo(int id, DeleteVideo body) {
        return apiMethods.deleteVideo("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id,body)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<ArrayList<EventSliderPhotos>> getEventPhoto(int id) {
        return apiMethods.getEventPhoto("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> addEventPhoto(int id,String description, File img) {
        MultipartBody.Part photo = null;
        if (img != null) {
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), img);
            photo = MultipartBody.Part.createFormData("picture", img.getName(), mFile);
        }

        MultipartBody.Part photoCut = null;
        if (img != null) {
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), img);
            photoCut = MultipartBody.Part.createFormData("picture_cut", img.getName(), mFile);
        }
        AddPhoto body = new AddPhoto();
        body.setLink(description);
        return apiMethods.addEventPhoto("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id,body, photo, photoCut)
                .compose(rxSchedulers.applySchedulers());
    }

    @Override
    public Observable<Object> deleteEventPhoto(int id, int photoID) {
        return apiMethods.deleteEventPhoto("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id,photoID)
                .compose(rxSchedulers.applySchedulers());
    }
}
