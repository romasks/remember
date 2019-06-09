package com.remember.app.ui.settings.data;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

@InjectViewState
public class PersonalDataFragmentPresenter extends BasePresenter<PersonalDataFragmentView> {

    public PersonalDataFragmentPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getPages(int countPage) {
//        Disposable subscription = getServiceNetwork().getPages(countPage)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(getViewState()::onReceivedPages);
//        unsubscribeOnDestroy(subscription);
    }
}
