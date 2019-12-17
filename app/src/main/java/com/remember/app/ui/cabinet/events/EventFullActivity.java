package com.remember.app.ui.cabinet.events;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.gson.Gson;
import com.remember.app.R;
import com.remember.app.data.models.EventResponse;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.utils.DateUtils;
import com.remember.app.ui.utils.Utils;

import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_FROM_NOTIF;
import static com.remember.app.ui.utils.ImageUtils.glideLoadIntoWithError;

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
    @BindView(R.id.back_button)
    ImageView backButton;

    private Drawable mDefaultBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        mDefaultBackground = this.getResources().getDrawable(R.drawable.darth_vader);

        if (Utils.isThemeDark()) {
            backButton.setImageResource(R.drawable.ic_back_dark_theme);
            title.setTextColor(getResources().getColor(R.color.colorWhiteDark));
        }

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean(INTENT_EXTRA_FROM_NOTIF)) {
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
    public void onReceivedEvents(List<EventResponse> responseEvents) {
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
            } else if (!responseEvents.getPicture().isEmpty()) {
                setEventPicture(BASE_SERVICE_URL + responseEvents.getPicture());
            } else {
                setEventPicture(mDefaultBackground);
            }
        } catch (Exception e) {
            setEventPicture(mDefaultBackground);
        }
        title.setText(responseEvents.getName());
        date.setText(DateUtils.convertRemoteToLocalFormat(responseEvents.getPutdate()));
        if (responseEvents.getBody() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                body.setText(Html.fromHtml(responseEvents.getBody(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                body.setText(Html.fromHtml(responseEvents.getBody()));
            }
        }
    }

    private void setEventPicture(Object imageObj) {
        glideLoadIntoWithError(this, imageObj, avatarImage);
    }
}
