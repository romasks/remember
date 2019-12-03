package com.remember.app.ui.splash;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.ui.base.BasePresenter;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

//    private ImagesRepositoryPagedListConfig imagesRepositoryConfig;

    SplashPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

//    void setImagesRepositoryConfig(ImagesRepositoryPagedListConfig imagesRepositoryConfig) {
//        this.imagesRepositoryConfig = imagesRepositoryConfig;
//    }

    /*void loadImages() {
        imagesRepositoryConfig.getMemoryPageModels().observeForever(this);
//        getServiceNetwork().getImages(1, true, true, IMAGES_STATUS_APPROVED)
    }

    @Override
    public void onChanged(@Nullable PagedList<MemoryPageModel> memoryPageModels) {
        if (memoryPageModels == null) {
            return;
        }
        Log.d("SplashPresenter", "SUCCESS");
    }*/

//    public LiveData<PagedList<MemoryPageModel>> getMemoryPageModel() {
//        return imagesRepositoryConfig.getMemoryPageModels();
//    }
}
