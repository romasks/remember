package com.remember.app.ui.grid;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.ResponsePages;

public interface GridView extends MvpView {

    void onReceivedImages(ResponsePages responsePages);

}
