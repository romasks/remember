package com.remember.app.ui.grid;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.ui.base.BasePresenter;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.remember.app.data.Constants.IMAGES_STATUS_APPROVED;

@InjectViewState
public class GridPresenter extends BasePresenter<GridView> {

    GridPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getImages(int count) {
        Disposable subscription = getServiceNetwork().getImages(count, true, true, IMAGES_STATUS_APPROVED)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onReceivedImages,
                        getViewState()::onError);
        unsubscribeOnDestroy(subscription);
    }

    public LiveData<PagedList<MemoryPageModel>> getMemoryPageModel() {
        return getServiceNetwork().getImagesRepositoryPagedListConfig().getMemoryPageModels();
    }

    public LiveData<PagedList<MemoryPageModel>> getSearchedMemoryPageModel(RequestSearchPage requestSearchPage) {
        return getServiceNetwork().getSearchedImagesRepositoryPagedListConfig(requestSearchPage).getMemoryPageModels();
    }
}
