package com.remember.app.ui.grid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.adapters.ImageAdapter;
import com.remember.app.ui.auth.AuthActivity;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GridActivity extends BaseActivity implements GridView, ImageAdapter.Callback {

    @InjectPresenter
    GridPresenter presenter;

    @BindView(R.id.image_rv)
    RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter();
        imageAdapter.setCallback(this);
        recyclerView.setAdapter(imageAdapter);

        presenter.getImages();
    }

    @OnClick(R.id.button)
    public void entry() {
        startActivity(new Intent(this, AuthActivity.class));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_grid;
    }

    @Override
    public void onReceivedImages(List<MemoryPageModel> memoryPageModel) {
        imageAdapter.setItems(memoryPageModel);
    }

    @Override
    public void openPage(MemoryPageModel memoryPageModel) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra("PERSON", memoryPageModel);
        intent.putExtra("IS_LIST", true);
        intent.putExtra("SHOW", true);
        startActivity(intent);

    }
}
