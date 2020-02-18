package com.remember.app.ui.cabinet.epitaphs;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class EpitaphsPresenter extends BasePresenter<EpitaphsView> {

    EpitaphsPresenter() {
        Remember.applicationComponent.inject(this);
    }

    void getEpitaphs(int pageId) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getEpitaphs(pageId)
            .subscribe(
                getViewState()::onReceivedEpitaphs,
                getViewState()::onErrorGetEpitaphs
            );
        unsubscribeOnDestroy(subscription);
    }

    void saveEpitaph(RequestAddEpitaphs requestAddEpitaphs) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.saveEpitaph(requestAddEpitaphs)
            .subscribe(
                getViewState()::onSavedEpitaphs,
                getViewState()::onErrorSavedEpitaphs
            );
        unsubscribeOnDestroy(subscription);
    }

    void editEpitaph(RequestAddEpitaphs requestAddEpitaphs, Integer id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.editEpitaph(requestAddEpitaphs, id)
            .subscribe(
                getViewState()::onEditedEpitaphs,
                getViewState()::onErrorSavedEpitaphs
            );
        unsubscribeOnDestroy(subscription);
    }

    void deleteEpitaph(Integer id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.deleteEpitaph(id)
            .subscribe(
                getViewState()::onDeletedEpitaphs,
                getViewState()::onErrorDeleteEpitaphs
            );
        unsubscribeOnDestroy(subscription);
    }
}
