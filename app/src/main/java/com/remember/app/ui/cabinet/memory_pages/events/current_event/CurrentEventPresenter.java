package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.AddComment;
import com.remember.app.data.models.AddVideo;
import com.remember.app.ui.base.BasePresenter;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

@InjectViewState
public class CurrentEventPresenter extends BasePresenter<CurrentEventView> {

    CurrentEventPresenter() {
        Remember.applicationComponent.inject(this);
    }

    void getDeadEvent(int id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getDeadEvent(id)
            .subscribe(getViewState()::onReceivedEvent);
        unsubscribeOnDestroy(subscription);
    }

    void getComments(int id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getEventComments(id)
                .subscribe(getViewState()::onReceivedComments);
        unsubscribeOnDestroy(subscription);
    }

    void getPhotos(int id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getEventPhoto(id)
                .subscribe(getViewState()::onReceivedPhotos);
        unsubscribeOnDestroy(subscription);
    }

    void getVideos(int id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getEventVideo(id)
                .subscribe(getViewState()::onReceivedVideos);
        unsubscribeOnDestroy(subscription);
    }

    void addVideo(int id, AddVideo body){
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.addVideo(id, body)
                .subscribe(getViewState()::onVideoAdded, getViewState()::onVideoAddedError);
        unsubscribeOnDestroy(subscription);
    }


    void addComment(int id, AddComment body){
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.addComment(id, body)
                .subscribe(getViewState()::onCommentAdded, getViewState()::onCommentAddedError);
        unsubscribeOnDestroy(subscription);
    }
}
