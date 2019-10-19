package com.remember.app.ui.grid;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.ui.adapters.ImageAdapter;
import com.remember.app.ui.auth.AuthActivity;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.menu.events.EventsActivityMenu;
import com.remember.app.ui.menu.page.PageActivityMenu;
import com.remember.app.ui.menu.question.QuestionActivity;
import com.remember.app.ui.menu.settings.SettingActivity;
import com.remember.app.ui.utils.PopupPageScreen;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GridActivity extends BaseActivity implements GridView, ImageAdapter.Callback, PopupPageScreen.Callback,NavigationView.OnNavigationItemSelectedListener {

    @InjectPresenter
    GridPresenter presenter;

    @BindView(R.id.image_rv)
    RecyclerView recyclerView;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.show_all)
    Button showAll;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.textView17)
    TextView name_user;
    @BindView(R.id.imageView5)
    ImageView avatar_user;

    private RecyclerView.LayoutManager layoutManager;
    private ImageAdapter imageAdapter;
    private int pageNumber = 1;
    private int countSum = 0;
    private PopupPageScreen popupWindowPage;
    private ImageView imageViewBigAvatar;
    private final String TAG="GridActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        imageAdapter = new ImageAdapter();
        imageAdapter.setCallback(this);
        recyclerView.setAdapter(imageAdapter);

        setUpLoadMoreListener();
        presenter.getImages(pageNumber);

        showAll.setOnClickListener(v -> {
            showAll.setVisibility(View.GONE);
            pageNumber = 1;
            presenter.getImages(pageNumber);
        });
        getInfoUser();
    }

    public void setBlackWhite(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    private void getInfoUser(){
        if (!Prefs.getString("USER_ID", "").equals("")) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.drawer_layout_2);
            NavigationView navigationView = findViewById(R.id.nav_view_grid);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = headerView.findViewById(R.id.title_menu);
            imageViewBigAvatar= headerView.findViewById(R.id.logo);
            TextView textView = headerView.findViewById(R.id.textView);
            navUsername.setText(Prefs.getString("NAME_USER", ""));
            textView.setText(Prefs.getString("EMAIL", ""));
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
            name_user.setText(Prefs.getString("NAME_USER",""));
            if (!Prefs.getString("AVATAR", "").equals("")) {
                Glide.with(this)
                        .load(Prefs.getString("AVATAR", ""))
                        .apply(RequestOptions.circleCropTransform())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(avatar_user);

                Glide.with(this)
                        .load(Prefs.getString("AVATAR", ""))
                        .apply(RequestOptions.circleCropTransform())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageViewBigAvatar);
            } else {
                Glide.with(this)
                        .load(R.drawable.ic_unknown)
                        .apply(RequestOptions.circleCropTransform())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(avatar_user);

                Glide.with(this)
                        .load(R.drawable.ic_unknown)
                        .apply(RequestOptions.circleCropTransform())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageViewBigAvatar);
            }
            setBlackWhite(avatar_user);
            setBlackWhite(imageViewBigAvatar);
        } else {
            DrawerLayout drawer = findViewById(R.id.drawer_layout_2);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            avatar_user.setVisibility(View.GONE);
            name_user.setVisibility(View.GONE);

        }
    }

    @OnClick(R.id.button)
    public void entry() {
        if (!Prefs.getString("USER_ID", "").equals("")) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, AuthActivity.class));
        }
    }

    @OnClick(R.id.search)
    public void doSearch() {
        showEventScreen();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_grid;
    }

    @Override
    public void onReceivedImages(ResponsePages responsePages) {
       imageAdapter.setItems(responsePages.getResult());
        progressBar.setVisibility(View.GONE);
        countSum = responsePages.getPages();
    }

    @Override
    public void onSearchedPages(List<MemoryPageModel> memoryPageModels) {
        if(memoryPageModels.size()==0) {
            Toast.makeText(getApplicationContext(), "Записи не найдены", Toast.LENGTH_SHORT).show();
        }
        if (memoryPageModels.isEmpty()) {
            showAll.setVisibility(View.VISIBLE);
        } else {
            showAll.setVisibility(View.GONE);
        }
        imageAdapter.setItemsSearch(memoryPageModels);
        progressBar.setVisibility(View.GONE);
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

    private void showEventScreen() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_page_screen, null);
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
    public void openPage(MemoryPageModel memoryPageModel) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra("PERSON", memoryPageModel);
        intent.putExtra("IS_LIST", true);
        intent.putExtra("SHOW", true);
        startActivity(intent);

    }

    @Override
    public void search(RequestSearchPage requestSearchPage) {
        presenter.search(requestSearchPage);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        if (id == R.id.event_calendar) {
            startActivity(new Intent(this, EventsActivityMenu.class));
            return true;
        }
        if (id == R.id.memory_pages) {
            startActivity(new Intent(this, PageActivityMenu.class));
            return true;
        }
        if (id == R.id.questions) {
            startActivity(new Intent(this, QuestionActivity.class));
            return true;
        }
        if (id == R.id.exit) {
            Prefs.clear();
            startActivity(new Intent(this, GridActivity.class));
            finish();
            return true;
        }
        if (id==R.id.notifications){
            return  true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        TextView textView = drawer.findViewById(R.id.title_menu);
        textView.setText(Prefs.getString("NAME_USER", ""));
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
