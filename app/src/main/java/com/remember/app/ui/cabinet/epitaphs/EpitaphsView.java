package com.remember.app.ui.cabinet.epitaphs;

import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.ui.base.BaseView;

import java.util.List;

public interface EpitaphsView extends BaseView {

    void onReceivedEpitaphs(List<ResponseEpitaphs> responseEpitaphs);

    void onSavedEpitaphs(RequestAddEpitaphs requestAddEpitaphs);

    void onErrorSavedEpitaphs(Throwable throwable);

    void onEditedEpitaphs(RequestAddEpitaphs requestAddEpitaphs);

    void onDeletedEpitaphs(Object o);

    void onErrorDeleteEpitaphs(Throwable throwable);

    void onErrorGetEpitaphs(Throwable throwable);
}
