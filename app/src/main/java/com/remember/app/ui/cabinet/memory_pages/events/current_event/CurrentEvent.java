package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;

import butterknife.BindView;

public class CurrentEvent extends BaseActivity {

    @InjectPresenter
    CurrentEventPresenter presenter;

    @BindView(R.id.back_button)
    ImageButton back;
    @BindView(R.id.pageAvatar)
    FrameLayout pageAvatar;
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

        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_event;
    }
}
