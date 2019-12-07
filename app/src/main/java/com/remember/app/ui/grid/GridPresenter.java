package com.remember.app.ui.grid;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.ui.base.BasePresenter;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

@InjectViewState
public class GridPresenter extends BasePresenter<GridView> {

    GridPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public LiveData<PagedList<MemoryPageModel>> getMemoryPageModel() {
        return getServiceNetwork().getImagesRepositoryPagedListConfig().getMemoryPageModels();
    }

    public LiveData<PagedList<MemoryPageModel>> getSearchedMemoryPageModel(RequestSearchPage requestSearchPage) {
        return getServiceNetwork().getSearchedImagesRepositoryPagedListConfig(requestSearchPage).getMemoryPageModels();
    }
}
