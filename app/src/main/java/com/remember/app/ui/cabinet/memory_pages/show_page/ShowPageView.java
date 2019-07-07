package com.remember.app.ui.cabinet.memory_pages.show_page;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.MemoryPageModel;

public interface ShowPageView extends MvpView {

    void onReceivedImage(MemoryPageModel memoryPageModel);

    void error(Throwable throwable);

    void onSavedImage(Object o);
}
