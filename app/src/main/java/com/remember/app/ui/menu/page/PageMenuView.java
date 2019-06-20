package com.remember.app.ui.menu.page;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.MemoryPageModel;

import java.util.List;

public interface PageMenuView extends MvpView {

    void onReceivedPages(List<MemoryPageModel> memoryPageModels);

    void error(Throwable throwable);
}
