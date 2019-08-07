package com.remember.app.ui.cabinet.memory_pages.events.current_event;

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
import com.bumptech.glide.request.RequestOptions;
import com.remember.app.R;
import com.remember.app.data.models.EventModel;
import com.remember.app.ui.adapters.EventStuffAdapter;
import com.remember.app.ui.base.BaseActivity;

import butterknife.BindView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photosView.setLayoutManager(new LinearLayoutManager(this));
        photosView.setAdapter(new EventStuffAdapter());
        videos.setLayoutManager(new LinearLayoutManager(this));
        videos.setAdapter(new EventStuffAdapter());
        comments.setLayoutManager(new LinearLayoutManager(this));
        comments.setAdapter(new EventStuffAdapter());
        presenter.getEvent(55);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_event;
    }

    @Override
    public void onReceivedEvent(EventModel requestEvent) {
        setItems(requestEvent);
    }

    private void setItems(EventModel requestEvent){
        name.setText(requestEvent.getName());
        Glide.with(this)
                .load("http://помню.рус"+requestEvent.getPicture())
                .into(imageAvatar);
        dateView.setText(requestEvent.getDate());
        messageView.setText(requestEvent.getName() + " - " + requestEvent.getDescription());
        photosView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        photosView.setAdapter(new EventStuffAdapter());
        videos.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        videos.setAdapter(new EventStuffAdapter());
        comments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        comments.setAdapter(new EventStuffAdapter());
    }
}
