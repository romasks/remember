package com.remember.app.ui.cabinet.memory_pages.show_page

import com.arellomobile.mvp.InjectViewState
import com.remember.app.Remember
import com.remember.app.data.models.MemoryPageModel
import com.remember.app.data.models.ResponseImagesSlider
import com.remember.app.ui.base.BasePresenter
import java.io.File
import java.util.*

@InjectViewState
public class ShowPagePresenter : BasePresenter<ShowPageView>() {
    var images: List<ResponseImagesSlider> = ArrayList()
    fun getImageAfterSave(id: Int?) {
        if (isOffline()) return
        val subscription = serviceNetwork.getImageAfterSave(id)
                .subscribe({ memoryPageModel: MemoryPageModel? -> viewState!!.onReceivedImage(memoryPageModel) }) { throwable: Throwable? -> viewState!!.error(throwable) }
        unsubscribeOnDestroy(subscription)
    }

    fun savePhoto(imageFile: File?, string: String?, id: Int?) {
        if (isOffline()) return
        val subscription = serviceNetwork.savePhoto(imageFile, string, id)
                .subscribe({ o: Any? -> viewState!!.onSavedImage(o) }) { throwable: Throwable? -> viewState!!.error(throwable) }
        unsubscribeOnDestroy(subscription)
    }

    fun getImagesSlider(id: Int?) {
        if (isOffline()) return
        val subscription = serviceNetwork.getImagesSlider(id)
                .subscribe({ responseImagesSliders: List<ResponseImagesSlider?>? -> viewState!!.onImagesSlider(responseImagesSliders) }) { throwable: Throwable? -> viewState!!.error(throwable) }
        unsubscribeOnDestroy(subscription)
    }

    fun deleteSliderPhoto(id: Int?) {
        if (isOffline()) return
        val subscription = serviceNetwork.deleteSliderPhoto(id)
                .subscribe({ o: Any? -> viewState!!.onDeleteSliderPhoto(o) }) { throwable: Throwable? -> viewState!!.onDeleteSliderPhotoError(throwable) }
        unsubscribeOnDestroy(subscription)
    }

    init {
        Remember.applicationComponent.inject(this)
    }
}