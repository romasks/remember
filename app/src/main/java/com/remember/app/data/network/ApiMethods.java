package com.remember.app.data.network;


import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiMethods {

    @GET("city")
    Observable<List<ResponseHandBook>> getCities();

    @GET("numen/city/{id}")
    Observable<List<ResponseCemetery>> getCemetery(@Path("id") int id);
}
