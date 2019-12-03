package com.remember.app.ui.adapters;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.network.ServiceNetwork;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.remember.app.data.Constants.IMAGES_STATUS_APPROVED;

public class ImagesDataSource extends PageKeyedDataSource<Integer, MemoryPageModel> {

    private ServiceNetwork serviceNetwork;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ImagesDataSource(ServiceNetwork serviceNetwork) {
        this.serviceNetwork = serviceNetwork;
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, MemoryPageModel> callback) {
        Disposable disposable = serviceNetwork.getImages(1, true, true, IMAGES_STATUS_APPROVED)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responsePages -> {
                    callback.onResult(responsePages.getResult(), 1, 2);
                }, throwable -> {
                    Log.e("ImagesDataSource", throwable.getMessage());
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MemoryPageModel> callback) {

    }

    @SuppressLint("CheckResult")
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MemoryPageModel> callback) {
        Disposable disposable = serviceNetwork.getImages(params.key, true, true, IMAGES_STATUS_APPROVED)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responsePages -> {
                    callback.onResult(responsePages.getResult(), params.key + 1);
                }, throwable -> {
                    Log.e("ImagesDataSource", throwable.getMessage());
                });
        compositeDisposable.add(disposable);
    }
}
