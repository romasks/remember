package com.remember.app.ui.menu.page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.ui.adapters.PageFragmentAdapter;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.utils.PopupPageScreen;
import com.remember.app.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.IMAGES_STATUS_APPROVED;
import static com.remember.app.data.Constants.INTENT_EXTRA_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_LIST;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON;
import static com.remember.app.data.Constants.INTENT_EXTRA_SHOW;
import static com.remember.app.data.Constants.SEARCH_ON_MAIN;

public class PageActivityMenu extends BaseActivity implements PageMenuView, PageFragmentAdapter.Callback, PopupPageScreen.Callback {

    @InjectPresenter
    PageMenuPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.show_all)
    Button showAll;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.search)
    ImageView search;

    private PopupPageScreen popupWindowPage;
    private List<MemoryPageModel> memoryPages = new ArrayList<>();
    private int pageNumber = 1;
    private int countSum = 0;

    private PageFragmentAdapter pageFragmentAdapter;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);

        if (Utils.isThemeDark()) {
            title.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            back.setImageResource(R.drawable.ic_back_dark_theme);
            search.setImageResource(R.drawable.ic_search2);
        }

        pageFragmentAdapter = new PageFragmentAdapter();
        pageFragmentAdapter.setCallback(this);
        pageFragmentAdapter.setIsMainPages(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pageFragmentAdapter);

//        setUpLoadMoreListener();
        presenter.getImages(pageNumber);
//        presenter.getAllPages();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_page_menu;
    }

    private void setUpLoadMoreListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (pageNumber < countSum) {
                    progressBar.setVisibility(View.VISIBLE);
                    pageNumber++;
                    presenter.getImages(pageNumber);
                }
            }
        });
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
    }

    @OnClick(R.id.show_all)
    public void showAll() {
        showAll.setVisibility(View.GONE);
        pageNumber = 1;
        presenter.getImages(pageNumber);
    }

    @OnClick(R.id.search)
    public void searchOpen() {
        showEventScreen();
    }

    @Override
    public void sendItem(MemoryPageModel person) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra(INTENT_EXTRA_SHOW, true);
        intent.putExtra(INTENT_EXTRA_PERSON, person);
        intent.putExtra(INTENT_EXTRA_ID, person.getId());
        intent.putExtra(INTENT_EXTRA_IS_LIST, true);
        startActivity(intent);
    }

    @Override
    public void error(Throwable throwable) {
        Utils.showSnack(showAll, "Ошибка получения данных");
    }

    @Override
    public void onReceivedPages(ResponsePages responsePages) {
        if (showAll.getVisibility() != View.GONE) showAll.setVisibility(View.GONE);
        countSum = responsePages.getPages();
        memoryPages.addAll(responsePages.getResult());
        pageFragmentAdapter.setItems(memoryPages);
        if (pageNumber < countSum) {
            progressBar.setVisibility(View.VISIBLE);
            pageNumber++;
            presenter.getImages(pageNumber);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSearchedPages(List<MemoryPageModel> memoryPageModels) {
        if (memoryPageModels.isEmpty()) {
            Utils.showSnack(recyclerView, "Записи не найдены");
        }
//        showAll.setVisibility(memoryPageModels.isEmpty() ? View.VISIBLE : View.GONE);
        showAll.setVisibility(View.VISIBLE);
        pageFragmentAdapter.setItemsSearched(memoryPageModels);
        progressBar.setVisibility(View.GONE);
    }

    private void showEventScreen() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_page_screen, null);
        popupWindowPage = new PopupPageScreen(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindowPage.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        popupWindowPage.setFocusable(true);
        popupWindowPage.setCallback(this);
        popupWindowPage.setSourceType(SEARCH_ON_MAIN);
        popupWindowPage.setUp(title);
    }

    @Override
    public void onBackPressed() {
        if (popupWindowPage != null && popupWindowPage.isShowing()) {
            popupWindowPage.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void search(RequestSearchPage requestSearchPage) {
        requestSearchPage.setFlag(true);
        requestSearchPage.setStatus(IMAGES_STATUS_APPROVED);
        presenter.search(requestSearchPage);
    }
}
