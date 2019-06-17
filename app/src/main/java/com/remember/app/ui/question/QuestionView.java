package com.remember.app.ui.question;

import com.arellomobile.mvp.MvpView;

public interface QuestionView extends MvpView {
    
    void onReceived(Object o);

    void error(Throwable throwable);
}
