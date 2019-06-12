package com.remember.app.ui.cabinet.home;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.ui.adapters.HomeGridAdapter;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeActivity extends MvpAppCompatActivity {

    @BindView(R.id.rv_grid)
    RecyclerView recyclerView;

    private HomeGridAdapter homeGridAdapter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);

        homeGridAdapter = new HomeGridAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(homeGridAdapter);
    }
}
