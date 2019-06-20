package com.remember.app.ui.menu.question;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestQuestion;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class QuestionPresenter extends BasePresenter<QuestionView> {


    @Inject
    ServiceNetwork serviceNetwork;

    public QuestionPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void send(RequestQuestion requestQuestion) {
        Disposable subscription = serviceNetwork.send(requestQuestion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceived,
                        getViewState()::error);
        unsubscribeOnDestroy(subscription);
    }
}
