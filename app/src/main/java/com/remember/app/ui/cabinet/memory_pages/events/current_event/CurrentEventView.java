package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import com.remember.app.data.models.EventComments;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventSliderPhotos;
import com.remember.app.data.models.EventVideos;
import com.remember.app.ui.base.BaseView;

import java.util.ArrayList;

public interface CurrentEventView extends BaseView {

    void onReceivedEvent(EventModel requestEvent);

    void onReceivedComments(ArrayList<EventComments> requestEvent);
    void onCommentAdded(Object o);
    void onCommentAddedError(Throwable throwable);

    void onReceivedVideos(ArrayList<EventVideos> requestEvent);
    void onVideoAdded(Object o);
    void onVideoAddedError(Throwable throwable);

    void onReceivedPhotos(ArrayList<EventSliderPhotos> requestEvent);
    void onPhotoAdded(Object o);
    void onPhotoAddedError(Throwable throwable);

}
