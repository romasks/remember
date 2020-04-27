package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters.VideoAdapter;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.commentDialog.CommentDialog;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.videoDialog.VideoDialog;
import com.remember.app.ui.utils.DateUtils;
import com.remember.app.ui.utils.Utils;

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
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoWithError;
import static com.remember.app.ui.utils.StringUtils.getVideoIdFromUrl;

public class CurrentEvent extends BaseActivity implements CurrentEventView, CommentsAdapter.CommentsAdapterListener, VideoAdapter.VideoAdapterListener {

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

    private Integer eventId = 0;
    private String personName;
    private String imageUrl = "";
    private int pageId = 0;
    private boolean isShow;
    private EventModel eventModel;
    static CurrentEvent activity;
    CommentsAdapter commentsAdapter;
    VideoAdapter videoAdapter;

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

        photosView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        photosView.setAdapter(new EventStuffAdapter());

        videos.setLayoutManager(new LinearLayoutManager(this));
        videos.setAdapter(new EventStuffAdapter());

        comments.setLayoutManager(new LinearLayoutManager(this));
        comments.setAdapter(new EventStuffAdapter());

//        presenter.getEvent(eventId);
        settings.setVisibility(isShow ? View.INVISIBLE : View.VISIBLE);
        initVideoAdapter();
        initCommentAdapter();
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
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onReceivedEvent(EventModel requestEvent) {
        eventModel = requestEvent;
        setItems(requestEvent);
    }

    @Override
    public void onReceivedComments(ArrayList<EventComments> requestEvent) {
       // EventComments s = (EventComments) requestEvent;
        commentsAdapter.updateList(requestEvent);
    }

    @Override
    public void onCommentAdded(Object o) {
        String s = o.toString();
    }

    @Override
    public void onCommentAddedError(Throwable throwable) {
        Toast.makeText(getBaseContext(), "ERROR", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceivedVideos(ArrayList<EventVideos> requestEvent) {
        videoAdapter.updateList(requestEvent);
    }

    @Override
    public void onVideoAdded(Object o){
       String s = o.toString();
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
        }
    }

    @Override
    public void onPhotoAdded(Object o) {

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
        //  String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY", "6JYIGclVQdw", "LvetJ9U_tVY", "6JYIGclVQdw", "LvetJ9U_tVY", "6JYIGclVQdw", "LvetJ9U_tVY", "6JYIGclVQdw", "LvetJ9U_tVY", "6JYIGclVQdw", "LvetJ9U_tVY"};
        videoAdapter = new VideoAdapter(new ArrayList<>(), this.getLifecycle(), this);
        recyclerView.setAdapter(videoAdapter);
    }

    private List<String> videoList() {
        List<String> s = new ArrayList<>();
        s.add("8EgX-Ify_fc");
        s.add("LvetJ9U_tVY");
        s.add("Os09-mH98H4");
        return s;
    }

    private void initCommentAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rvComments);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        commentsAdapter = new CommentsAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(commentsAdapter);
       // commentsAdapter.updateList(comentList());
    }

    private List<String> comentList() {
        List<String> s = new ArrayList<>();
        s.add("dd");
        s.add("dd");
        s.add("dd");
        return s;
    }

    private void showCommentDialog() {
        CommentDialog commentDialog = new CommentDialog();
        final Bundle bundle = new Bundle();
        // bundle.putInt(StoreFragment.ARG_ID, -999);
        // bundle.putSerializable("set", set);
        commentDialog.setArguments(bundle);
        commentDialog.setCancelable(true);
        commentDialog.listener = text -> {
           // Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
//            JsonObject postParam = new JsonObject();
//            postParam.addProperty("comment ", text);
//            RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), postParam.toString());
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
           // Toast.makeText(getBaseContext(), name + "   " + url, Toast.LENGTH_LONG).show();
//            JsonObject postParam = new JsonObject();
//            postParam.addProperty("link", getVideoIdFromUrl(url));
//            postParam.addProperty("name_link ", name);
//            RequestBody body = RequestBody.create(MediaType.parse("application/json"), postParam.toString());
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
    public void showMoreVideos() {
       // videoAdapter.updateList(videoList());
        presenter.getVideos(eventId);
    }

    @Override
    public void onShowAddVideoDialog() {
        showVideoDialog();
    }
}
