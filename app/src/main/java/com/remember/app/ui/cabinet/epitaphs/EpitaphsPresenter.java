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

    public EpitaphsPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getEpitaphs(int pageId) {
        Disposable subscription = serviceNetwork.getEpitaphs(pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedEpitaphs);
        unsubscribeOnDestroy(subscription);
    }

    public void saveEpitaph(RequestAddEpitaphs requestAddEpitaphs) {
        Disposable subscription = serviceNetwork.saveEpitaph(requestAddEpitaphs)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onSavedEpitaphs,
                        getViewState()::onErrorSavedEpitaphs);
        unsubscribeOnDestroy(subscription);
    }
}
