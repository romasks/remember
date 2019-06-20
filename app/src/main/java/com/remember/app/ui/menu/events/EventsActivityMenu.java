package com.remember.app.ui.menu.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.remember.app.R;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.adapters.EventsFragmentAdapter;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.events.EventFullActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EventsActivityMenu extends BaseActivity implements EventsFragmentAdapter.Callback, EventsMenuView {

    @InjectPresenter
    EventsMenuPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;


    private EventsFragmentAdapter eventsFragmentAdapter;

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

    @Override
    protected int getContentView() {
        return R.layout.activity_event_menu;
    }

    @Override
    public void click(ResponseEvents events) {
        Intent intent = new Intent(this, EventFullActivity.class);
        String eventJson = new Gson().toJson(events);
        intent.putExtra("EVENT", eventJson);
        startActivity(intent);
    }

    @Override
    public void onReceivedEvents(List<ResponseEvents> responseEvents) {
        eventsFragmentAdapter.setItems(responseEvents);
    }

    @Override
    public void error(Throwable throwable) {
        Snackbar.make(recyclerView, "Оибка получения данных", Snackbar.LENGTH_LONG).show();
    }
}
