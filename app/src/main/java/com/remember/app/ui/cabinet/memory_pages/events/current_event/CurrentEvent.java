package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.remember.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CurrentEvent extends AppCompatActivity {


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
    private Unbinder unbinder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event);
        unbinder = ButterKnife.bind(this);


        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}
