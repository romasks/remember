package com.remember.app.data.network;


import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.MemoryPageModel;
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

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiMethods {

    @GET("city")
    Observable<List<ResponseHandBook>> getCities();

    @GET("numen/city/{id}")
    Observable<List<ResponseCemetery>> getCemetery(@Path("id") int id);


    @GET("religia")
    Observable<List<ResponseHandBook>> getReligion();

    @GET("pages/{page}")
    Observable<ResponsePages> getPages(@Path("page") int countPage);

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

    @Headers("Content-Type: application/json")
    @POST("user/add")
    Observable<ResponseAuth> singInAuth(@Body RequestAuth requestAuth);

    @Headers("Content-Type: application/json")
    @POST("user")
    Observable<ResponseRegister> registerLogin(@Body RequestRegister requestRegister);

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


    @GET("page")
    Observable<List<MemoryPageModel>> getImages();

    @Multipart
    @POST("page/edit/{id}")
    Observable<ResponsePages> editPage(@Part("oblast") RequestBody oblast,
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
}
