package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.customView.LinearLayoutManagerWithoutScroll;
import com.remember.app.data.Constants;
import com.remember.app.data.models.AddComment;
import com.remember.app.data.models.AddVideo;
import com.remember.app.data.models.DeleteVideo;
import com.remember.app.data.models.EventComments;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventSliderPhotos;
import com.remember.app.data.models.EventVideos;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.events.add_new_event.AddNewEventActivity;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters.CommentsAdapter;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters.PhotoAdapter;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters.VideoAdapter;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.commentDialog.CommentDialog;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.commentDialog.DeleteCommentDialog;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.commentDialog.EditDialog;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.reviewPhoto.ReviewPhotoActivity;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.videoDialog.AddVideoDialog;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.videoDialog.DeleteVideoDialog;
import com.remember.app.ui.utils.DateUtils;
import com.remember.app.ui.utils.PhotoDialog;
import com.remember.app.ui.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
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
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoWithError;

public class CurrentEvent extends BaseActivity implements CurrentEventView, CommentsAdapter.CommentsAdapterListener, VideoAdapter.VideoAdapterListener, PhotoDialog.Callback, DeleteCommentDialog.Callback, PhotoAdapter.ItemClickListener, ReviewPhotoActivity.DeleteCallBack, DeleteVideoDialog.Callback {

    @InjectPresenter
    CurrentEventPresenter presenter;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
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
    int changedCommentPosition = 0;
    int currentVideoPosition = 0;
    String newComment = "";
    ArrayList<EventSliderPhotos> photoList = new ArrayList<>();
    LinearLayoutManagerWithoutScroll linearLayoutManagerWithoutScroll;
    private boolean isFull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        activity = this;


        initIU(true);

    }

    private void initIU(boolean openFirst) {
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Utils.isThemeDark()) {
            back.setImageResource(R.drawable.ic_back_dark_theme);
            settings.setImageResource(R.drawable.setting_white);
        }

        eventId = getIntent().getIntExtra(INTENT_EXTRA_EVENT_ID, 0);
        pageId = getIntent().getIntExtra(INTENT_EXTRA_PAGE_ID, 0);
        personName = getIntent().getStringExtra(INTENT_EXTRA_PERSON_NAME);
        imageUrl = getIntent().getStringExtra(INTENT_EXTRA_EVENT_IMAGE_URL);
        isShow = getIntent().getBooleanExtra(INTENT_EXTRA_SHOW, false);

        settings.setVisibility(isShow ? View.INVISIBLE : View.VISIBLE);
        initVideoAdapter();
        initCommentAdapter();
        initPhotoAdapter();
        if (openFirst) {
            presenter.getDeadEvent(eventId);
            presenter.getComments(eventId);
            presenter.getVideos(eventId);
            presenter.getPhotos(eventId);
        }
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
        presenter.saveUseData(requestEvent);
        setItems(requestEvent);
    }

    @Override
    public void onReceivedComments(ArrayList<EventComments> requestEvent) {
        commentsAdapter.setList(requestEvent);
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
    public void onCommentDelete(Object o) {
        commentsAdapter.editList(new EventComments(), changedCommentPosition, true);
    }

    @Override
    public void onCommentEdit(Object o) {
        EventComments comment = commentsAdapter.getList().get(changedCommentPosition);
        comment.setComment(newComment);
        commentsAdapter.editList(comment, changedCommentPosition, false);
    }

    @Override
    public void onReceivedVideos(ArrayList<EventVideos> requestEvent) {
        videoAdapter.setList(requestEvent);
    }

    @Override
    public void onVideoAdded(Object o) {
        presenter.getVideos(eventId);
    }

    @Override
    public void onVideoAddedError(Throwable throwable) {
        Toast.makeText(getBaseContext(), "ERROR", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVideoDelete(Object o) {
        ArrayList<EventVideos> newList = videoAdapter.getItems();
        newList.remove(currentVideoPosition);
        videoAdapter.setList(newList);
        // videoAdapter.updateList(currentVideoPosition);
    }

    @Override
    public void onVideoDeleteError(Throwable throwable) {
        Log.d("error delete video", throwable.getMessage());
    }

    @Override
    public void onReceivedPhotos(ArrayList<EventSliderPhotos> requestEvent) {
        if (requestEvent.size() > 0) {
            photoAdapter.setItems(requestEvent);
            photoList = requestEvent;
        }
    }

    @Override
    public void onPhotoAdded(Object o) {
        photoDialog.dismiss();
        //   Utils.showSnack(addVideo, "Успешно");
        presenter.getPhotos(eventId);
    }

    @Override
    public void onPhotoAddedError(Throwable throwable) {
    }

    private void setItems(EventModel requestEvent) {
        try {
            glideLoadIntoWithError(this, BASE_SERVICE_URL + requestEvent.getPicture(), imageAvatar);
            dateView.setText(DateUtils.convertRemoteToLocalFormat(requestEvent.getDate()));
            eventName.setText(requestEvent.getName());
            description.setText(requestEvent.getDescription());
        } catch (Exception ignored) {
            Log.d("dd", "ddd");
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
        linearLayoutManagerWithoutScroll = new LinearLayoutManagerWithoutScroll(this);
        linearLayoutManagerWithoutScroll = new LinearLayoutManagerWithoutScroll(getBaseContext()) {
            @Override
            public boolean canScrollVertically() {
                isFull = Prefs.getBoolean("isFull", false);
                return !isFull;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManagerWithoutScroll);
        videoAdapter = new VideoAdapter(presenter.getVideoList(), this.getLifecycle(), this);
        recyclerView.setAdapter(videoAdapter);
        recyclerView.swapAdapter(videoAdapter, true);
        if (Prefs.getBoolean("isFull", false))
            linearLayoutManagerWithoutScroll.scrollToPositionWithOffset(presenter.getAdapterPosition(), 0);
    }

    private void initPhotoAdapter() {
        photosView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        photosView.setLayoutManager(mLayoutManager);
        photoAdapter = new PhotoAdapter(presenter.getPhotoList());
        photosView.setAdapter(photoAdapter);
        photoAdapter.setClickListener(this);
    }


    private void initCommentAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rvComments);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        commentsAdapter = new CommentsAdapter(this, presenter.getCommentsList());
        recyclerView.setAdapter(commentsAdapter);
    }

    private void showCommentDialog() {
        CommentDialog commentDialog = new CommentDialog();
        final Bundle bundle = new Bundle();
        commentDialog.setArguments(bundle);
        commentDialog.setCancelable(true);
        commentDialog.listener = text -> {
            AddComment s = new AddComment();
            s.setComment(text);
            presenter.addComment(eventId, s);
        };
        commentDialog.show(getSupportFragmentManager(), CommentDialog.TAG);
    }

    private void showEditCommentDialog(int commentID, String comment, int itemPosition) {
        EditDialog editDialog = new EditDialog();
        final Bundle bundle = new Bundle();
        bundle.putInt("id", commentID);
        bundle.putString("comment", comment);
        bundle.putInt("position", itemPosition);
        editDialog.setArguments(bundle);
        editDialog.setCancelable(true);
        editDialog.listener = (text, commentID1, position) -> {
            AddComment s = new AddComment();
            s.setComment(text);
            presenter.editComment(eventId, commentID, s);
            newComment = text;
            changedCommentPosition = position;
        };
        editDialog.show(getSupportFragmentManager(), EditDialog.TAG);
    }

    private void showVideoDialog() {
        AddVideoDialog videoDialog = new AddVideoDialog();
        final Bundle bundle = new Bundle();
        videoDialog.setArguments(bundle);
        videoDialog.setCancelable(true);
        videoDialog.listener = (name, url) -> {
            AddVideo body = new AddVideo();
            body.setLink(url);
            body.setLinkName(name);
            presenter.addVideo(eventId, body);
        };
        videoDialog.show(getSupportFragmentManager(), AddVideoDialog.TAG);
    }

    @OnClick(R.id.back_button)
    public void onBackClick() {
        onBackPressed();
    }

    public static CurrentEvent getInstance() {
        return activity;
    }

    public void showDeleteDialog(int commentID, int position) {
        DeleteCommentDialog myDialogFragment = new DeleteCommentDialog();
        myDialogFragment.setCallback(this);
        final Bundle bundle = new Bundle();
        bundle.putInt("id", commentID);
        bundle.putInt("position", position);
        myDialogFragment.setArguments(bundle);
        myDialogFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }

    public void showDeleteVideoDialog(String link, int position) {
        DeleteVideoDialog myDialogFragment = new DeleteVideoDialog();
        myDialogFragment.setCallback(this);
        final Bundle bundle = new Bundle();
        bundle.putString("link", link);
        bundle.putInt("position", position);
        myDialogFragment.setArguments(bundle);
        myDialogFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }

    @Override
    public void onChangeComment(int commentID, String newComment, int position) {
        showEditCommentDialog(commentID, newComment, position);
    }

    @Override
    public void onDeleteComment(int commentID, int position) {
        showDeleteDialog(commentID, position);
    }

    @Override
    public void onShowAddCommentDialog() {
        showCommentDialog();
    }

    @Override
    public void showMoreComments() {
        presenter.getComments(eventId);
    }

    @Override
    public void onShowAddVideoDialog() {
        showVideoDialog();
    }

    @Override
    public void onShowDeleteVideoDialog(String link, int position) {
        showDeleteVideoDialog(link, position);
    }

    @Override
    public void openFull(int position) {
        presenter.saveAdapterPosition(position);
        presenter.saveVideoList(videoAdapter.getItems());
        presenter.saveCommentsList(commentsAdapter.getList());
        presenter.savePhotoList(photoAdapter.getList());
        presenter.saveScrollViewPosition(scrollView.getScrollY());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        isFull = true;
        Prefs.putBoolean("isFull", true);
        //  linearLayoutManagerWithoutScroll.scrollToPositionWithOffset(presenter.getAdapterPosition(), 0);

    }

    @Override
    public void closeFull() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isFull = false;
        Prefs.putBoolean("isFull", false);
    }

    @Override
    public void showPhoto() {
        cropImage(this);
    }

    @Override
    public void sendPhoto(File imageFile, String string) {
        presenter.addPhotos(eventId, string, imageFile);
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

    @Override
    public void onDelete(int commentID, int position) {
        presenter.deleteComment(eventId, commentID);
        changedCommentPosition = position;
    }

    @Override
    public void onItemClick(View view, int position, ArrayList<EventSliderPhotos> list) {
        Bundle extra = new Bundle();
        extra.putSerializable("objects", list);
        startActivity(
                new Intent(this, ReviewPhotoActivity.class)
                        .putExtra(Constants.INTENT_EXTRA_ID, eventId)
                        .putExtra(Constants.INTENT_EXTRA_POSITION_IN_SLIDER, position)
                        .putExtra("list", extra)
        );
        ReviewPhotoActivity.setCallback(this);
    }

    @Override
    public void deletePhoto(int position) {
        photoList.remove(photoAdapter.getItems().get(position));
        photoAdapter.setItems(photoList);
    }

    @Override
    public void onDeleteVideo(DeleteVideo body, int position) {
        currentVideoPosition = position;
        presenter.deleteVideo(eventId, body);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            int pos = presenter.getAdapterPosition();
            setContentView(R.layout.fragment_event_land);
            initVideoAdapter();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //linearLayoutManagerWithoutScroll.scrollToPositionWithOffset(pos, 0);
            //  Prefs.putBoolean("isFull", false);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.fragment_event);
            ButterKnife.bind(this);
            initIU(false);
            setItems(presenter.getUserData());

//            new Handler().post(() -> {
//                int po = presenter.getScrollViewPosition();
//                Log.d("DDDDDDDDDDDDDDDDDDDDDDD", po+"");
//                scrollView.smoothScrollTo(0, po);
//            });

        }
    }
}
