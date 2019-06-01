package com.remember.app.ui.cabinet.epitaphs;

import com.arellomobile.mvp.MvpView;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.ResponseEpitaphs;

import java.util.List;

public interface EpitaphsView extends MvpView {

    void onReceivedEpitaphs(List<ResponseEpitaphs> responseEpitaphs);

    void onSavedEpitaphs(RequestAddEpitaphs requestAddEpitaphs);

    void onErrorSavedEpitaphs(Throwable throwable);
}
