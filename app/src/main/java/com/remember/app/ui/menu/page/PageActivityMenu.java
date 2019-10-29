package com.remember.app.ui.menu.page;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.ui.adapters.PageFragmentAdapter;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.utils.PopupPageScreen;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.search2)
    ImageView search;

    private PopupPageScreen popupWindowPage;
    private ProgressDialog progressDialog;
    private int pageNumber = 1;
    private int countSum = 0;

    private PageFragmentAdapter pageFragmentAdapter;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Prefs.getInt("IS_THEME",0)==2) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppTheme_Dark);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        if (Prefs.getInt("IS_THEME",0)==2){
            title.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            back.setImageResource(R.drawable.ic_back_dark_theme);
            search.setImageResource(R.drawable.ic_search2);

        }
        pageFragmentAdapter = new PageFragmentAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pageFragmentAdapter);
        pageFragmentAdapter.setCallback(this);

        setUpLoadMoreListener();
        presenter.getImages(pageNumber);
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
        pageNumber = 1;
        presenter.getImages(pageNumber);
    }

    @OnClick(R.id.search2)
    public void searchOpen(){
        showEventScreen();
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
    public void error(Throwable throwable) {
        Snackbar.make(showAll, "Ошибка получения данных", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onReceivedPages(ResponsePages responsePages) {
        showAll.setVisibility(View.GONE);
        pageFragmentAdapter.setItems(responsePages.getResult());
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSearchedPages(List<MemoryPageModel> memoryPageModels) {
        if(memoryPageModels.size()==0) {
            Toast.makeText(getApplicationContext(), "Записи не найдены", Toast.LENGTH_SHORT).show();
        }
        if (memoryPageModels.isEmpty()){
            showAll.setVisibility(View.VISIBLE);
        } else {
            showAll.setVisibility(View.GONE);
        }
        pageFragmentAdapter.setItemsSearched(memoryPageModels);
        progressBar.setVisibility(View.GONE);
    }

    private void showEventScreen() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_page_screen, null);
        ConstraintLayout layout=popupView.findViewById(R.id.cont);
        Toolbar toolbar=popupView.findViewById(R.id.toolbar);
        ImageView backImg=popupView.findViewById(R.id.back);
        TextView textView=popupView.findViewById(R.id.textView2);
        AutoCompleteTextView lastName=popupView.findViewById(R.id.last_name_value);
        AutoCompleteTextView name=popupView.findViewById(R.id.first_name_value);
        AutoCompleteTextView middleName=popupView.findViewById(R.id.father_name_value);
        AutoCompleteTextView place=popupView.findViewById(R.id.live_place_value);
        AutoCompleteTextView dateBegin=popupView.findViewById(R.id.date_begin_value);
        AutoCompleteTextView dateEnd=popupView.findViewById(R.id.date_end_value);
        if (Prefs.getInt("IS_THEME",0)==2) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBlack));
            layout.setBackgroundColor(getResources().getColor(R.color.colorBlacDark));
            backImg.setImageResource(R.drawable.ic_back_dark_theme);
            textView.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            name.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            lastName.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            middleName.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            dateBegin.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            dateEnd.setTextColor(getResources().getColor(R.color.colorWhiteDark));
            place.setTextColor(getResources().getColor(R.color.colorWhiteDark));
        }
        popupWindowPage = new PopupPageScreen(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindowPage.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        popupWindowPage.setFocusable(true);
        popupWindowPage.setCallback(this);
        popupWindowPage.setUp(title);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (popupWindowPage != null && popupWindowPage.isShowing()) {
            popupWindowPage.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void search(RequestSearchPage requestSearchPage) {
        presenter.search(requestSearchPage);
    }
}
