package com.remember.app.ui.cabinet.main;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.data.models.ResponsePages;

import java.util.List;

public interface MainView extends MvpView {

    void onReceivedReligions(List<String> strings);

    void onSearchedLastNames(List<MemoryPageModel> memoryPageModels);
}
