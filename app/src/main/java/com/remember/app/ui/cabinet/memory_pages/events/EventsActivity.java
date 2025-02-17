package com.remember.app.ui.cabinet.memory_pages.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.adapters.EventsDeceaseAdapter;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.events.add_new_event.AddNewEventActivity;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.CurrentEvent;
import com.remember.app.ui.utils.PopupEventScreen;
import com.remember.app.ui.utils.Utils;

import java.util.List;

import butterknife.BindView;

import static com.remember.app.data.Constants.BIRTH_DATE;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_IMAGE_URL;
import static com.remember.app.data.Constants.INTENT_EXTRA_NAME;
import static com.remember.app.data.Constants.INTENT_EXTRA_PAGE_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON_NAME;
import static com.remember.app.data.Constants.INTENT_EXTRA_SHOW;

public class EventsActivity extends BaseActivity implements EventsView, EventsDeceaseAdapter.Callback, PopupEventScreen.Callback {

    private final String TAG = EventsActivity.class.getSimpleName();

    @InjectPresenter
    EventsPresenter presenter;
    static EventsActivity activity;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.new_event)
    ImageView plus;
    @BindView(R.id.back)
    ImageView back;
    /*@BindView(R.id.search)
    ImageView search;*///temporarily comment
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_events)
    LinearLayout noEvents;
    @BindView(R.id.btn_create_event)
    Button btnCreateEvent;

    private String name;
    private EventsDeceaseAdapter eventsDeceaseAdapter;
    private int pageId;
    private boolean isShow;
    private String imageUrl = "";

    private PopupEventScreen popupWindowEvent;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        activity = this;
        isShow = getIntent().getBooleanExtra(INTENT_EXTRA_SHOW, false);

        if (Utils.isThemeDark()) {
            back.setImageResource(R.drawable.ic_back_dark_theme);
            //search.setImageResource(R.drawable.ic_search2);//temporarily comment
            plus.setImageResource(R.drawable.ic_add2);
        }

        try {
            name = getIntent().getStringExtra(INTENT_EXTRA_NAME);
            pageId = getIntent().getIntExtra(INTENT_EXTRA_PAGE_ID, 1);
        } catch (NullPointerException ignored) {
        }

        presenter.getEvents(pageId);

        eventsDeceaseAdapter = new EventsDeceaseAdapter();
        eventsDeceaseAdapter.setCallback(this);
        eventsDeceaseAdapter.setIsOwnPage(!isShow);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventsDeceaseAdapter);

        plus.setVisibility(isShow ? View.GONE : View.VISIBLE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        //if (isShow) search.setLayoutParams(params); //temporarily comment

        plus.setOnClickListener(v -> {
            if (!isShow) {
                openAddNewEventScreen();
            }
        });
        btnCreateEvent.setOnClickListener(v -> {
            if (!isShow) {
                openAddNewEventScreen();
            }
        });
        /*search.setOnClickListener(v -> {
            showPageScreen();
        });*/ //temporarily comment
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_event;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getEvents(pageId);
    }

    @Override
    public void onReceivedEvent(List<RequestAddEvent> requestAddEvent) {
        eventsDeceaseAdapter.setItems(requestAddEvent);
        noEvents.setVisibility(requestAddEvent.isEmpty() ? View.VISIBLE : View.GONE);
        btnCreateEvent.setVisibility(isShow ? View.GONE : requestAddEvent.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void openAddNewEventScreen() {
        Intent intent = new Intent(this, AddNewEventActivity.class);
        intent.putExtra(INTENT_EXTRA_PERSON_NAME, name);
        intent.putExtra(INTENT_EXTRA_PAGE_ID, pageId);
        intent.putExtra(INTENT_EXTRA_EVENT_IMAGE_URL, imageUrl);
        intent.putExtra(BIRTH_DATE, getIntent().getStringExtra(BIRTH_DATE));
        startActivity(intent);
    }

    private void showPageScreen() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_event_screen, null);
        popupWindowEvent = new PopupEventScreen(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindowEvent.setFocusable(true);
        popupWindowEvent.setCallback(this);
        popupWindowEvent.setUp(title);
    }

    @Override
    public void openEvent(Integer eventId, String imageUrl) {
        Intent intent = new Intent(this, CurrentEvent.class);
        Log.d("myLog", "data2 = " + getIntent().getStringExtra(BIRTH_DATE));
        if (isShow) {
            intent.putExtra(INTENT_EXTRA_SHOW, true);
        }
        intent.putExtra(INTENT_EXTRA_EVENT_ID, eventId);
        intent.putExtra(INTENT_EXTRA_PERSON_NAME, name);
        intent.putExtra(INTENT_EXTRA_EVENT_IMAGE_URL, imageUrl);
        intent.putExtra(INTENT_EXTRA_PAGE_ID, pageId);
        intent.putExtra(BIRTH_DATE, getIntent().getStringExtra(BIRTH_DATE));
        startActivity(intent);
    }

    public static EventsActivity getInstance() {
        return activity;
    }
}
