package com.remember.app.ui.splash;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

    SplashPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void initLoadImages() {
        getServiceNetwork()
                .getImagesRepositoryPagedListConfig()
                .getMemoryPageModels()
                .observeForever(pagedList -> {
                    Log.d("SplashPresenter", "Только для создания PagedList");
                    Log.d("SplashPresenter", "Так как он создаётся только тогда, когда LiveData становится наблюдаемой");
                    Log.d("SplashPresenter", "LivePagedListBuilder -> compute -> the creation of the first PagedList is deferred until the LiveData is observed");
                });
    }

}
