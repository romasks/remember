package com.remember.app.ui.cabinet.memory_pages.place;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseCities;

import java.util.List;

import io.reactivex.Observable;

public interface PlaceView extends MvpView {

    void onUpdatedCities(List<ResponseCities> responseCities);
}
