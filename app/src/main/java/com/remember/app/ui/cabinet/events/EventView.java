package com.remember.app.ui.cabinet.events;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.MemoryPageModel;

import java.util.List;

public interface EventView extends MvpView {

    void getEvents(List<MemoryPageModel> memoryPageModelList);

}
