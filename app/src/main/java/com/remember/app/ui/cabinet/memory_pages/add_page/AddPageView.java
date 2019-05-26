package com.remember.app.ui.cabinet.memory_pages.add_page;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.Response;

public interface AddPageView extends MvpView {

    void onSavedPage(ResponseCemetery response);

    void onGetedInfo(List<ResponseHandBook> responseHandBooks);
}
