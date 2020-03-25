package com.remember.app.ui.cabinet.memory_pages.show_page;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseImagesSlider;
import com.remember.app.ui.base.BaseView;

import java.util.List;

public interface ShowPageView extends BaseView {

    void onReceivedImage(MemoryPageModel memoryPageModel);

    void error(Throwable throwable);

    void onSavedImage(Object o);

    void onImagesSlider(List<ResponseImagesSlider> responseImagesSliders);

    void onDeleteSliderPhoto(Object o);

    void onDeleteSliderPhotoError(Throwable throwable);
}
