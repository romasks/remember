package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.AddComment;
import com.remember.app.data.models.AddVideo;
import com.remember.app.data.models.DeleteVideo;
import com.remember.app.data.models.EventComments;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventSliderPhotos;
import com.remember.app.data.models.EventVideos;
import com.remember.app.ui.base.BasePresenter;

import java.io.File;
import java.util.ArrayList;

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

    void addComment(int id, AddComment body){
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.addComment(id, body)
                .subscribe(getViewState()::onCommentAdded, getViewState()::onCommentAddedError);
        unsubscribeOnDestroy(subscription);
    }


    void editComment(int id, int commentID, AddComment body){
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.editComment(id,commentID, body)
                .subscribe(getViewState()::onCommentEdit, getViewState()::onCommentAddedError);
        unsubscribeOnDestroy(subscription);
    }

    void deleteComment(int id, int commentID){
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.deleteComment(id, commentID)
                .subscribe(getViewState()::onCommentDelete, getViewState()::onCommentAddedError);
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

    void deleteVideo(int id, DeleteVideo link){
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.deleteVideo(id, link)
                .subscribe(getViewState()::onVideoDelete, getViewState()::onVideoDeleteError);
        unsubscribeOnDestroy(subscription);
    }

    void getPhotos(int id) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getEventPhoto(id)
                .subscribe(getViewState()::onReceivedPhotos);
        unsubscribeOnDestroy(subscription);
    }

    void addPhotos(int id, String description, File img) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.addEventPhoto(id, description, img)
                .subscribe(getViewState()::onPhotoAdded, getViewState()::onPhotoAddedError);
        unsubscribeOnDestroy(subscription);
    }

    private int currentVideoPosition = 0;

    void saveAdapterPosition(int position){
        currentVideoPosition = position;
    }

    int getAdapterPosition(){
       return currentVideoPosition;
    }

    private ArrayList<EventVideos> videoList = new ArrayList();
    void saveVideoList(ArrayList<EventVideos> list){
        videoList = list;
    }
    ArrayList<EventVideos> getVideoList(){
       return videoList;
    }

    private EventModel requestEvent;
    void saveUseData(EventModel data){
        requestEvent = data;
    }
    EventModel getUserData(){
        return requestEvent;
    }

    private ArrayList<EventSliderPhotos> photoList = new ArrayList();
    void savePhotoList(ArrayList<EventSliderPhotos> list){
        photoList = list;
    }
    ArrayList<EventSliderPhotos> getPhotoList(){
        return photoList;
    }

    private ArrayList<EventComments> commentsList = new ArrayList();
    void saveCommentsList(ArrayList<EventComments> list){
        commentsList = list;
    }
    ArrayList<EventComments> getCommentsList(){
       return commentsList;
    }

    private int lastPositionInScrollView = 0;

    void saveScrollViewPosition(int position){
        lastPositionInScrollView = position;
    }

    int getScrollViewPosition(){
        return lastPositionInScrollView;
    }

}
