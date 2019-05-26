package com.remember.app.data.network;

import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.Response;

public interface ServiceNetwork {

    Observable<List<ResponseHandBook>> getCities();

    Observable<List<ResponseCemetery>> getCemetery(int id);

    Observable<ResponseCemetery> addPage(AddPageModel person);

    Observable<List<ResponseHandBook>> getReligion();
}
