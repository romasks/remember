package com.remember.app.data.network;


import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.data.models.ResponsePages;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiMethods {

    @GET("city")
    Observable<List<ResponseHandBook>> getCities();

    @GET("numen/city/{id}")
    Observable<List<ResponseCemetery>> getCemetery(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @POST("page/add")
    Observable<ResponseCemetery> addPage(@Body AddPageModel person);

    @GET("religia")
    Observable<List<ResponseHandBook>> getReligion();

    @GET("pages/{page}")
    Observable<ResponsePages> getPages(@Path("page") int countPage);

    @GET("page/epit/{id}")
    Observable<List<ResponseEpitaphs>> getEpitaphs(@Path("id") int pageId);

    @Headers("Content-Type: application/json")
    @POST("epit/add")
    Observable<RequestAddEpitaphs> saveEpitaph(@Body RequestAddEpitaphs requestAddEpitaphs);
}
