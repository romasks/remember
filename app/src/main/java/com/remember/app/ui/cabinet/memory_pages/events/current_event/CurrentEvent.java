package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

public class CurrentEvent extends BaseActivity implements CurrentEventView {

    @InjectPresenter
    CurrentEventPresenter presenter;

    @BindView(R.id.back_button)
    ImageButton back;
    @BindView(R.id.name)
    TextView name;
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
    @BindView(R.id.messageHeader)
    TextView messageHeader;
    @BindView(R.id.message)
    TextView messageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventId = getIntent().getExtras().getInt("ID_EVENT", 0);
        pageId = getIntent().getExtras().getInt("PAGE_ID", 0);
        personName = getIntent().getExtras().getString("PERSON_NAME", "");
        imageUrl = getIntent().getExtras().getString("EVENT_IMAGE_URL", "");
        photosView.setLayoutManager(new LinearLayoutManager(this));
        photosView.setAdapter(new EventStuffAdapter());
        videos.setLayoutManager(new LinearLayoutManager(this));
        videos.setAdapter(new EventStuffAdapter());
        comments.setLayoutManager(new LinearLayoutManager(this));
        comments.setAdapter(new EventStuffAdapter());
//        presenter.getEvent(eventId);
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
        presenter.getEvent(eventId);
    }

    @Override
    public void onReceivedEvent(EventModel requestEvent) {
        setItems(requestEvent);
    }

    private void setItems(EventModel requestEvent) {
        try {
            name.setText(requestEvent.getName());
            Glide.with(this)
                    .load("http://помню.рус" + requestEvent.getPicture())
                    .into(imageAvatar);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            imageAvatar.setColorFilter(filter);
            dateView.setText(formatDate(requestEvent.getDate()));
            messageHeader.setText(requestEvent.getName());
            messageView.setText(requestEvent.getDescription());
//            messageView.setText(requestEvent.getName() + " - " + requestEvent.getDescription());
        } catch (Exception e) {

        }
    }

    @OnClick(R.id.settings)
    public void onSettingsClicked() {
        Intent intent = new Intent(this, AddNewEventActivity.class);
        intent.putExtra("EVENT_ID", eventId);
        intent.putExtra("EVENT_NAME", name.getText().toString());
        intent.putExtra("EVENT_PERSON", personName);
        intent.putExtra("EVENT_DESCRIPTION", messageView.getText().toString());
        intent.putExtra("EVENT_IMAGE_URL", imageUrl);
        intent.putExtra("EVENT_DATE", dateView.getText().toString());
        intent.putExtra("PAGE_ID", pageId);
        intent.putExtra("IS_EVENT_EDITING", true);
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
