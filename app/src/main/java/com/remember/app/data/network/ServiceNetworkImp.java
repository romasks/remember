package com.remember.app.data.network;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.data.models.RequestAuth;
import com.remember.app.data.models.RequestQuestion;
import com.remember.app.data.models.RequestRegister;
import com.remember.app.data.models.RequestSettings;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.data.models.ResponseRegister;
import com.remember.app.data.models.ResponseSettings;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

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
    public Observable<ResponseCemetery> addPage(AddPageModel person, File imageFile) {
        RequestBody area = null;
        RequestBody birthDate = null;
        RequestBody cemeteryName = null;
        RequestBody city = null;
        RequestBody comment = null;
        RequestBody coords = null;
        RequestBody deathDate = null;
        RequestBody district = null;
        RequestBody flag = null;
        RequestBody grave = null;
        RequestBody name = null;
        RequestBody optradio = null;
        RequestBody religion = null;
        RequestBody secondNam = null;
        RequestBody spotId = null;
        RequestBody star = null;
        RequestBody thirdName = null;
        RequestBody userId = null;
        MultipartBody.Part fileToUploadTranser = null;
        try {
            area = RequestBody.create(MultipartBody.FORM, person.getArea());
        } catch (Exception e) {
            area = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            birthDate = RequestBody.create(MultipartBody.FORM, person.getBirthDate());
        } catch (Exception e) {
            birthDate = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            cemeteryName = RequestBody.create(MultipartBody.FORM, person.getCemeteryName());
        } catch (Exception e) {
            cemeteryName = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            city = RequestBody.create(MultipartBody.FORM, person.getCity());
        } catch (Exception e) {
            city = RequestBody.create(MultipartBody.FORM,"");
        }
        try {
            comment = RequestBody.create(MultipartBody.FORM, person.getComment());
        } catch (Exception e) {
            comment = RequestBody.create(MultipartBody.FORM,"");
        }
        try {
            coords = RequestBody.create(MultipartBody.FORM, person.getCoords());
        } catch (Exception e) {
            coords = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            deathDate = RequestBody.create(MultipartBody.FORM, person.getDeathDate());
        } catch (Exception e) {
            deathDate = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            district = RequestBody.create(MultipartBody.FORM, person.getDistrict());
        } catch (Exception e) {
            district = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            flag = RequestBody.create(MultipartBody.FORM, person.getFlag());
        } catch (Exception e) {
            flag = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            grave = RequestBody.create(MultipartBody.FORM, person.getGraveId());
        } catch (Exception e) {
            grave = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            name = RequestBody.create(MultipartBody.FORM, person.getName());
        } catch (Exception e) {
            name = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            optradio = RequestBody.create(MultipartBody.FORM, person.getOptradio());
        } catch (Exception e) {
            optradio = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            religion = RequestBody.create(MultipartBody.FORM, person.getReligion());
        } catch (Exception e) {
            religion = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            secondNam = RequestBody.create(MultipartBody.FORM, person.getSecondName());
        } catch (Exception e) {
            secondNam = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            spotId = RequestBody.create(MultipartBody.FORM, person.getSpotId());
        } catch (Exception e) {
            spotId = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            star = RequestBody.create(MultipartBody.FORM, person.getStar());
        } catch (Exception e) {
            star = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            userId = RequestBody.create(MultipartBody.FORM, person.getUserId());
        } catch (Exception e) {
            userId = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            thirdName = RequestBody.create(MultipartBody.FORM, person.getThirdName());
        } catch (Exception e) {
            thirdName = RequestBody.create(MultipartBody.FORM, "");
        }
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        fileToUploadTranser = MultipartBody.Part.createFormData("picture_data", imageFile.getName(), mFile);
        return apiMethods.addPage(area,
                birthDate,
                cemeteryName,
                city,
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
                fileToUploadTranser);
    }

    @Override
    public Observable<List<ResponseHandBook>> getReligion() {
        return apiMethods.getReligion();
    }

    @Override
    public Observable<List<MemoryPageModel>> getPages() {
        return apiMethods.getPages(Prefs.getString("USER_ID", "0"));
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
    public Observable<EventModel> getEvent(int id){return apiMethods.getEvent(id);}

    @Override
    public Observable<ResponseAuth> singInAuth(String email, String password) {
        RequestAuth requestAuth = new RequestAuth();
        requestAuth.setEmail(email);
        requestAuth.setPassword(password);
        return apiMethods.singInAuth(requestAuth);
    }

    @Override
    public Observable<Response<ResponseRegister>> registerLogin(String nickName, String email) {
        RequestRegister requestRegister = new RequestRegister();
        requestRegister.setEmail(email);
        requestRegister.setName(nickName);
        return apiMethods.registerLogin(requestRegister);
    }

    @Override
    public Observable<List<MemoryPageModel>> getImages() {
        return apiMethods.getImages();
    }

    @Override
    public Observable<ResponsePages> editPage(AddPageModel person, Integer id, File imageFile) {
        RequestBody area = null;
        RequestBody birthDate = null;
        RequestBody cemeteryName = null;
        RequestBody city = null;
        RequestBody comment = null;
        RequestBody coords = null;
        RequestBody deathDate = null;
        RequestBody district = null;
        RequestBody flag = null;
        RequestBody grave = null;
        RequestBody name = null;
        RequestBody optradio = null;
        RequestBody religion = null;
        RequestBody secondNam = null;
        RequestBody spotId = null;
        RequestBody star = null;
        RequestBody thirdName = null;
        RequestBody userId = null;
        RequestBody mFile = null;
        MultipartBody.Part fileToUploadTranser = null;
        try {
            area = RequestBody.create(MultipartBody.FORM, person.getArea());
        } catch (Exception e) {
            area = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            birthDate = RequestBody.create(MultipartBody.FORM, person.getBirthDate());
        } catch (Exception e) {
            birthDate = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            cemeteryName = RequestBody.create(MultipartBody.FORM, person.getCemeteryName());
        } catch (Exception e) {
            cemeteryName = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            city = RequestBody.create(MultipartBody.FORM, person.getCity());
        } catch (Exception e) {
            city = RequestBody.create(MultipartBody.FORM,"");
        }
        try {
            comment = RequestBody.create(MultipartBody.FORM, person.getComment());
        } catch (Exception e) {
            comment = RequestBody.create(MultipartBody.FORM,"");
        }
        try {
            coords = RequestBody.create(MultipartBody.FORM, person.getCoords());
        } catch (Exception e) {
            coords = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            deathDate = RequestBody.create(MultipartBody.FORM, person.getDeathDate());
        } catch (Exception e) {
            deathDate = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            district = RequestBody.create(MultipartBody.FORM, person.getDistrict());
        } catch (Exception e) {
            district = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            flag = RequestBody.create(MultipartBody.FORM, person.getFlag());
        } catch (Exception e) {
            flag = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            grave = RequestBody.create(MultipartBody.FORM, person.getGraveId());
        } catch (Exception e) {
            grave = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            name = RequestBody.create(MultipartBody.FORM, person.getName());
        } catch (Exception e) {
            name = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            optradio = RequestBody.create(MultipartBody.FORM, person.getOptradio());
        } catch (Exception e) {
            optradio = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            religion = RequestBody.create(MultipartBody.FORM, person.getReligion());
        } catch (Exception e) {
            religion = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            secondNam = RequestBody.create(MultipartBody.FORM, person.getSecondName());
        } catch (Exception e) {
            secondNam = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            spotId = RequestBody.create(MultipartBody.FORM, person.getSpotId());
        } catch (Exception e) {
            spotId = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            star = RequestBody.create(MultipartBody.FORM, person.getStar());
        } catch (Exception e) {
            star = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            thirdName = RequestBody.create(MultipartBody.FORM, person.getThirdName());
        } catch (Exception e) {
            thirdName = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            userId = RequestBody.create(MultipartBody.FORM, person.getUserId());
        } catch (Exception e) {
            userId = RequestBody.create(MultipartBody.FORM, "");
        }
        try {
            userId = RequestBody.create(MultipartBody.FORM, person.getUserId());
        } catch (Exception e) {
            userId = RequestBody.create(MultipartBody.FORM, "");
        }
        if (imageFile != null){
            mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            fileToUploadTranser = MultipartBody.Part.createFormData("picture_data", imageFile.getName(), mFile);
            return apiMethods.editPage(area,
                    birthDate,
                    cemeteryName,
                    city,
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
                    fileToUploadTranser,
                    id

            );
        } else {
            return apiMethods.editPageWithoutImage(area,
                    birthDate,
                    cemeteryName,
                    city,
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
    public Observable<ResponseSettings> getInfo(String id) {
        return apiMethods.getInfo(id);
    }

    @Override
    public Observable<Object> saveSettings(RequestSettings requestSettings, String id) {
        return apiMethods.saveSettings(requestSettings, id);
    }

    @Override
    public  Observable<List<ResponseSettings>> signInVk(String email) {
        String name = Prefs.getString("USER_NAME","");
        return apiMethods.signInVk(email, name);
    }

    @Override
    public Observable<List<MemoryPageModel>> getAllPages() {
        return apiMethods.getAllPages();
    }

    @Override
    public Observable<Object> saveImageSetting(File imageFile) {
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part fileToUploadTranser = MultipartBody.Part.createFormData("picture", imageFile.getName(), mFile);
        return apiMethods.savePhotoSettings(fileToUploadTranser, Integer.parseInt(Prefs.getString("USER_ID", "0")));
    }

    @Override
    public Observable<RequestAddEpitaphs> editEpitaph(RequestAddEpitaphs requestAddEpitaphs, Integer id) {
        return apiMethods.editEpitaph(requestAddEpitaphs, id);
    }
}
