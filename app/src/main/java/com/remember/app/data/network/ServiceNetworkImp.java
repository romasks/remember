package com.remember.app.data.network;

import com.pixplicity.easyprefs.library.Prefs;
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
import com.remember.app.ui.adapters.ImagesDataFactory;
import com.remember.app.ui.adapters.ImagesRepositoryPagedListConfig;
import com.remember.app.ui.adapters.SearchedImagesDataFactory;
import com.remember.app.ui.adapters.SearchedImagesRepositoryPagedListConfig;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static com.remember.app.data.Constants.PREFS_KEY_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;

public class ServiceNetworkImp implements ServiceNetwork {

    private ApiMethods apiMethods;
    private ImagesDataFactory imagesDataFactory = null;
    private ImagesRepositoryPagedListConfig imagesRepositoryPagedListConfig = null;
    private SearchedImagesRepositoryPagedListConfig searchedImagesRepositoryPagedListConfig = null;

    @Inject
    ServiceNetworkImp(ApiMethods apiMethods) {
        this.apiMethods = apiMethods;
        if (imagesDataFactory == null) {
            imagesDataFactory = new ImagesDataFactory(this);
        }
        if (imagesRepositoryPagedListConfig == null) {
            imagesRepositoryPagedListConfig = new ImagesRepositoryPagedListConfig(imagesDataFactory);
        }
    }


    @Override
    public ImagesRepositoryPagedListConfig getImagesRepositoryPagedListConfig() {
        return imagesRepositoryPagedListConfig;
    }

    @Override
    public SearchedImagesRepositoryPagedListConfig getSearchedImagesRepositoryPagedListConfig(RequestSearchPage requestSearchPage) {
        return searchedImagesRepositoryPagedListConfig = new SearchedImagesRepositoryPagedListConfig(
                new SearchedImagesDataFactory(this, requestSearchPage)
        );
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
    public Observable<List<ResponseHandBook>> getReligion() {
        return apiMethods.getReligion();
    }

    @Override
    public Observable<List<MemoryPageModel>> getPages() {
        return apiMethods.getPages(Prefs.getString(PREFS_KEY_USER_ID, "0"));
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
    public Observable<RequestAddEvent> saveEvent(CreateEventRequest createEventRequest, File imageFile) {
//        return apiMethods.saveEvent(token, requestAddEvent);
        RequestBody pageId = null;
        RequestBody date = null;
        RequestBody name = null;
        RequestBody flag = null;
        RequestBody uvShow = null;
        RequestBody description = null;
        MultipartBody.Part fileToUploadTransfer = null;
        try {
            pageId = RequestBody.create(MultipartBody.FORM, createEventRequest.getPageId());
        } catch (Exception e) {
            pageId = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            date = RequestBody.create(MultipartBody.FORM, createEventRequest.getDate());
        } catch (Exception e) {
            date = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            name = RequestBody.create(MultipartBody.FORM, createEventRequest.getName());
        } catch (Exception e) {
            name = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            flag = RequestBody.create(MultipartBody.FORM, createEventRequest.getFlag());
        } catch (Exception e) {
            flag = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            uvShow = RequestBody.create(MultipartBody.FORM, createEventRequest.getUvShow());
        } catch (Exception e) {
            uvShow = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            description = RequestBody.create(MultipartBody.FORM, createEventRequest.getDescription());
        } catch (Exception e) {
            description = RequestBody.create(MultipartBody.FORM, "");
        }
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        fileToUploadTransfer = MultipartBody.Part.createFormData("picture", imageFile.getName(), mFile);
        String token = "Bearer " + Prefs.getString("TOKEN", "");
        return apiMethods.saveEvent(token,
                pageId,
                date,
                name,
                flag,
                uvShow,
                description,
                fileToUploadTransfer);
    }

    @Override
    public Observable<RequestAddEvent> editEvent(EditEventRequest editEventRequest, File image) {
        RequestBody pageId = null;
        RequestBody date = null;
        RequestBody name = null;
        RequestBody flag = null;
        RequestBody uvShow = null;
        RequestBody description = null;
        MultipartBody.Part fileToUploadTransfer = null;
        try {
            pageId = RequestBody.create(MultipartBody.FORM, editEventRequest.getPageId());
        } catch (Exception e) {
            pageId = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            date = RequestBody.create(MultipartBody.FORM, editEventRequest.getDate());
        } catch (Exception e) {
            date = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            name = RequestBody.create(MultipartBody.FORM, editEventRequest.getName());
        } catch (Exception e) {
            name = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            flag = RequestBody.create(MultipartBody.FORM, editEventRequest.getFlag());
        } catch (Exception e) {
            flag = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            uvShow = RequestBody.create(MultipartBody.FORM, editEventRequest.getUvShow());
        } catch (Exception e) {
            uvShow = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            description = RequestBody.create(MultipartBody.FORM, editEventRequest.getDescription());
        } catch (Exception e) {
            description = RequestBody.create(MultipartBody.FORM, "");
        }
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        fileToUploadTransfer = MultipartBody.Part.createFormData("picture", image.getName(), mFile);
        String token = "Bearer " + Prefs.getString("TOKEN", "");
        return apiMethods.editEvent(token,
                pageId,
                date,
                name,
                flag,
                uvShow,
                description,
                fileToUploadTransfer,
                editEventRequest.getEventId());
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
    public Observable<List<EventResponse>> getEventsFeed() {
        String token = "Bearer " + Prefs.getString("TOKEN", "");
        String eventsType = "DEAD_EVENTS";
//        String eventsType = "ALL";
        return apiMethods.getEventsFeed(token, eventsType);
    }

    @Override
    public Observable<ResponseEvents> getEvent(int id) {
        return apiMethods.getEvent(id);
    }

    @Override
    public Observable<EventModel> getDeadEvent(int id) {
        return apiMethods.getDeadEvent(id);
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
        return apiMethods.singInAuth(email, password);
    }

    @Override
    public Observable<Response<ResponseRegister>> registerLogin(String nickName, String email) {
        RequestRegister requestRegister = new RequestRegister();
        requestRegister.setEmail(email);
        requestRegister.setName(nickName);
        return apiMethods.registerLogin(requestRegister);
    }

    @Override
    public Observable<ResponsePages> getImages(int pageNumber, boolean isStar, boolean flag, String status) {
        return apiMethods.getImages(pageNumber, status, flag, isStar);
    }

    @Override
    public Observable<List<MemoryPageModel>> searchLastName(String lastName) {
        return apiMethods.searchLastName(lastName);
    }

    @Override
    public Observable<Object> send(RequestQuestion requestQuestion) {
        return apiMethods.send(requestQuestion);
    }

    @Override
    public Observable<MemoryPageModel> getImageAfterSave(Integer id) {
        return apiMethods.getImageAfterSave(id);
    }

    @Override
    public Observable<ResponseUserInfo> getInfo() {
        return apiMethods.getInfo("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""));
    }

    @Override
    public Observable<ResponseSettings> getUserSettings() {
        return apiMethods.getUserSettings("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""));
    }

    @Override
    public Observable<Object> saveSettings(RequestSettings requestSettings) {
        return apiMethods.saveSettings("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), requestSettings);
    }

    @Override
    public Observable<ResponseSocialAuth> signInVk(String email) {
        String name = Prefs.getString("USER_NAME", "");
        return apiMethods.signInVk(email, name);
    }

    @Override
    public Observable<ResponseSocialAuth> signInSocial(RequestSocialAuth request) {
        return apiMethods.signInSocial(request);
    }

    @Override
    public Observable<List<MemoryPageModel>> getAllPages() {
        return apiMethods.getAllPages();
    }

    @Override
    public Observable<Object> saveImageSetting(File imageFile) {
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part fileToUploadTranser = MultipartBody.Part.createFormData("picture", imageFile.getName(), mFile);
        return apiMethods.savePhotoSettings(fileToUploadTranser, "Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""));
    }

    @Override
    public Observable<RequestAddEpitaphs> editEpitaph(RequestAddEpitaphs requestAddEpitaphs, Integer id) {
        return apiMethods.editEpitaph(requestAddEpitaphs, id);
    }

    @Override
    public Observable<Object> deleteEpitaph(Integer id) {
        return apiMethods.deleteEpitaph("Bearer " + Prefs.getString(PREFS_KEY_TOKEN, ""), id);
    }

    @Override
    public Observable<ResponseRestorePassword> restorePassword(String email) {
        return apiMethods.restorePassword(email);
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
        );
    }

    @Override
    public Observable<List<ResponseEvents>> searchEventReligios(String date, int selectedIndex) {
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
                religia = "Отсутствует";
                break;
            default:
                religia = "Отсутствует";
                break;
        }
        String resultDate;
        if (!date.isEmpty()) {
            resultDate = date.substring(0, date.length() - 5);
        } else {
            resultDate = "";
        }
        return apiMethods.searchEventReligios(resultDate, religia);
    }

    @Override
    public Observable<Object> savePhoto(File imageFile, String string, Integer id) {
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part fileToUploadTransfer = MultipartBody.Part.createFormData("picture", imageFile.getName(), mFile);
        MultipartBody.Part imageCut = MultipartBody.Part.createFormData("picture_cut", imageFile.getName(), mFile);
        String token = "Bearer " + Prefs.getString(PREFS_KEY_TOKEN, "");
        return apiMethods.savePhoto(token, string, id, fileToUploadTransfer, imageCut);
    }

    @Override
    public Observable<List<ResponseImagesSlider>> getImagesSlider(Integer id) {
        return apiMethods.getAllPhotosForPage(id);
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

        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        fileToUploadTransfer = MultipartBody.Part.createFormData("picture_data", imageFile.getName(), mFile);
        String token = "Bearer " + Prefs.getString(PREFS_KEY_TOKEN, "");
        return apiMethods.addPage(
                token,
                area,
                birthDate,
                cemeteryName,
                city,
                sector,
                comment,
                coords,
                deathDate,
                district,
                flag,
                grave,
                name,
                optradio,
                religion,
                secondNam,
                spotId,
                star,
                thirdName,
                userId,
                fileToUploadTransfer
        );
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
            return apiMethods.editPage(
                    token,
                    area,
                    birthDate,
                    cemeteryName,
                    city,
                    sector,
                    comment,
                    coords,
                    deathDate,
                    district,
                    flag,
                    grave,
                    name,
                    optradio,
                    religion,
                    secondNam,
                    spotId,
                    star,
                    thirdName,
                    userId,
                    fileToUploadTransfer,
                    id

            );
        } else {
            String token = "Bearer " + Prefs.getString(PREFS_KEY_TOKEN, "");
            return apiMethods.editPageWithoutImage(
                    token,
                    area,
                    birthDate,
                    cemeteryName,
                    city,
                    sector,
                    comment,
                    coords,
                    deathDate,
                    district,
                    flag,
                    grave,
                    name,
                    optradio,
                    religion,
                    secondNam,
                    spotId,
                    star,
                    thirdName,
                    userId,
                    id
            );
        }
    }
}
