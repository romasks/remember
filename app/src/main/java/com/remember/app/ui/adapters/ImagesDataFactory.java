package com.remember.app.ui.adapters;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.network.ServiceNetwork;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ImagesDataFactory extends DataSource.Factory<Integer, MemoryPageModel> {

    private final ServiceNetwork serviceNetwork;

    private MutableLiveData<ImagesDataSource> sourceLiveData = new MutableLiveData<>();
    private ImagesDataSource latestSource = null;

    @Inject
    public ImagesDataFactory(ServiceNetwork serviceNetwork) {
        this.serviceNetwork = serviceNetwork;
    }

    @NonNull
    @Override
    public DataSource<Integer, MemoryPageModel> create() {
        latestSource = new ImagesDataSource(serviceNetwork);
        sourceLiveData.postValue(latestSource);
        return latestSource;
    }
}
