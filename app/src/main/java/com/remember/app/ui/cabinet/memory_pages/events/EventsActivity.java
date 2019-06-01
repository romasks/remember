package com.remember.app.ui.cabinet.memory_pages.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.remember.app.R;
import com.remember.app.ui.adapters.EventsFragmentAdapter;
import com.remember.app.ui.cabinet.memory_pages.events.add_new_event.AddNewEventActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventsActivity extends MvpAppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.new_event)
    ImageView plus;

    private Unbinder unbinder;
    private String name;
    private EventsFragmentAdapter eventsFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        unbinder = ButterKnife.bind(this);

        name = getIntent().getExtras().getString("NAME", "");
        eventsFragmentAdapter = new EventsFragmentAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventsFragmentAdapter);

        plus.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNewEventActivity.class);
            intent.putExtra("NAME", name);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
