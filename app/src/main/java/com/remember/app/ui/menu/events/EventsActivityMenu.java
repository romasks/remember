package com.remember.app.ui.menu.events;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.adapters.EventsFragmentAdapter;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.events.EventFullActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.PopupEventScreen;
import com.remember.app.ui.utils.PopupEventScreenLocal;
import com.remember.app.ui.utils.PopupPageScreen;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EventsActivityMenu extends BaseActivity implements EventsFragmentAdapter.Callback, EventsMenuView, PopupEventScreenLocal.Callback {

    @InjectPresenter
    EventsMenuPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.rel_event)
    TextView relEvent;

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

    @OnClick(R.id.search)
    public void openSearch(){
        String [] rhb = {"aaa", "bbb", "ccc"};
        showPageScreen(Arrays.asList(rhb));
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

}
