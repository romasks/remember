package com.remember.app.ui.cabinet.epitaphs;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class EpitaphsPresenter extends BasePresenter<EpitaphsView> {

    @Inject
    ServiceNetwork serviceNetwork;

    EpitaphsPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getEpitaphs(int pageId) {
        Disposable subscription = serviceNetwork.getEpitaphs(pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedEpitaphs);
        unsubscribeOnDestroy(subscription);
    }

    void saveEpitaph(RequestAddEpitaphs requestAddEpitaphs) {
        Disposable subscription = serviceNetwork.saveEpitaph(requestAddEpitaphs)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedEpitaphs,
                        getViewState()::onErrorSavedEpitaphs);
        unsubscribeOnDestroy(subscription);
    }

    void editEpitaph(RequestAddEpitaphs requestAddEpitaphs, Integer id) {
        Disposable subscription = serviceNetwork.editEpitaph(requestAddEpitaphs, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onEditedEpitaphs,
                        getViewState()::onErrorSavedEpitaphs);
        unsubscribeOnDestroy(subscription);
    }

    void deleteEpitaph(Integer id) {
        Disposable subscription = serviceNetwork.deleteEpitaph(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onDeletedEpitaphs,
                        getViewState()::onErrorDeleteEpitaphs);
        unsubscribeOnDestroy(subscription);
    }
}
