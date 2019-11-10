package com.remember.app.ui.cabinet.memory_pages.place;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;

import java.util.List;

public interface PlaceView extends MvpView {

    void onUpdatedCities(List<ResponseHandBook> responseCities);

    void onUpdatedCemetery(List<ResponseCemetery> responseCemeteries);
}
