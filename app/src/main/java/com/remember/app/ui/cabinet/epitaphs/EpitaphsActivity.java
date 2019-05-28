package com.remember.app.ui.cabinet.epitaphs;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.remember.app.R;
import com.remember.app.ui.adapters.EpitaphsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EpitaphsActivity extends MvpAppCompatActivity {

    @BindView(R.id.rv_epitaphs)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private EpitaphsAdapter epitaphsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epitaphs);
        unbinder = ButterKnife.bind(this);

        epitaphsAdapter = new EpitaphsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(epitaphsAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
