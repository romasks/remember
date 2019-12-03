package com.remember.app.ui.adapters;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.remember.app.data.models.MemoryPageModel;

import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ImagesRepositoryPagedListConfig {

    private final PagedList.Config config;
    private final ImagesDataFactory dataSourceFactory;
    private LiveData<PagedList<MemoryPageModel>> memoryPageModels;

    public ImagesRepositoryPagedListConfig(ImagesDataFactory imagesDataFactory) {
        this.dataSourceFactory = imagesDataFactory;

        config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(15)
                .setPageSize(15)
                .setPrefetchDistance(9)
                .build();

        memoryPageModels = new LivePagedListBuilder<>(dataSourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

    public LiveData<PagedList<MemoryPageModel>> getMemoryPageModels() {
        return memoryPageModels;
    }
}
