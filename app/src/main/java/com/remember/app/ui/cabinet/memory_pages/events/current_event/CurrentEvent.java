package com.remember.app.ui.cabinet.memory_pages.events.current_event;

import android.annotation.SuppressLint;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
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

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;

public class CurrentEvent extends BaseActivity implements CurrentEventView {

    @InjectPresenter
    CurrentEventPresenter presenter;

    @BindView(R.id.back_button)
    ImageView back;
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
    @BindView(R.id.name)
    TextView name_event;

    private Integer eventId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photosView.setLayoutManager(new LinearLayoutManager(this));
        photosView.setAdapter(new EventStuffAdapter());

        videos.setLayoutManager(new LinearLayoutManager(this));
        videos.setAdapter(new EventStuffAdapter());

        comments.setLayoutManager(new LinearLayoutManager(this));
        comments.setAdapter(new EventStuffAdapter());

        eventId = getIntent().getExtras().getInt("ID_EVENT", 0);
        presenter.getDeadEvent(eventId);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        imageAvatar.setColorFilter(filter);

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

    private void setItems(EventModel requestEvent) {
        try {
            name_event.setText(requestEvent.getName());
            Glide.with(this)
                    .load(BASE_SERVICE_URL + requestEvent.getPicture())
                    .into(imageAvatar);
            dateView.setText(formatDate(requestEvent.getDate()));
            messageView.setText(requestEvent.getDescription());
        } catch (Exception e) {

        }
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
