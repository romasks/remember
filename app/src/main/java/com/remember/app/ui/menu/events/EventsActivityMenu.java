package com.remember.app.ui.menu.events;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.remember.app.R;
import com.remember.app.customView.CustomAutoCompleteTextView;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.events.EventFullActivity;
import com.remember.app.utils.PopupEventScreenLocal;
import com.remember.app.utils.Utils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EventsActivityMenu extends BaseActivity implements EventsMenuAdapter.Callback, EventsMenuView, PopupEventScreenLocal.Callback {

    @InjectPresenter
    EventsMenuPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.rel_event)
    CustomTextView relEvent;
    @BindView(R.id.show_all)
    CustomTextView showAll;
    /*@BindView(R.id.no_events)
    LinearLayout noEvents;*/
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.search)
    ImageView search;

    private EventsMenuAdapter eventsMenuAdapter;
    private PopupEventScreenLocal popupWindowEvent;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        if (Utils.isThemeDark()) {
            relEvent.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            back.setImageResource(R.drawable.ic_back_dark_theme);
            search.setImageResource(R.drawable.ic_search2);
        }
        presenter.getEvents();
        eventsMenuAdapter = new EventsMenuAdapter();
        eventsMenuAdapter.setCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventsMenuAdapter);
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
                "Индуизм", "Другая религия", "Религия"};
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
        eventsMenuAdapter.setItems(responseEvents);
        //noEvents.setVisibility(responseEvents.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void error(Throwable throwable) {
        Utils.showSnack(recyclerView, "Ошибка получения данных");
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

        ConstraintLayout layout = popupView.findViewById(R.id.cl);
        Toolbar toolbar = popupView.findViewById(R.id.toolbar);
        ImageView backImg = popupView.findViewById(R.id.back);
        CustomTextView textView = popupView.findViewById(R.id.textView2);
        CustomAutoCompleteTextView date = popupView.findViewById(R.id.date_value);
        MaterialSpinner religion = popupView.findViewById(R.id.spinner);

        if (Utils.isThemeDark()) {
            religion.setBackgroundColor(getResources().getColor(R.color.colorBlackDark));
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBlack));
            layout.setBackgroundColor(getResources().getColor(R.color.colorBlackDark));
            backImg.setImageResource(R.drawable.ic_back_dark_theme);
            textView.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            date.setTextColor(getResources().getColor(R.color.colorWhiteDark));
        }

        popupWindowEvent = new PopupEventScreenLocal(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                getSupportFragmentManager());
        popupWindowEvent.setContext(EventsActivityMenu.this);
        popupWindowEvent.setFocusable(true);
        popupWindowEvent.setCallback(this);
        popupWindowEvent.setUp(relEvent, responseHandBooks);
    }

    @Override
    public void search(String date, int selectedIndex) {
        showAll.setVisibility(View.VISIBLE);
        presenter.searchEventReligious(date, selectedIndex);
    }
}
