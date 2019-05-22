package com.remember.app.data.network;

import com.remember.app.data.models.ResponseHandBook;

import java.util.List;

import io.reactivex.Observable;

public interface ServiceNetwork {

    Observable<List<ResponseHandBook>> getCities();

}
