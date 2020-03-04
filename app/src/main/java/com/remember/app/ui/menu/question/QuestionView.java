package com.remember.app.ui.menu.question;

import com.remember.app.ui.base.BaseView;

public interface QuestionView extends BaseView {

    void onReceived(Object o);

    void error(Throwable throwable);
}
