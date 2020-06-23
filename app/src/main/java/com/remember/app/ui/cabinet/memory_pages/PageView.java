package com.remember.app.ui.cabinet.memory_pages;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BaseView;

import java.util.LinkedList;
import java.util.List;

public interface PageView extends BaseView {

    void onReceivedPages(LinkedList<MemoryPageModel> memoryPageModels);
}
