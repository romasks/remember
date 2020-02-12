package com.remember.app.ui.menu.question;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.RequestQuestion;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class QuestionPresenter extends BasePresenter<QuestionView> {

    QuestionPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void send(RequestQuestion requestQuestion) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.send(requestQuestion)
            .subscribe(
                getViewState()::onReceived,
                getViewState()::error
            );
        unsubscribeOnDestroy(subscription);
    }
}
