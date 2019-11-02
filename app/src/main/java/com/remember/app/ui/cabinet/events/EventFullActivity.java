package com.remember.app.ui.cabinet.events;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.remember.app.R;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;

public class EventFullActivity extends BaseActivity implements EventView {

    @InjectPresenter
    EventsPresenter presenter;

    @BindView(R.id.avatar)
    ImageView avatarImage;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.body)
    TextView body;
    @BindView(R.id.date)
    TextView date;

    private Drawable mDefaultBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDefaultBackground = this.getResources().getDrawable(R.drawable.darth_vader);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean("FROM_NOTIF")) {
                presenter.getEvent(getIntent().getExtras().getInt("ID_EVENT"));
            } else {
                ResponseEvents responseEvents = new Gson().fromJson(String.valueOf(getIntent().getExtras().get("EVENTS")), ResponseEvents.class);
                System.out.println();
                setEventData(responseEvents);
            }
        }
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_full_event;
    }

    @Override
    public void onReceivedEvents(List<ResponseEvents> responseEvents) {
        // placeholder
    }

    @Override
    public void onError(Throwable throwable) {
        // placeholder
    }

    @Override
    public void onReceivedEvent(ResponseEvents responseEvents) {
        setEventData(responseEvents);
    }

    private void setEventData(ResponseEvents responseEvents) {
        try {
            if (!responseEvents.getPicture().contains("upload")) {
                setEventPicture(BASE_SERVICE_URL + "/uploads/" + responseEvents.getPicture());
            } else {
                setEventPicture(mDefaultBackground);
            }
        } catch (Exception e) {
            setEventPicture(mDefaultBackground);
        }
        title.setText(responseEvents.getName());
        date.setText(responseEvents.getPutdate());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            body.setText(Html.fromHtml(responseEvents.getBody(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            body.setText(Html.fromHtml(responseEvents.getBody()));
        }
    }

    private void setEventPicture(Object imageObj) {
        Glide.with(this)
                .load(imageObj)
                .error(mDefaultBackground)
                .into(avatarImage);
    }
}
