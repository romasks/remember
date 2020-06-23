package com.remember.app.ui.menu.page;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.ui.base.BaseView;

import java.util.LinkedList;
import java.util.List;

public interface PageMenuView extends BaseView {

    void error(Throwable throwable);

    void onReceivedPages(ResponsePages responsePages);

    void onSearchedPages(LinkedList<MemoryPageModel> memoryPageModels);
}
