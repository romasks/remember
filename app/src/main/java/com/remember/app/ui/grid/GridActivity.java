package com.remember.app.ui.grid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.navigation.NavigationView;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.ui.adapters.ImageAdapter;
import com.remember.app.ui.adapters.ImagePagedAdapter;
import com.remember.app.ui.auth.AuthActivity;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.menu.events.EventsActivityMenu;
import com.remember.app.ui.menu.notifications.NotificationsActivity;
import com.remember.app.ui.menu.page.PageActivityMenu;
import com.remember.app.ui.menu.question.QuestionActivity;
import com.remember.app.ui.menu.settings.SettingActivity;
import com.remember.app.ui.utils.PopupPageScreen;
import com.remember.app.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.INTENT_EXTRA_IS_LIST;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON;
import static com.remember.app.data.Constants.INTENT_EXTRA_SHOW;
import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.data.Constants.SEARCH_ON_GRID;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class GridActivity extends BaseActivity implements GridView, ImageAdapter.Callback, ImagePagedAdapter.Callback,
        PopupPageScreen.Callback, NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = GridActivity.class.getSimpleName();

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
    @BindView(R.id.avatar_small_toolbar)
    ImageView avatar_user;
    @BindView(R.id.menu_icon)
    ImageView button_menu;
    @BindView(R.id.grid_sign_in)
    Button signInButton;
    @BindView(R.id.progressVideoView)
    VideoView progressVideoView;
    @BindView(R.id.grid_splash)
    RelativeLayout gridSplashLayout;
    @BindView(R.id.videoView)
    VideoView splashVideoView;
    @BindView(R.id.grid_main_content)
    RelativeLayout gridMainContentLayout;

    private ImageAdapter imageAdapter;
    private DrawerLayout drawer;
    private int theme_setting = 0;
    private boolean isLoaded = false;

    private int pageNumber = 1;
    private List<MemoryPageModel> allMemoryPageModels = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_grid;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);

        search.setImageResource(Utils.isThemeDark() ? R.drawable.ic_search_dark_theme : R.drawable.ic_search);

        imageAdapter = new ImageAdapter();
        imageAdapter.setCallback(this);
        imageAdapter.setContext(this);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setHasFixedSize(true);

//        startVideo();
        startSplashVideo();
        presenter.getImages(pageNumber);

        drawer = findViewById(R.id.drawer_layout_2);
    }

    private void startSplashVideo() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pomnyu_text_animation);
        splashVideoView.setVideoURI(videoUri);
        splashVideoView.start();
        splashVideoView.setOnCompletionListener(mp -> {
            stopSplashVideo();
        });
    }

    private void startVideo() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sand_clock_light);
        progressVideoView.setVideoURI(videoUri);
        progressVideoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (theme_setting == 1) {
            this.recreate();
        }
        drawer.setDrawerLockMode(
                Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)
                        ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                        : DrawerLayout.LOCK_MODE_UNLOCKED
        );
        getInfoUser();
    }

    @Override
    protected void onPause() {
        progressVideoView.stopPlayback();
        super.onPause();
    }

    private void getInfoUser() {
        if (!Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)) {
            avatar_user.setVisibility(View.VISIBLE);
            button_menu.setVisibility(View.VISIBLE);
            signInButton.setText("Перейти в кабинет");

            setSupportActionBar(findViewById(R.id.toolbar));

            NavigationView navigationView = findViewById(R.id.nav_view_grid);
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);

            TextView navUserName = headerView.findViewById(R.id.user_name);
            navUserName.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));

            TextView navEmail = headerView.findViewById(R.id.user_email);
            navEmail.setText(Prefs.getString(PREFS_KEY_EMAIL, ""));

            ImageView imageViewBigAvatar = headerView.findViewById(R.id.logo);

            if (Utils.isEmptyPrefsKey(PREFS_KEY_AVATAR)) {
                setGlideImage(this, R.drawable.ic_unknown, avatar_user);
                setGlideImage(this, R.drawable.ic_unknown, imageViewBigAvatar);
            } else {
                setGlideImage(this, Prefs.getString(PREFS_KEY_AVATAR, ""), avatar_user);
                setGlideImage(this, Prefs.getString(PREFS_KEY_AVATAR, ""), imageViewBigAvatar);
            }

            imageViewBigAvatar.setOnClickListener(view -> {
                startActivity(new Intent(this, SettingActivity.class));
            });
        } else {
            avatar_user.setVisibility(View.GONE);
            button_menu.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.grid_sign_in)
    public void entry() {
        if (!Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, AuthActivity.class));
        }
    }

    @OnClick(R.id.search)
    public void doSearch() {
        showEventScreen();
    }

    @OnClick(R.id.show_all)
    public void showAll() {
        imageAdapter.setItems(allMemoryPageModels);
        showAll.setVisibility(View.GONE);
        showMorePages();
    }

    @OnClick(R.id.menu_icon)
    public void onMenuClick() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void showEventScreen() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_page_screen, null);
        PopupPageScreen popupWindowPage = new PopupPageScreen(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindowPage.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        popupWindowPage.setFocusable(true);
        popupWindowPage.setCallback(this);
        popupWindowPage.setSourceType(SEARCH_ON_GRID);
        popupWindowPage.setUp(title);
    }

    @Override
    public void openPage(MemoryPageModel memoryPageModel) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra(INTENT_EXTRA_PERSON, memoryPageModel);
        intent.putExtra(INTENT_EXTRA_IS_LIST, true);
        intent.putExtra(INTENT_EXTRA_SHOW, true);
        startActivity(intent);
    }

    @Override
    public void showMorePages() {
        pageNumber++;
        presenter.getImages(pageNumber);
    }

    @Override
    public void openPageFromPagedAdapter(MemoryPageModel memoryPageModel) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra(INTENT_EXTRA_PERSON, memoryPageModel);
        intent.putExtra(INTENT_EXTRA_IS_LIST, true);
        intent.putExtra(INTENT_EXTRA_SHOW, true);
        startActivity(intent);
    }

    @Override
    public void search(RequestSearchPage requestSearchPage) {
        presenter.search(requestSearchPage);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.settings: {
                startActivity(new Intent(this, SettingActivity.class));
//                theme_setting = 1;
                return true;
            }
            case R.id.event_calendar: {
                startActivity(new Intent(this, EventsActivityMenu.class));
                return true;
            }
            case R.id.memory_pages: {
                startActivity(new Intent(this, PageActivityMenu.class));
                return true;
            }
            case R.id.questions: {
                startActivity(new Intent(this, QuestionActivity.class));
                return true;
            }
            case R.id.notifications: {
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            }
            case R.id.exit: {
                Prefs.clear();
                startActivity(new Intent(this, GridActivity.class));
                finish();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onReceivedImages(ResponsePages responsePages) {
        allMemoryPageModels.addAll(responsePages.getResult());
        int lastIndex = allMemoryPageModels.size() - 1;
        for (MemoryPageModel model : allMemoryPageModels) model.setShowMore(false);
        if (pageNumber < responsePages.getCount()) {
            allMemoryPageModels.get(lastIndex).setShowMore(true);
        }
        imageAdapter.setItems(allMemoryPageModels);

        isLoaded = true;
        stopVideo();
//        progressVideoView.postDelayed(this::stopVideo, 1000);
    }

    @Override
    public void onSearchedPages(List<MemoryPageModel> memoryPageModels) {
        if (memoryPageModels.isEmpty()) {
            Utils.showSnack(recyclerView, "Записи не найдены");
        }
        isLoaded = true;
        showAll.setVisibility(View.VISIBLE);
        imageAdapter.setItems(memoryPageModels);
    }

    @Override
    public void onError(Throwable throwable) {
        Utils.showSnack(recyclerView, "Ошибка получения плиток");
        stopVideo();
    }

    private void stopVideo() {
        progressVideoView.stopPlayback();
        progressVideoView.setVisibility(View.GONE);
    }

    private void stopSplashVideo() {
        splashVideoView.stopPlayback();
        gridSplashLayout.setVisibility(View.GONE);
        gridMainContentLayout.setVisibility(View.VISIBLE);

        if (!isLoaded) {
            progressVideoView.setVisibility(View.VISIBLE);
        }
    }
}
