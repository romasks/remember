package com.remember.app.ui.menu.page;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponsePages;

import java.util.List;

public interface PageMenuView extends MvpView {

    void error(Throwable throwable);

    void onReceivedPages(ResponsePages responsePages);

    void onSearchedPages(List<MemoryPageModel> memoryPageModels);
}
