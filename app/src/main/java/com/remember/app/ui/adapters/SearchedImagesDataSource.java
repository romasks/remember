package com.remember.app.ui.adapters;

import android.annotation.SuppressLint;
import android.util.Log;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.network.ServiceNetwork;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class SearchedImagesDataSource extends PageKeyedDataSource<Integer, MemoryPageModel> {

    private final ServiceNetwork serviceNetwork;
    private final RequestSearchPage requestSearchPage;

    @Inject
    SearchedImagesDataSource(ServiceNetwork serviceNetwork, RequestSearchPage requestSearchPage) {
        this.serviceNetwork = serviceNetwork;
        this.requestSearchPage = requestSearchPage;
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, MemoryPageModel> callback) {
        serviceNetwork.searchPageAllDead(requestSearchPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchedMemoryPages -> {
                    callback.onResult(searchedMemoryPages, null, 2);
                }, throwable -> {
                    Log.e("ImagesDataSource", throwable.getMessage());
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MemoryPageModel> callback) {

    }

    @SuppressLint("CheckResult")
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MemoryPageModel> callback) {
        serviceNetwork.searchPageAllDead(requestSearchPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchedMemoryPages -> {
                    callback.onResult(searchedMemoryPages, params.key + 1);
                }, throwable -> {
                    Log.e("ImagesDataSource", throwable.getMessage());
                });
    }
}
