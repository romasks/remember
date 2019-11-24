package com.remember.app.ui.cabinet.main;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseUserInfo;

import java.util.List;

public interface MainView extends MvpView {

    void onReceivedReligions(List<String> strings);

    void onSearchedLastNames(List<MemoryPageModel> memoryPageModels);

    void onReceivedInfo(ResponseUserInfo responseSettings);

    void onError(Throwable throwable);
}
