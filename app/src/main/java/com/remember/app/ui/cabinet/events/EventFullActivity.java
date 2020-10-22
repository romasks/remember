package com.remember.app.ui.cabinet.events;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.gson.Gson;
import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventResponse;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.events.add_new_event.AddNewEventActivity;
import com.remember.app.utils.DateUtils;
import com.remember.app.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.BASE_URL_FROM_PHOTO;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ACCESS;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_DATE;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_DESCRIPTION;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_IMAGE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_IS_FOR_ONE;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_NAME;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_PERSON;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_EVENT_EDITING;
import static com.remember.app.data.Constants.INTENT_EXTRA_PAGE_ID;
import static com.remember.app.utils.ImageUtils.glideLoadIntoWithError;

public class EventFullActivity extends BaseActivity implements EventView {

    @InjectPresenter
    EventsPresenter presenter;

    @BindView(R.id.avatar)
    ImageView avatarImage;
    @BindView(R.id.settings)
    ImageView settingsImage;
    @BindView(R.id.tvTitle)
    CustomTextView title;
    @BindView(R.id.body)
    CustomTextView body;
    @BindView(R.id.date)
    CustomTextView date;
    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.eventName)
    CustomTextView eventName;
    private boolean isEventReligion = false;
    private Drawable mDefaultBackground;
    private EventModel eventModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        mDefaultBackground = this.getResources().getDrawable(R.drawable.no_photo);

        if (Utils.isThemeDark()) {
            backButton.setImageResource(R.drawable.ic_back_dark_theme);
            title.setTextColor(getResources().getColor(R.color.colorWhiteDark));
        }
        settingsImage.setVisibility(View.GONE);

//        if (getIntent().getExtras() != null) {
//            if (getIntent().getExtras().getBoolean(INTENT_EXTRA_FROM_NOTIF)) {
//                presenter.getEvent(getIntent().getExtras().getInt(INTENT_EXTRA_EVENT_ID));
//            } else {
//                ResponseEvents responseEvents = new Gson().fromJson(String.valueOf(getIntent().getExtras().get("EVENTS")), ResponseEvents.class);
//                System.out.println();
//                setEventData(responseEvents);
//            }
//        }

        if (getIntent().getExtras() != null && getIntent().getExtras().getInt(INTENT_EXTRA_EVENT_ID) != 0) {
            presenter.getEvent(getIntent().getExtras().getInt(INTENT_EXTRA_EVENT_ID));

        } else {
            isEventReligion = true;
            ResponseEvents responseEvents = new Gson().fromJson(String.valueOf(getIntent().getExtras().get("EVENTS")), ResponseEvents.class);
            eventModel = new EventModel();
            eventModel.setId(responseEvents.getId());
            eventModel.setPage_id(String.valueOf(responseEvents.getPageId()));
            eventModel.setDate(responseEvents.getPutdate());
            eventModel.setName(responseEvents.getName());
            eventModel.setFlag(String.valueOf(responseEvents.getFlag()));
            eventModel.setDescription(responseEvents.getBody());
            eventModel.setPicture(responseEvents.getPicture());
              setEventData(eventModel);
        }

//        if (getIntent().getExtras() != null) {
//            if (getIntent().getExtras().getString("event") != null)
//                if (getIntent().getExtras().getString("event").equals("event")) {
//                    presenter.getEvent(getIntent().getExtras().getInt(INTENT_EXTRA_EVENT_ID));
//                    settingsImage.setVisibility(View.GONE);
//                } else {
//                    ResponseEvents responseEvents = new Gson().fromJson(String.valueOf(getIntent().getExtras().get("EVENTS")), ResponseEvents.class);
//                    System.out.println();
//                    setEventData(responseEvents);
//                }
//        }
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
        Log.d("TT", "dd");
    }

    @Override
    public void onError(Throwable throwable) {
        // placeholder
        String s = throwable.getMessage();
        Log.d("TT", "dd");
    }

    @Override
    public void onReceivedEvent(ResponseEvents responseEvents) {
//        setEventData(responseEvents);
        setEventData(responseEvents);
        Log.d("TT", "dd");
        title.setText(R.string.events_calendar_header_text);
    }

    @Override
    public void onReceivedDeadEvent(EventModel eventModel) {
        this.eventModel = eventModel;
        setEventData(eventModel);
    }

    private void setEventData(EventModel responseEvents) {
        try {
            if (!responseEvents.getPicture().contains("upload")) {
                setEventPicture(BASE_URL_FROM_PHOTO + "/uploads/" + responseEvents.getPicture());
            } else if (!responseEvents.getPicture().isEmpty()) {
                setEventPicture(BASE_URL_FROM_PHOTO + responseEvents.getPicture());
            } else {
                setEventPicture(mDefaultBackground);
            }
        } catch (Exception e) {
            setEventPicture(mDefaultBackground);
        }

        if (isEventReligion) {
            title.setText(R.string.events_calendar_header_text);
            date.setText(responseEvents.getDate());
        } else {
//            title.setText(responseEvents.getName());
            title.setText(R.string.memory_page_event_header_text);
            date.setText(DateUtils.convertRemoteToLocalFormat(responseEvents.getDate()));
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

    private void setEventData(ResponseEvents responseEvents) {
        try {
            if (!responseEvents.getPicture().contains("upload")) {
                setEventPicture(BASE_URL_FROM_PHOTO + "/uploads/" + responseEvents.getPicture());
            } else if (!responseEvents.getPicture().isEmpty()) {
                setEventPicture(BASE_URL_FROM_PHOTO + responseEvents.getPicture());
            } else {
                setEventPicture(mDefaultBackground);
            }
        } catch (Exception e) {
            setEventPicture(mDefaultBackground);
        }

        if (isEventReligion) {
            title.setText(R.string.events_calendar_header_text);
            date.setText(DateUtils.convertReligiousEventServerFormat(responseEvents.getPutdate()));
        } else {
//            title.setText(responseEvents.getName());
            title.setText(R.string.memory_page_event_header_text);
            date.setText(DateUtils.convertRemoteToLocalFormat(responseEvents.getPutdate()));
        }

        if (responseEvents.getBody() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                body.setText(Html.fromHtml(responseEvents.getBody(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                body.setText(Html.fromHtml(responseEvents.getBody()));
            }
        }

        eventName.setText(responseEvents.getName());
    }

    private void setEventPicture(Object imageObj) {
        glideLoadIntoWithError(this, imageObj, avatarImage);
    }

    @OnClick(R.id.settings)
    public void onSettingsClicked() {
        Intent intent = new Intent(this, AddNewEventActivity.class);
        intent.putExtra(INTENT_EXTRA_EVENT_ID, eventModel.getId() == null ? -999 : eventModel.getId());
        intent.putExtra(INTENT_EXTRA_EVENT_NAME, eventName.getText().toString());
        intent.putExtra(INTENT_EXTRA_EVENT_PERSON, eventModel.getName());
        intent.putExtra(INTENT_EXTRA_EVENT_DESCRIPTION, eventModel.getDescription());
        intent.putExtra(INTENT_EXTRA_EVENT_IMAGE_URL, eventModel.getPicture());
        intent.putExtra(INTENT_EXTRA_EVENT_DATE, eventModel.getDate());
        intent.putExtra(INTENT_EXTRA_PAGE_ID, eventModel.getPage_id());
        intent.putExtra(INTENT_EXTRA_IS_EVENT_EDITING, true);
        intent.putExtra(INTENT_EXTRA_EVENT_IS_FOR_ONE, eventModel.getFlag());
        intent.putExtra(INTENT_EXTRA_EVENT_ACCESS, eventModel.getUv_show());
        startActivity(intent);
    }
}
