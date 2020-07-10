package com.remember.app.ui.grid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alphamovie.lib.AlphaMovieView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.BuildConfig;
import com.remember.app.R;
import com.remember.app.customView.CustomButton;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RefreshToken;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.ui.adapters.ImageAdapter;
import com.remember.app.ui.auth.AuthActivity;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.menu.events.EventsActivityMenu;
import com.remember.app.ui.menu.manual.ManualActivity;
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
import static com.remember.app.data.Constants.PREFS_KEY_IS_LAUNCH_MODE;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.data.Constants.PREFS_KEY_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.data.Constants.SEARCH_ON_GRID;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class GridActivity extends BaseActivity implements GridView, ImageAdapter.Callback,
        PopupPageScreen.Callback, NavigationView.OnNavigationItemSelectedListener, SettingActivity.MainActivityListener {

    private final String TAG = GridActivity.class.getSimpleName();

    @InjectPresenter
    GridPresenter presenter;

    @BindView(R.id.splash_video)
    AlphaMovieView splashVideo;

    @BindView(R.id.menu_icon)
    ImageView button_menu;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.avatar_small_toolbar)
    ImageView avatar_user;

    @BindView(R.id.image_rv)
    RecyclerView recyclerView;
    @BindView(R.id.grid_sign_in)
    CustomButton signInButton;

    @BindView(R.id.drawer_layout_2)
    DrawerLayout drawer;

    private ImageAdapter imageAdapter;
    private NavigationView navigationView;

    private int pageNumber = 1;
    private boolean isClickLocked = true;
    private List<MemoryPageModel> allMemoryPageModels = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_grid;
    }

    void checkIntentForNotification() {
        String s = getIntent().getStringExtra("firstLink");
        if (s != null)
        Log.d("TTTTT", s);

        String d = getIntent().getStringExtra("ID");
        if (d != null)
        Log.d("TTTTT", d);
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                if (value != null && !value.equals("")) {
                    if (value.equals("")) {
                        Log.d("TTTTT", "value zero");
                    }
                    Log.d("TTTTT", value);
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(9379992);
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        search.setImageResource(Utils.isThemeDark() ? R.drawable.ic_search_dark_theme : R.drawable.ic_search);
        SettingActivity.setListener(this);
        findViewById(R.id.app_bar_grid_layout).setClickable(!isClickLocked);
        signInButton.setClickable(!isClickLocked);
        sendID();
        setUpRecycler();
        checkIntentForNotification();
        presenter.refreshToken();
        presenter.getImages(pageNumber);
        if (Prefs.getBoolean(PREFS_KEY_IS_LAUNCH_MODE, false)) {
            Prefs.putBoolean(PREFS_KEY_IS_LAUNCH_MODE, false);
            setSplashVideo();
            splashVideo.start();
            splashVideo.postDelayed(() -> recyclerView.setVisibility(View.VISIBLE), 1000);
        } else {
            showMainContent();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfoUser();
        drawer.setDrawerLockMode(
                Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)
                        ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                        : DrawerLayout.LOCK_MODE_UNLOCKED
        );

        changeMenuSize(navigationView);

    }

    @Override
    protected void onPause() {
        splashVideo.onPause();
        showMainContent();
        super.onPause();
    }

    private void getInfoUser() {
        if (!Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)) {
            avatar_user.setVisibility(View.VISIBLE);
            button_menu.setVisibility(View.VISIBLE);
            signInButton.setText(getResources().getString(R.string.grid_go_to_cabinet));

            setSupportActionBar(findViewById(R.id.toolbar));

            navigationView = findViewById(R.id.nav_view_grid);
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);
            CustomTextView version = navigationView.findViewById(R.id.version);
            version.setText("Версия " + BuildConfig.VERSION_NAME);

            CustomTextView navUserName = headerView.findViewById(R.id.user_name);
            navUserName.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));

            CustomTextView navEmail = headerView.findViewById(R.id.user_email);
            navEmail.setText(Prefs.getString(PREFS_KEY_EMAIL, ""));

            ImageView imageViewBigAvatar = headerView.findViewById(R.id.logo);

            if (Utils.isEmptyPrefsKey(PREFS_KEY_AVATAR)) {
                setGlideImage(this, R.drawable.ic_unknown, avatar_user);
                setGlideImage(this, R.drawable.ic_unknown, imageViewBigAvatar);
            } else {
                setGlideImage(this, Prefs.getString(PREFS_KEY_AVATAR, ""), avatar_user);
                setGlideImage(this, Prefs.getString(PREFS_KEY_AVATAR, ""), imageViewBigAvatar);
            }

            imageViewBigAvatar.setOnClickListener(view -> goToSettingsPage());
        } else {
            avatar_user.setVisibility(View.GONE);
            button_menu.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.grid_sign_in)
    public void entry() {
        if (signInButton.getText().equals(getResources().getString(R.string.grid_show_all))) {
            showAll();
        } else {
            if (!Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, AuthActivity.class));
            }
        }
    }

    @OnClick(R.id.search)
    public void doSearch() {
        showEventScreen();
    }

    private void showAll() {
        if (!Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)) {
            signInButton.setText(getResources().getString(R.string.grid_go_to_cabinet));
        } else {
            signInButton.setText(getResources().getString(R.string.grid_login));
        }
        imageAdapter.setItems(allMemoryPageModels);
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

    @OnClick(R.id.avatar_small_toolbar)
    public void onSmallAvatarClick() {
        goToSettingsPage();
    }

    private void showEventScreen() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_page_screen, null);
        PopupPageScreen popupWindowPage = new PopupPageScreen(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, this.getSupportFragmentManager());
        popupWindowPage.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        popupWindowPage.setFocusable(true);
        popupWindowPage.setCallback(this);
        popupWindowPage.setSourceType(SEARCH_ON_GRID);
        popupWindowPage.setUp(title);
    }

    @Override
    public void openPage(MemoryPageModel memoryPageModel) {
        if (!isClickLocked) {
            Intent intent = new Intent(this, ShowPageActivity.class);
            intent.putExtra(INTENT_EXTRA_PERSON, memoryPageModel);
            intent.putExtra(INTENT_EXTRA_IS_LIST, true);
            intent.putExtra(INTENT_EXTRA_SHOW, true);
            startActivity(intent);
        }
    }

    @Override
    public void showMorePages() {
        pageNumber++;
        presenter.getImages(pageNumber);
    }

    @Override
    public void search(RequestSearchPage requestSearchPage) {
        presenter.search(requestSearchPage);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_cabinet: {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
            case R.id.menu_gallery: {
                startActivity(new Intent(this, GridActivity.class));
                return true;
            }
            case R.id.menu_memory_pages: {
                startActivity(new Intent(this, PageActivityMenu.class));
                return true;
            }
            case R.id.menu_event_calendar: {
                startActivity(new Intent(this, EventsActivityMenu.class));
                return true;
            }
            case R.id.menu_notifications: {
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            }
            case R.id.menu_settings: {
                goToSettingsPage();
                return true;
            }
            case R.id.menu_questions: {
                startActivity(new Intent(this, QuestionActivity.class));
                return true;
            }
            case R.id.menu_manual: {
                startActivity(new Intent(this, ManualActivity.class));
                return true;
            }
            case R.id.menu_exit: {
                unsubscribeToTopic();
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
        for (MemoryPageModel model : allMemoryPageModels) {
            model.setShowMore(false);
        }
        if (pageNumber < responsePages.getCount()) {
            allMemoryPageModels.get(lastIndex).setShowMore(true);
        }
        imageAdapter.setItems(allMemoryPageModels);
        //TODO
    }

    @Override
    public void onSearchedPages(List<MemoryPageModel> memoryPageModels) {
        if (memoryPageModels.isEmpty()) {
            Utils.showSnack(recyclerView, "Записи не найдены");
        }
        imageAdapter.setItems(memoryPageModels);
        signInButton.setText(getResources().getString(R.string.grid_show_all));
    }

    @Override
    public void onError(Throwable throwable) {
        Utils.showSnack(recyclerView, "Ошибка получения плиток");
    }

    @Override
    public void onStatus(Object o) {
        Prefs.putBoolean("deviceID", false);
    }


    private void setUpRecycler() {
        imageAdapter = new ImageAdapter(this);
        imageAdapter.setCallback(this);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void setSplashVideo() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pomnyu_logo_animation);
        splashVideo.setVideoFromUri(this, videoUri);
        splashVideo.setLooping(false);
        splashVideo.setOnVideoEndedListener(this::hideSplashVideo);
    }

    private void hideSplashVideo() {
        showMainContent();
        splashVideo.setAlpha(1.0f);
        splashVideo.animate()
                .translationY(0)
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        splashVideo.setVisibility(View.GONE);
                    }
                });
    }

    private void showMainContent() {
        isClickLocked = false;
        findViewById(R.id.app_bar_grid_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.app_bar_grid_layout).setClickable(!isClickLocked);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setClickable(!isClickLocked);
        signInButton.setVisibility(View.VISIBLE);
        signInButton.setClickable(!isClickLocked);

        changeMenuSize(navigationView);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void goToSettingsPage() {
        startActivity(new Intent(this, SettingActivity.class));
    }

    private void unsubscribeToTopic() {
        String topic = "user-" + Prefs.getString(PREFS_KEY_USER_ID, "");
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener(task -> {
                    String msg = "unsubscr";
                    if (!task.isSuccessful()) {
                        msg = "not unsubscr";
                    }
                    Log.d(TAG, msg);
                });
    }

    private void setScaleText(NavigationView navigationView, float proportion, int id) {
        MenuItem item = navigationView.getMenu().findItem(id);
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new RelativeSizeSpan(proportion), 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(spanString);
    }

    private void changeMenuSize(NavigationView navigationView) {
        if (navigationView != null) {
            float proportion;
            if (Prefs.getBoolean("standard", true))
                proportion = 1f;
            else {
                proportion = 1.25f;
            }

            setScaleText(navigationView, proportion, R.id.menu_cabinet);
            setScaleText(navigationView, proportion, R.id.menu_gallery);
            setScaleText(navigationView, proportion, R.id.menu_memory_pages);
            setScaleText(navigationView, proportion, R.id.menu_event_calendar);
            setScaleText(navigationView, proportion, R.id.menu_notifications);
            setScaleText(navigationView, proportion, R.id.menu_settings);
            setScaleText(navigationView, proportion, R.id.menu_questions);
            setScaleText(navigationView, proportion, R.id.menu_manual);
            setScaleText(navigationView, proportion, R.id.menu_exit);
        }
    }

    @Override
    public void finishMainActivity() {
        recreate();
    }


    private void sendID() {
        if (Prefs.getBoolean("deviceID", true)) {
            @SuppressLint("HardwareIds")
            String androidID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            presenter.sendDeviceID(androidID);
        }
    }

    @Override
    public void onErrorSendID(Throwable throwable) {
        Prefs.putBoolean("deviceID", true);
    }

    @Override
    public void refreshToken(RefreshToken model) {
        Prefs.putString(PREFS_KEY_TOKEN, model.getToken());
    }

    @Override
    public void onErrorRefresh(Throwable throwable) {
        Prefs.clear();
        signInButton.setText(getResources().getString(R.string.grid_login));
    }
}
