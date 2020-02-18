package com.remember.app.ui.cabinet.events;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.EventModel;
import com.remember.app.data.models.EventResponse;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.adapters.EventsFragmentAdapter;
import com.remember.app.ui.base.BaseFragment;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.utils.Utils;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_ID;
import static com.remember.app.data.Constants.NOTIF_EVENT_TYPE_BIRTH;
import static com.remember.app.data.Constants.NOTIF_EVENT_TYPE_DEAD;
import static com.remember.app.data.Constants.NOTIF_EVENT_TYPE_DEAD_EVENT;

public class EventFragment extends BaseFragment implements EventView, EventsFragmentAdapter.Callback {

    @InjectPresenter
    EventsPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.no_events)
    LinearLayout noEventsLayout;

    private EventsFragmentAdapter eventsFragmentAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_memory_pages;
    }

    @Override
    protected void setUp() {
        eventsFragmentAdapter = new EventsFragmentAdapter();
        eventsFragmentAdapter.setCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventsFragmentAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getEvents();
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
            Prefs.putBoolean("EVENT_FRAGMENT", true);
            Prefs.putBoolean("PAGE_FRAGMENT", false);
        } else {
            Prefs.putBoolean("EVENT_FRAGMENT", false);
            Prefs.putBoolean("PAGE_FRAGMENT", true);
        }
    }

    @Override
    public void onReceivedEvents(List<EventResponse> responseEvents) {
        noEventsLayout.setVisibility(responseEvents.isEmpty() ? View.VISIBLE : View.GONE);
        eventsFragmentAdapter.setItems(responseEvents);
    }

    @Override
    public void onError(Throwable throwable) {
        Utils.showSnack(recyclerView, "Ошибка получения события");
    }

    @Override
    public void onReceivedEvent(ResponseEvents responseEvents) {
        // placeholder
    }

    @Override
    public void onReceivedDeadEvent(EventModel eventModel) {
        // placeholder
    }

    @Override
    public void click(EventResponse event) {
        if (event.getType().equals(NOTIF_EVENT_TYPE_BIRTH) ||
                event.getType().equals(NOTIF_EVENT_TYPE_DEAD)) {
            Intent intent = new Intent(getActivity(), ShowPageActivity.class);
            intent.putExtra(INTENT_EXTRA_ID, event.getPageId());
            startActivity(intent);
        } else if (event.getType().equals(NOTIF_EVENT_TYPE_DEAD_EVENT)) {
            Intent intent = new Intent(getActivity(), EventFullActivity.class);
            String eventJson = new Gson().toJson(event);
            intent.putExtra("EVENTS", eventJson);
            intent.putExtra(INTENT_EXTRA_EVENT_ID, event.getEventId());
            startActivity(intent);
        }
    }
}