package com.remember.app.ui.cabinet.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.EventResponse;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.adapters.EventsFragmentAdapter;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.utils.MvpAppCompatFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.remember.app.data.Constants.INTENT_EXTRA_ID;
import static com.remember.app.data.Constants.NOTIF_EVENT_TYPE_BIRTH;
import static com.remember.app.data.Constants.NOTIF_EVENT_TYPE_DEAD;
import static com.remember.app.data.Constants.NOTIF_EVENT_TYPE_DEAD_EVENT;

public class EventFragment extends MvpAppCompatFragment implements EventView, EventsFragmentAdapter.Callback {

    @InjectPresenter
    EventsPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private EventsFragmentAdapter eventsFragmentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_memory_pages, container, false);
        unbinder = ButterKnife.bind(this, v);
        presenter.getEvents();
        eventsFragmentAdapter = new EventsFragmentAdapter();
        eventsFragmentAdapter.setCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventsFragmentAdapter);
        return v;
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
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onReceivedEvents(List<EventResponse> responseEvents) {
        if (responseEvents.size() == 0)
            Toast.makeText(getContext(), "Записи не найдены", Toast.LENGTH_SHORT).show();
        eventsFragmentAdapter.setItems(responseEvents);
    }

    @Override
    public void onError(Throwable throwable) {
        Snackbar.make(recyclerView, "Ошибка получения события", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onReceivedEvent(ResponseEvents responseEvents) {
        // placeholder
    }

    @Override
    public void click(EventResponse event) {
        if (event.getType().equals(NOTIF_EVENT_TYPE_BIRTH) ||
                event.getType().equals(NOTIF_EVENT_TYPE_DEAD)) {
            Intent intent = new Intent(getActivity(), ShowPageActivity.class);
            intent.putExtra(INTENT_EXTRA_ID, event.getPageId());
            startActivity(intent);
        } else if (event.getType().equals(NOTIF_EVENT_TYPE_DEAD_EVENT)){
            Intent intent = new Intent(getActivity(), EventFullActivity.class);
            String eventJson = new Gson().toJson(event);
            intent.putExtra("EVENTS", eventJson);
            startActivity(intent);
        }
    }
}