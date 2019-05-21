package com.remember.app.data.network;


import com.remember.app.data.models.ResponseCities;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiMethods {

    @GET("city")
    Observable<List<ResponseCities>> getCities();
}
