package com.remember.app.ui.grid;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.MemoryPageModel;

import java.util.List;

public interface GridView extends MvpView {

    void onReceivedImages(List<MemoryPageModel> memoryPageModel);

}
