package com.remember.app.ui.cabinet.memory_pages.place;

import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BaseView;

import java.util.List;

public interface PlaceView extends BaseView {

    void onUpdatedCities(List<ResponseHandBook> responseCities);

    void onUpdatedCemetery(List<ResponseCemetery> responseCemeteries);
}
