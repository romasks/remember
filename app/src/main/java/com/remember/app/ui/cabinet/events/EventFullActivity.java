package com.remember.app.ui.cabinet.events;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.gson.Gson;
import com.remember.app.R;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoWithError;
import static com.remember.app.ui.utils.ImageUtils.setBlackWhite;

public class EventFullActivity extends BaseActivity implements EventView {

    @InjectPresenter
    EventsPresenter presenter;

    @BindView(R.id.avatar)
    ImageView avatarImage;
    @BindView(R.id.settings)
    ImageView settingsImage;
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
                presenter.getEvent(getIntent().getExtras().getInt(INTENT_EXTRA_EVENT_ID));
            } else {
                ResponseEvents responseEvents = new Gson().fromJson(String.valueOf(getIntent().getExtras().get("EVENTS")), ResponseEvents.class);
                System.out.println();
                setEventData(responseEvents);
            }
        }

        settingsImage.setVisibility(View.INVISIBLE);
        settingsImage.setEnabled(false);
    }

    @OnClick(R.id.back_button)
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
                glideLoadIntoWithError(this, mDefaultBackground, avatarImage);
            }
        } catch (Exception e) {
            glideLoadIntoWithError(this, mDefaultBackground, avatarImage);
        }
        setBlackWhite(avatarImage);
        title.setText(responseEvents.getName());
        date.setText(responseEvents.getPutdate());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            body.setText(Html.fromHtml(responseEvents.getBody(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            body.setText(Html.fromHtml(responseEvents.getBody()));
        }
    }

    private void setEventPicture(Object imageObj) {
        glideLoadIntoWithError(this, imageObj, avatarImage);
    }
}
