package com.remember.app.data.network;


import com.remember.app.data.models.EpitNotificationModel;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventNotificationModel;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.data.models.RequestQuestion;
import com.remember.app.data.models.RequestRegister;
import com.remember.app.data.models.RequestSettings;
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

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiMethods {

    @GET("city")
    Observable<List<ResponseHandBook>> getCities();

//    @GET("numen/city/{id}")
    @GET("numen/{id}")
    Observable<ResponseCemetery> getCemetery(@Path("id") int id);


    @GET("religia")
    Observable<List<ResponseHandBook>> getReligion();

    @Headers("Content-Type: application/json")
    @GET("page/user/{id}")
    Observable<List<MemoryPageModel>> getPages(@Path("id") String id);

    @GET("page/epit/{id}")
    Observable<List<ResponseEpitaphs>> getEpitaphs(@Path("id") int pageId);

    @Headers("Content-Type: application/json")
    @POST("epit/add")
    Observable<RequestAddEpitaphs> saveEpitaph(@Body RequestAddEpitaphs requestAddEpitaphs);

    @Headers("Content-Type: application/json")
    @POST("deadevent/add")
    Observable<RequestAddEvent> saveEvent(@Body RequestAddEvent requestAddEvent);

    @GET("event")
    Observable<List<ResponseEvents>> getEvents();

    @GET("deadevent/{id}")
    Observable<EventModel> getEvent(@Path("id") int id);

    @GET("event/notifications")
    Observable<List<EventNotificationModel>> getEventNotifications(@Header("Authorization") String token, @Query("filter_type") String filterType);

    @GET("epit/notification")
    Observable<List<EpitNotificationModel>> getEpitNotifications(@Header("Authorization") String token);

    @GET("user/login")
    Observable<ResponseAuth> singInAuth(@Query("email") String email,
                                        @Query("password") String password);

    @Headers("Content-Type: application/json")
    @POST("user/create")
    Observable<Response<ResponseRegister>> registerLogin(@Body RequestRegister requestRegister);

    @GET("deadevent/page/{id}")
    Observable<List<RequestAddEvent>> getEventsForId(@Path("id") int pageId);

    @Multipart
    @POST("page/add")
    Observable<ResponseCemetery> addPage(@Part("oblast") RequestBody oblast,
                                         @Part("datarod") RequestBody datarod,
                                         @Part("nazvaklad") RequestBody nazvaklad,
                                         @Part("gorod") RequestBody gorod,
                                         @Part("comment") RequestBody comment,
                                         @Part("coords") RequestBody coords,
                                         @Part("datasmert") RequestBody deathDate,
                                         @Part("rajon") RequestBody district,
                                         @Part("flag") RequestBody flag,
                                         @Part("nummogil") RequestBody graveId,
                                         @Part("name") RequestBody name,
                                         @Part("optradio") RequestBody optradio,
                                         @Part("religiya") RequestBody religion,
                                         @Part("secondname") RequestBody secondName,
                                         @Part("uchastok") RequestBody spotId,
                                         @Part("star") RequestBody star,
                                         @Part("thirtname") RequestBody thirdName,
                                         @Part("user_id") RequestBody userId,
                                         @Part MultipartBody.Part image);


    @GET("pages")
    Observable<ResponsePages> getImages(@Query("status") String status);

    @Multipart
    @POST("page/edit/{id}")

//    Observable<ResponsePages> editPage(@Part("oblast") RequestBody oblast,
//                                       @Part("datarod") RequestBody datarod,
//                                       @Part("nazvaklad") RequestBody nazvaklad,
//                                       @Part("gorod") RequestBody gorod,
//                                       @Part("comment") RequestBody comment,
//                                       @Part("coords") RequestBody coords,
//                                       @Part("datasmert") RequestBody deathDate,
//                                       @Part("rajon") RequestBody district,
//                                       @Part("flag") RequestBody flag,
//                                       @Part("nummogil") RequestBody graveId,
//                                       @Part("name") RequestBody name,
//                                       @Part("optradio") RequestBody optradio,
//                                       @Part("religiya") RequestBody religion,
//                                       @Part("secondname") RequestBody secondName,
//                                       @Part("uchastok") RequestBody spotId,
//                                       @Part("star") RequestBody star,
//                                       @Part("thirtname") RequestBody thirdName,
//                                       @Part("user_id") RequestBody userId,
//                                       @Part MultipartBody.Part image,
//                                       @Path("id") Integer id);
//    Observable<ResponsePages> editPage(@Header("Authorization") String token,
//    Observable<PageEditedResponse> editPage(@Header("Authorization") String token,
    Observable<MemoryPageModel> editPage(@Header("Authorization") String token,
                                         @Part("oblast") RequestBody oblast,
                                         @Part("datarod") RequestBody datarod,
                                         @Part("nazvaklad") RequestBody nazvaklad,
                                         @Part("gorod") RequestBody gorod,
                                         @Part("comment") RequestBody comment,
                                         @Part("coords") RequestBody coords,
                                         @Part("datasmert") RequestBody deathDate,
                                         @Part("rajon") RequestBody district,
                                         @Part("flag") RequestBody flag,
                                         @Part("nummogil") RequestBody graveId,
                                         @Part("name") RequestBody name,
                                         @Part("optradio") RequestBody optradio,
                                         @Part("religiya") RequestBody religion,
                                         @Part("secondname") RequestBody secondName,
                                         @Part("uchastok") RequestBody spotId,
                                         @Part("star") RequestBody star,
                                         @Part("thirtname") RequestBody thirdName,
                                         @Part("user_id") RequestBody userId,
                                         @Part MultipartBody.Part image,
                                         @Path("id") Integer id);


    @GET("poisk/secondname/{second_name}")
    Observable<List<MemoryPageModel>> searchLastName(@Path("second_name") String lastName);

    @Headers("Content-Type: application/json")
    @POST("question/add")
    Observable<Object> send(@Body RequestQuestion requestQuestion);

    @Multipart
    @POST("page/edit/{id}")

//    Observable<ResponsePages> editPageWithoutImage(@Part("oblast") RequestBody oblast,
//                                                   @Part("datarod") RequestBody datarod,
//                                                   @Part("nazvaklad") RequestBody nazvaklad,
//                                                   @Part("gorod") RequestBody gorod,
//                                                   @Part("comment") RequestBody comment,
//                                                   @Part("coords") RequestBody coords,
//                                                   @Part("datasmert") RequestBody deathDate,
//                                                   @Part("rajon") RequestBody district,
//                                                   @Part("flag") RequestBody flag,
//                                                   @Part("nummogil") RequestBody graveId,
//                                                   @Part("name") RequestBody name,
//                                                   @Part("optradio") RequestBody optradio,
//                                                   @Part("religiya") RequestBody religion,
//                                                   @Part("secondname") RequestBody secondName,
//                                                   @Part("uchastok") RequestBody spotId,
//                                                   @Part("star") RequestBody star,
//                                                   @Part("thirtname") RequestBody thirdName,
//                                                   @Part("user_id") RequestBody userId,
//                                                   @Path("id") Integer id);

//    Observable<ResponsePages> editPageWithoutImage(@Header("Authorization") String token,
//    Observable<PageEditedResponse> editPageWithoutImage(@Header("Authorization") String token,
    Observable<MemoryPageModel> editPageWithoutImage(@Header("Authorization") String token,
                                                     @Part("oblast") RequestBody oblast,
                                                     @Part("datarod") RequestBody datarod,
                                                     @Part("nazvaklad") RequestBody nazvaklad,
                                                     @Part("gorod") RequestBody gorod,
                                                     @Part("comment") RequestBody comment,
                                                     @Part("coords") RequestBody coords,
                                                     @Part("datasmert") RequestBody deathDate,
                                                     @Part("rajon") RequestBody district,
                                                     @Part("flag") RequestBody flag,
                                                     @Part("nummogil") RequestBody graveId,
                                                     @Part("name") RequestBody name,
                                                     @Part("optradio") RequestBody optradio,
                                                     @Part("religiya") RequestBody religion,
                                                     @Part("secondname") RequestBody secondName,
                                                     @Part("uchastok") RequestBody spotId,
                                                     @Part("star") RequestBody star,
                                                     @Part("thirtname") RequestBody thirdName,
                                                     @Part("user_id") RequestBody userId,
                                                     @Path("id") Integer id);


    @GET("page/{id}")
    Observable<MemoryPageModel> getImageAfterSave(@Path("id") Integer id);

    @GET("settings")
    Observable<ResponseSettings> getInfo(@Header("Authorization") String token);

    @POST("setting/edit/{id}")
    Observable<Object> saveSettings(@Body RequestSettings requestSettings,
                                    @Path("id") String id);

    @GET("user/social")
    Observable<ResponseSettings> signInVk(@Query("email") String email,
                                                @Query("name") String name);

    @GET("page")
    Observable<List<MemoryPageModel>> getAllPages();

    @GET("/user/restore/{email}")
    Observable<ResponseRestorePassword> restorePassword(@Path("email") String email);

    @Multipart
    @POST("settings/picture")
    Observable<Object> savePhotoSettings(@Part MultipartBody.Part image,
                                         @Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("epit/edit/{id}")
    Observable<RequestAddEpitaphs> editEpitaph(@Body RequestAddEpitaphs requestAddEpitaphs,
                                               @Path("id") Integer id);

    @GET("poisk/page")
    Observable<List<MemoryPageModel>> searchPageAllDead(@Query("name") String name,
                                                        @Query("secondname") String secondName,
                                                        @Query("thirtname") String middleName,
                                                        @Query("datarod") String dateStart,
                                                        @Query("datasmert") String dateEnd,
                                                        @Query("gorod") String city);

    @GET("poisk/event")
    Observable<List<ResponseEvents>> searchEventReligios(@Query("date") String date,
                                                         @Query("religia") String religia);

    @GET("photo/page/{page_id}")
    Observable<List<ResponseImagesSlider>> getAllPhotosForPage(@Path("page_id") int pageId);




    @Multipart
    @POST("photo/add")
    Observable<Object> savePhoto(@Header("Authorization") String token,
                                 @Part("body") String string,
                                 @Part("page_id") Integer id,
                                 @Part MultipartBody.Part image,
                                 @Part MultipartBody.Part imageCut);

}
