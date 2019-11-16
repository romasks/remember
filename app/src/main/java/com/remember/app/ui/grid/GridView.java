package com.remember.app.ui.grid;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.data.models.ResponseUserInfo;

import java.util.List;

public interface GridView extends MvpView {

    void onReceivedImages(ResponsePages responsePages);

    void onSearchedPages(List<MemoryPageModel> memoryPageModels);

    void onReceivedInfo(ResponseUserInfo responseSettings);

    void onError(Throwable throwable);
}
