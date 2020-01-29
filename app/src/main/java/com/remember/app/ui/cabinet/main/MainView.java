package com.remember.app.ui.cabinet.main;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseUserInfo;
import com.remember.app.ui.base.BaseView;

import java.util.List;

public interface MainView extends BaseView {

    void onReceivedReligions(List<String> strings);

    void onSearchedLastNames(List<MemoryPageModel> memoryPageModels);

    void onReceivedInfo(ResponseUserInfo responseSettings);

    void onError(Throwable throwable);
}
