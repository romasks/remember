package com.remember.app.ui.menu.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.remember.app.R;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.adapters.EventsFragmentAdapter;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.events.EventFullActivity;
import com.remember.app.ui.utils.PopupEventScreenLocal;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class EventsActivityMenu extends BaseActivity implements EventsFragmentAdapter.Callback, EventsMenuView, PopupEventScreenLocal.Callback {

    @InjectPresenter
    EventsMenuPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.rel_event)
    TextView relEvent;
    @BindView(R.id.show_all)
    Button showAll;
    @BindView(R.id.no_events)
    LinearLayout noEvents;

    private EventsFragmentAdapter eventsFragmentAdapter;
    private PopupEventScreenLocal popupWindowEvent;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter.getEvents();
        eventsFragmentAdapter = new EventsFragmentAdapter();
        eventsFragmentAdapter.setCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventsFragmentAdapter);
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
    }

    @OnClick(R.id.show_all)
    public void showAll() {
        showAll.setVisibility(View.GONE);
        presenter.getEvents();
    }

    @OnClick(R.id.search)
    public void openSearch() {
        String[] rhb = {"Православие", "Католицизм", "Ислам", "Иудаизм", "Буддизм",
                "Индуизм", "Другая религия", "Отсутствует"};
        try {
            showPageScreen(Arrays.asList(rhb));
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_event_menu;
    }

    @Override
    public void click(ResponseEvents events) {
        Intent intent = new Intent(this, EventFullActivity.class);
        String eventJson = new Gson().toJson(events);
        intent.putExtra("EVENTS", eventJson);
        startActivity(intent);
    }

    @Override
    public void onReceivedEvents(List<ResponseEvents> responseEvents) {
        if (responseEvents.size() == 0)
            Toast.makeText(getApplicationContext(), "Записи не найдены", Toast.LENGTH_SHORT).show();
        eventsFragmentAdapter.setItems(responseEvents);
        noEvents.setVisibility(responseEvents.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void error(Throwable throwable) {
        Snackbar.make(recyclerView, "Ошибка получения данных", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (popupWindowEvent != null && popupWindowEvent.isShowing()) {
            popupWindowEvent.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    private void showPageScreen(List<String> responseHandBooks) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_event_screen_local, null);
        popupWindowEvent = new PopupEventScreenLocal(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindowEvent.setFocusable(true);
        popupWindowEvent.setCallback(this);
        popupWindowEvent.setUp(relEvent, responseHandBooks);
    }

    @Override
    public void search(String date, int selectedIndex) {
        showAll.setVisibility(View.VISIBLE);
        presenter.searchEventReligios(date, selectedIndex);
    }
}
