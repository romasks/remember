package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.gson.JsonObject;
import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.AddComment;
import com.remember.app.data.models.AddVideo;
import com.remember.app.data.models.EventComments;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventSliderPhotos;
import com.remember.app.data.models.EventVideos;
import com.remember.app.ui.adapters.EventStuffAdapter;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.events.add_new_event.AddNewEventActivity;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters.CommentsAdapter;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters.PhotoAdapter;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters.VideoAdapter;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.commentDialog.CommentDialog;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.videoDialog.VideoDialog;
import com.remember.app.ui.utils.DateUtils;
import com.remember.app.ui.utils.PhotoDialog;
import com.remember.app.ui.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.BIRTH_DATE;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ACCESS;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_DATE;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_DESCRIPTION;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_IMAGE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_IS_FOR_ONE;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_NAME;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_PERSON;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_EVENT_EDITING;
import static com.remember.app.data.Constants.INTENT_EXTRA_PAGE_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON_NAME;
import static com.remember.app.data.Constants.INTENT_EXTRA_SHOW;
import static com.remember.app.ui.utils.FileUtils.storagePermissionGranted;
import static com.remember.app.ui.utils.FileUtils.verifyStoragePermissions;
import static com.remember.app.ui.utils.ImageUtils.cropImage;
import static com.remember.app.ui.utils.ImageUtils.glideLoadInto;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoAsBitmap;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoWithError;
import static com.remember.app.ui.utils.StringUtils.getVideoIdFromUrl;

public class CurrentEvent extends BaseActivity implements CurrentEventView, CommentsAdapter.CommentsAdapterListener, VideoAdapter.VideoAdapterListener,PhotoDialog.Callback {

    @InjectPresenter
    CurrentEventPresenter presenter;

    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.settings)
    ImageView settings;
    @BindView(R.id.event_name)
    CustomTextView eventName;
    @BindView(R.id.pageAvatar)
    FrameLayout pageAvatar;
    @BindView(R.id.image_avatar)
    ImageView imageAvatar;
    @BindView(R.id.photos)
    RecyclerView photosView;
    @BindView(R.id.add_photo)
    ImageView addPhoto;
    @BindView(R.id.date)
    CustomTextView dateView;
    @BindView(R.id.description)
    CustomTextView description;
    @BindView(R.id.videos)
    RecyclerView videos;
    @BindView(R.id.add_video)
    ImageView addVideo;
    @BindView(R.id.rvComments)
    RecyclerView comments;
    private PhotoDialog photoDialog;

    private Integer eventId = 0;
    private String personName;
    private String imageUrl = "";
    private int pageId = 0;
    private boolean isShow;
    private EventModel eventModel;
    static CurrentEvent activity;
    CommentsAdapter commentsAdapter;
    VideoAdapter videoAdapter;
    PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        activity = this;
        if (Utils.isThemeDark()) {
            back.setImageResource(R.drawable.ic_back_dark_theme);
            settings.setImageResource(R.drawable.setting_white);
        }

        eventId = getIntent().getIntExtra(INTENT_EXTRA_EVENT_ID, 0);
        pageId = getIntent().getIntExtra(INTENT_EXTRA_PAGE_ID, 0);
        personName = getIntent().getStringExtra(INTENT_EXTRA_PERSON_NAME);
        imageUrl = getIntent().getStringExtra(INTENT_EXTRA_EVENT_IMAGE_URL);
        isShow = getIntent().getBooleanExtra(INTENT_EXTRA_SHOW, false);

//        presenter.getEvent(eventId);
        settings.setVisibility(isShow ? View.INVISIBLE : View.VISIBLE);
        initVideoAdapter();
        initCommentAdapter();
        initPhotoAdapter();
        presenter.getDeadEvent(eventId);
        presenter.getComments(eventId);
        presenter.getVideos(eventId);
        presenter.getPhotos(eventId);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_event;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == Activity.RESULT_OK) {
            //assert result != null;
            if (result != null) {
                photoDialog.setUri(result.getUri());
            } else
                Log.e("TAG", "RESULT IS NULL!!!");

            Log.i("TAG", "RESULT_OK");
        }
    }

    private void showPhotoDialog() {
        photoDialog = new PhotoDialog();
        photoDialog.setCallback(this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        photoDialog.show(transaction, "photoDialog");
    }

    @Override
    public void onReceivedEvent(EventModel requestEvent) {
        eventModel = requestEvent;
        setItems(requestEvent);
    }

    @Override
    public void onReceivedComments(ArrayList<EventComments> requestEvent) {
        commentsAdapter.setList(requestEvent);
      //  comments.smoothScrollToPosition(commentsAdapter.getItemCount());
    }

    @Override
    public void onCommentAdded(Object o) {
        presenter.getComments(eventId);
    }

    @Override
    public void onCommentAddedError(Throwable throwable) {
        Toast.makeText(getBaseContext(), "ERROR", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceivedVideos(ArrayList<EventVideos> requestEvent) {
        videoAdapter.setList(requestEvent);
        //videos.smoothScrollToPosition(0);
    }

    @Override
    public void onVideoAdded(Object o){
        presenter.getVideos(eventId);
    }

    @Override
    public void onVideoAddedError(Throwable throwable) {
        Toast.makeText(getBaseContext(), "ERROR", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceivedPhotos(ArrayList<EventSliderPhotos> requestEvent) {
        if (requestEvent.size()>0){
        EventSliderPhotos s =  requestEvent.get(0);
        String d = s.getBody();
        String v =d;
        photoAdapter.setItems(requestEvent);
        }
    }

    @Override
    public void onPhotoAdded(Object o) {
        String s = o.toString();
        photoDialog.dismiss();
        Utils.showSnack(addVideo, "Успешно");
        presenter.getPhotos(eventId);
    }

    @Override
    public void onPhotoAddedError(Throwable throwable) {
                String s = "";
    }

    private void setItems(EventModel requestEvent) {
        try {
            glideLoadIntoWithError(this, BASE_SERVICE_URL + requestEvent.getPicture(), imageAvatar);
            dateView.setText(DateUtils.convertRemoteToLocalFormat(requestEvent.getDate()));
            eventName.setText(requestEvent.getName());
            description.setText(requestEvent.getDescription());
        } catch (Exception ignored) {
        }
    }

    @OnClick(R.id.settings)
    public void onSettingsClicked() {
        Intent intent = new Intent(this, AddNewEventActivity.class);
        intent.putExtra(INTENT_EXTRA_EVENT_ID, eventId);
        intent.putExtra(INTENT_EXTRA_EVENT_NAME, eventName.getText().toString());
        intent.putExtra(INTENT_EXTRA_EVENT_PERSON, personName);
        intent.putExtra(INTENT_EXTRA_EVENT_DESCRIPTION, description.getText().toString());
        intent.putExtra(INTENT_EXTRA_EVENT_IMAGE_URL, eventModel.getPicture());
        intent.putExtra(INTENT_EXTRA_EVENT_DATE, dateView.getText().toString());
        intent.putExtra(INTENT_EXTRA_PAGE_ID, pageId);
        intent.putExtra(INTENT_EXTRA_IS_EVENT_EDITING, true);
        intent.putExtra(INTENT_EXTRA_EVENT_IS_FOR_ONE, eventModel.getFlag());
        intent.putExtra(INTENT_EXTRA_EVENT_ACCESS, eventModel.getUv_show());
        intent.putExtra(BIRTH_DATE, getIntent().getStringExtra(BIRTH_DATE));
        startActivity(intent);
    }

    private void initVideoAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rvVideo);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        videoAdapter = new VideoAdapter(new ArrayList<>(), this.getLifecycle(), this);
        recyclerView.setAdapter(videoAdapter);
    }
    private void initPhotoAdapter() {
        photosView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        photosView.setLayoutManager(mLayoutManager);
        photoAdapter = new PhotoAdapter();
        photosView.setAdapter(photoAdapter);
    }


    private void initCommentAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rvComments);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        commentsAdapter = new CommentsAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(commentsAdapter);
    }

    private void showCommentDialog() {
        CommentDialog commentDialog = new CommentDialog();
        final Bundle bundle = new Bundle();
        // bundle.putInt(StoreFragment.ARG_ID, -999);
        // bundle.putSerializable("set", set);
        commentDialog.setArguments(bundle);
        commentDialog.setCancelable(true);
        commentDialog.listener = text -> {
            AddComment s = new AddComment();
            s.setComment(text);
            presenter.addComment(eventId, s);
        };
        commentDialog.show(getSupportFragmentManager(), CommentDialog.TAG);
    }

    private void showVideoDialog() {
        VideoDialog videoDialog = new VideoDialog();
        final Bundle bundle = new Bundle();
        videoDialog.setArguments(bundle);
        videoDialog.setCancelable(true);
        videoDialog.listener = (name, url) -> {
            AddVideo body = new AddVideo();
            body.setLink(url);
            body.setLinkName(name);
            presenter.addVideo(eventId, body);
        };
        videoDialog.show(getSupportFragmentManager(), VideoDialog.TAG);
    }

    @OnClick(R.id.back_button)
    public void onBackClick() {
        onBackPressed();
    }

    public static CurrentEvent getInstance() {
        return activity;
    }

    @Override
    public void onChangeComment() {

    }

    @Override
    public void onDeleteComment() {

    }

    @Override
    public void onShowAddCommentDialog() {
        showCommentDialog();
    }

    @Override
    public void showMoreComments() {
        //commentsAdapter.updateList(comentList());
        presenter.getComments(eventId);
    }

    @Override
    public void onShowAddVideoDialog() {
        showVideoDialog();
    }

    @Override
    public void showPhoto() {
        cropImage(this);
    }

    @Override
    public void sendPhoto(File imageFile, String string) {
            presenter.addPhotos(eventId,string, imageFile);
    }

    @OnClick(R.id.add_photo)
    public void pickImage() {
        if (storagePermissionGranted(this) || Build.VERSION.SDK_INT < 23) {
            showPhotoDialog();
        } else {
            verifyStoragePermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            showPhotoDialog();
        } else
            Toast.makeText(getBaseContext(), "Для загрузки фотографии разрешите доступ к хранилищу.", Toast.LENGTH_LONG).show();

    }
}
