package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.remember.app.R;
import com.remember.app.data.models.EventModel;
import com.remember.app.ui.adapters.EventStuffAdapter;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.events.add_new_event.AddNewEventActivity;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_IMAGE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_ID_PAGE;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON_NAME;
import static com.remember.app.data.Constants.INTENT_EXTRA_SHOW;
import static com.remember.app.ui.utils.ImageUtils.setBlackWhite;

public class CurrentEvent extends BaseActivity implements CurrentEventView {

    @InjectPresenter
    CurrentEventPresenter presenter;

    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.settings)
    ImageView settings;
    @BindView(R.id.event_name)
    TextView eventName;
    @BindView(R.id.pageAvatar)
    FrameLayout pageAvatar;
    @BindView(R.id.image_avatar)
    ImageView imageAvatar;
    @BindView(R.id.photos)
    RecyclerView photosView;
    @BindView(R.id.add_photo)
    ImageView addPhoto;
    @BindView(R.id.date)
    TextView dateView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.video)
    VideoView videoView;
    @BindView(R.id.videos)
    RecyclerView videos;
    @BindView(R.id.add_video)
    ImageView addVideo;
    @BindView(R.id.comments)
    RecyclerView comments;

    private Integer eventId = 0;
    private String personName;
    private String imageUrl = "";
    private int pageId = 0;
    private boolean isShow;
    private EventModel eventModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            eventId = getIntent().getExtras().getInt(INTENT_EXTRA_EVENT_ID, 0);
            pageId = getIntent().getExtras().getInt(INTENT_EXTRA_ID_PAGE, 0);
            personName = getIntent().getExtras().getString(INTENT_EXTRA_PERSON_NAME, "");
            imageUrl = getIntent().getExtras().getString(INTENT_EXTRA_EVENT_IMAGE_URL, "");
            isShow = getIntent().getBooleanExtra(INTENT_EXTRA_SHOW, false);
        }

        photosView.setLayoutManager(new LinearLayoutManager(this));
        photosView.setAdapter(new EventStuffAdapter());

        videos.setLayoutManager(new LinearLayoutManager(this));
        videos.setAdapter(new EventStuffAdapter());

        comments.setLayoutManager(new LinearLayoutManager(this));
        comments.setAdapter(new EventStuffAdapter());

//        presenter.getEvent(eventId);

        settings.setVisibility(isShow ? View.INVISIBLE : View.VISIBLE);

        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_event;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getDeadEvent(eventId);
    }

    @Override
    public void onReceivedEvent(EventModel requestEvent) {
        eventModel = requestEvent;
        setItems(requestEvent);
    }

    private void setItems(EventModel requestEvent) {
        try {
            Glide.with(this)
                    .load(BASE_SERVICE_URL + requestEvent.getPicture())
                    .into(imageAvatar);
            setBlackWhite(imageAvatar);
            dateView.setText(formatDate(requestEvent.getDate()));
            eventName.setText(requestEvent.getName());
            description.setText(requestEvent.getDescription());
        } catch (Exception e) {

        }
    }

    @OnClick(R.id.settings)
    public void onSettingsClicked() {
        Intent intent = new Intent(this, AddNewEventActivity.class);
        intent.putExtra(INTENT_EXTRA_EVENT_ID, eventId);
        intent.putExtra("EVENT_NAME", eventName.getText().toString());
        intent.putExtra("EVENT_PERSON", personName);
        intent.putExtra("EVENT_DESCRIPTION", description.getText().toString());
//        intent.putExtra("EVENT_IMAGE_URL", imageUrl);
        intent.putExtra("EVENT_IMAGE_URL", eventModel.getPicture());
        intent.putExtra("EVENT_DATE", dateView.getText().toString());
        intent.putExtra("PAGE_ID", pageId);
        intent.putExtra("IS_EVENT_EDITING", true);
        intent.putExtra("IS_FOR_ONE", eventModel.getFlag());
        intent.putExtra("ACCESS", eventModel.getUv_show());
        startActivity(intent);
    }

    private String formatDate(String date) {
        String result = "";
        try {
            @SuppressLint("SimpleDateFormat")
            Date dateResult = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            Format formatter = new SimpleDateFormat("dd.MM.yyyy");
            result = formatter.format(dateResult);
        } catch (ParseException e) {
            result = date;
        }
        return result;
    }
}
