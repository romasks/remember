package com.remember.app.ui.adapters;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.network.ServiceNetwork;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

@Singleton
public class SearchedImagesDataFactory extends DataSource.Factory<Integer, MemoryPageModel> {

    private final ServiceNetwork serviceNetwork;
    private final RequestSearchPage requestSearchPage;

    private MutableLiveData<SearchedImagesDataSource> sourceLiveData = new MutableLiveData<>();
    private SearchedImagesDataSource latestSource = null;

    @Inject
    public SearchedImagesDataFactory(ServiceNetwork serviceNetwork, RequestSearchPage requestSearchPage) {
        this.serviceNetwork = serviceNetwork;
        this.requestSearchPage = requestSearchPage;
    }

    @NonNull
    @Override
    public DataSource<Integer, MemoryPageModel> create() {
        latestSource = new SearchedImagesDataSource(serviceNetwork, requestSearchPage);
        sourceLiveData.postValue(latestSource);
        return latestSource;
    }
}
