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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.remember.app.R;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventResponse;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.base.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
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
    @BindView(R.id.eventName)
    TextView eventName;

    private Drawable mDefaultBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDefaultBackground = this.getResources().getDrawable(R.drawable.darth_vader);

//        if (getIntent().getExtras() != null) {
//            if (getIntent().getExtras().getBoolean("FROM_NOTIF")) {
//                presenter.getEvent(getIntent().getExtras().getInt(INTENT_EXTRA_EVENT_ID));
//            } else {
//                ResponseEvents responseEvents = new Gson().fromJson(String.valueOf(getIntent().getExtras().get("EVENTS")), ResponseEvents.class);
//                System.out.println();
//                setEventData(responseEvents);
//            }
        if (getIntent().getExtras() != null && getIntent().getExtras().getInt(INTENT_EXTRA_EVENT_ID) != 0) {
            presenter.getEvent(getIntent().getExtras().getInt(INTENT_EXTRA_EVENT_ID));
        } else {
            ResponseEvents responseEvents = new Gson().fromJson(String.valueOf(getIntent().getExtras().get("EVENTS")), ResponseEvents.class);
            EventModel eventModel = new EventModel();
            eventModel.setId(responseEvents.getId());
            eventModel.setPage_id(String.valueOf(responseEvents.getPageId()));
            eventModel.setDate(responseEvents.getPutdate());
            eventModel.setName(responseEvents.getName());
            eventModel.setFlag(responseEvents.getFlag().toString());
            eventModel.setDescription(responseEvents.getBody());
            eventModel.setPicture(responseEvents.getPicture());
            setEventData(eventModel);
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
//        setEventData(responseEvents);
    }

    @Override
    public void onReceivedDeadEvent(EventModel eventModel) {
        setEventData(eventModel);
    }

    //    private void setEventData(ResponseEvents responseEvents) {
    private void setEventData(EventModel responseEvents) {
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
        setBlackWhite(avatarImage);
        title.setText(responseEvents.getName());
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date parsedDate = inputFormat.parse(responseEvents.getDate());
            String formattedDate = outputFormat.format(parsedDate);
            date.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (responseEvents.getDescription() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                body.setText(Html.fromHtml(responseEvents.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                body.setText(Html.fromHtml(responseEvents.getDescription()));
            }
        }
        eventName.setText(responseEvents.getName());
    }

    private void setEventPicture(Object imageObj) {
        Glide.with(this)
                .load(imageObj)
                .error(mDefaultBackground)
                .into(avatarImage);
    }
}
