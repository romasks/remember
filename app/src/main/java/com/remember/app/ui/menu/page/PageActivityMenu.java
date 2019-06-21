package com.remember.app.ui.menu.page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.adapters.PageFragmentAdapter;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PageActivityMenu extends BaseActivity implements PageMenuView, PageFragmentAdapter.Callback {

    @InjectPresenter
    PageMenuPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.show_all)
    TextView showAll;

    private PageFragmentAdapter pageFragmentAdapter;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter.getPages();
        pageFragmentAdapter = new PageFragmentAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pageFragmentAdapter);
        pageFragmentAdapter.setCallback(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_page_menu;
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
    }

    @Override
    public void sendItem(MemoryPageModel person) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra("PERSON", person);
        intent.putExtra("ID", person.getId());
        intent.putExtra("IS_LIST", true);
        startActivity(intent);
    }

    @Override
    public void onReceivedPages(List<MemoryPageModel> memoryPageModels) {
        pageFragmentAdapter.setItems(memoryPageModels);
    }

    @Override
    public void error(Throwable throwable) {
        Snackbar.make(showAll, "Оибка получения данных", Snackbar.LENGTH_LONG).show();
    }
}
