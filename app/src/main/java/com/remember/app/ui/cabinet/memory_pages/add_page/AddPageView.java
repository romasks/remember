package com.remember.app.ui.cabinet.memory_pages.add_page;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.base.BaseView;

import java.util.List;

public interface AddPageView extends BaseView {

    void onSavedPage(ResponseCemetery response);

    void onGetInfo(List<ResponseHandBook> responseHandBooks);

    void onEdited(MemoryPageModel memoryPageModel);

    void onError(Throwable throwable);

    void onErrorSave(Throwable throwable);
}
