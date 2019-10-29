package com.remember.app.ui.cabinet.memory_pages.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.adapters.EventsDeceaseAdapter;
import com.remember.app.ui.cabinet.memory_pages.events.add_new_event.AddNewEventActivity;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.CurrentEvent;
import com.remember.app.ui.utils.MvpAppCompatActivity;
import com.remember.app.ui.utils.PopupEventScreen;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventsActivity extends MvpAppCompatActivity implements EventsView, EventsDeceaseAdapter.Callback, PopupEventScreen.Callback {

    private final String TAG = EventsActivity.class.getSimpleName();

    @InjectPresenter
    EventsPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.new_event)
    ImageView plus;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_events)
    LinearLayout noEvents;
    @BindView(R.id.btn_create_event)
    Button btnCreateEvent;

    private Unbinder unbinder;
    private String name;
    private EventsDeceaseAdapter eventsDeceaseAdapter;
    private int pageId;
    private boolean isShow;

    private PopupEventScreen popupWindowEvent;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        unbinder = ButterKnife.bind(this);
        isShow = getIntent().getBooleanExtra("SHOW", false);
        try {
            name = getIntent().getExtras().getString("NAME", "");
            pageId = getIntent().getIntExtra("ID_PAGE", 1);
        } catch (NullPointerException ignored) {
        }

        presenter.getEvents(pageId);

        eventsDeceaseAdapter = new EventsDeceaseAdapter();
        eventsDeceaseAdapter.setCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventsDeceaseAdapter);

        plus.setVisibility(isShow ? View.GONE : View.VISIBLE);
        plus.setOnClickListener(v -> {
            if (!isShow) {
                Intent intent = new Intent(this, AddNewEventActivity.class);
                intent.putExtra("NAME", name);
                intent.putExtra("ID_PAGE", pageId);
                startActivity(intent);
            }
        });
        btnCreateEvent.setOnClickListener(v -> {
            if (!isShow) {
                Intent intent = new Intent(this, AddNewEventActivity.class);
                intent.putExtra("NAME", name);
                intent.putExtra("ID_PAGE", pageId);
                startActivity(intent);
            }
        });
        search.setOnClickListener(v -> {
            showPageScreen();
        });
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onReceivedEvent(List<RequestAddEvent> requestAddEvent) {
        eventsDeceaseAdapter.setItems(requestAddEvent);
        noEvents.setVisibility(eventsDeceaseAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
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
    public void openEvent(Integer pageId) {
        Intent intent = new Intent(this, CurrentEvent.class);
        intent.putExtra("ID_EVENT", pageId);
        startActivity(intent);
    }
}
