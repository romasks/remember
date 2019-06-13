package com.remember.app.ui.cabinet.memory_pages.events;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.RequestAddEvent;
import com.remember.app.ui.adapters.EventsDeceaseAdapter;
import com.remember.app.ui.cabinet.memory_pages.events.add_new_event.AddNewEventActivity;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventsActivity extends MvpAppCompatActivity implements EventsView {

    @InjectPresenter
    EventsPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.new_event)
    ImageView plus;
    @BindView(R.id.back)
    ImageView back;

    private Unbinder unbinder;
    private String name;
    private EventsDeceaseAdapter eventsDeceaseAdapter;
    private int pageId;
    private boolean isShow;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventsDeceaseAdapter);

        plus.setOnClickListener(v -> {
            if (!isShow){
                Intent intent = new Intent(this, AddNewEventActivity.class);
                intent.putExtra("NAME", name);
                intent.putExtra("ID_PAGE", pageId);
                startActivity(intent);
            }
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
    }
}
