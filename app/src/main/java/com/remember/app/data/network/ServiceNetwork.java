package com.remember.app.data.network;

import com.remember.app.data.models.ResponseCities;

import java.util.List;

import io.reactivex.Observable;

public interface ServiceNetwork {

    Observable<List<ResponseCities>> getCities();

}
