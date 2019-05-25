package com.remember.app.ui.cabinet.memory_pages;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.MemoryPageModel;

import java.util.List;

import retrofit2.Response;

public interface PageView extends MvpView {

    void getPages(List<MemoryPageModel> memoryPageModelList);

}
