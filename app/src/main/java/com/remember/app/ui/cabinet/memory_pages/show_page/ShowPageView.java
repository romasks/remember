package com.remember.app.ui.cabinet.memory_pages.show_page;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseImagesSlider;

import java.util.List;

public interface ShowPageView extends MvpView {

    void onReceivedImage(MemoryPageModel memoryPageModel);

    void error(Throwable throwable);

    void onSavedImage(Object o);

    void onImagesSlider(List<ResponseImagesSlider> responseImagesSliders);
}
