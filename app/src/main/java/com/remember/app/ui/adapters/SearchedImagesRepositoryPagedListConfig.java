package com.remember.app.ui.adapters;

import com.remember.app.data.models.MemoryPageModel;

import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

@Singleton
public class SearchedImagesRepositoryPagedListConfig {

    private final PagedList.Config config;
    private final SearchedImagesDataFactory dataSourceFactory;
    private LiveData<PagedList<MemoryPageModel>> memoryPageModels;

    @Inject
    public SearchedImagesRepositoryPagedListConfig(SearchedImagesDataFactory imagesDataFactory) {
        this.dataSourceFactory = imagesDataFactory;

        config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(100)
                .setPageSize(100)
                .build();

        memoryPageModels = new LivePagedListBuilder<>(dataSourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

    public LiveData<PagedList<MemoryPageModel>> getMemoryPageModels() {
        return memoryPageModels;
    }
}
