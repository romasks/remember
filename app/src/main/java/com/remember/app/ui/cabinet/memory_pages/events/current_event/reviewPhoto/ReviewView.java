package com.remember.app.ui.cabinet.memory_pages.events.current_event.reviewPhoto;

import com.remember.app.ui.base.BaseView;

public interface ReviewView extends BaseView {
    void onDeleteSliderPhoto(Object o);
    void onDeleteSliderPhotoError(Throwable throwable);


}
