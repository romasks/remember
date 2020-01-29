package com.remember.app.ui.grid;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.ui.base.BaseView;

import java.util.List;

public interface GridView extends BaseView {
    void onReceivedImages(ResponsePages responsePages);

    void onSearchedPages(List<MemoryPageModel> memoryPageModels);

    void onError(Throwable throwable);
}
