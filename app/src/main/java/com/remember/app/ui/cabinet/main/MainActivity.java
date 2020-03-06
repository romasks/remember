package com.remember.app.ui.cabinet.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.ResponseUserInfo;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.FragmentPager;
import com.remember.app.ui.cabinet.events.EventFragment;
import com.remember.app.ui.cabinet.memory_pages.PageFragment;
import com.remember.app.ui.cabinet.memory_pages.add_page.NewMemoryPageActivity;
import com.remember.app.ui.grid.GridActivity;
import com.remember.app.ui.menu.events.EventsActivityMenu;
import com.remember.app.ui.menu.notifications.NotificationsActivity;
import com.remember.app.ui.menu.page.PageActivityMenu;
import com.remember.app.ui.menu.question.QuestionActivity;
import com.remember.app.ui.menu.settings.SettingActivity;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.PopupEventScreen;
import com.remember.app.ui.utils.PopupPageScreen;
import com.remember.app.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.data.Constants.PREFS_KEY_THEME;
import static com.remember.app.data.Constants.PREFS_KEY_THEME_CHANGED;
import static com.remember.app.data.Constants.PREFS_KEY_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.data.Constants.SEARCH_ON_MAIN;
import static com.remember.app.data.Constants.THEME_LIGHT;
import static com.remember.app.ui.utils.ImageUtils.getBlackWhiteFilter;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupPageScreen.Callback, PopupEventScreen.Callback, MainView {

    private static String TAG = MainActivity.class.getSimpleName();

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.title_name)
    TextView titleUserName;
    @BindView(R.id.search)
    ImageView searchImg;
    @BindView(R.id.add_plus)
    ImageView addImg;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    private PageFragment pageFragment;
    private CallbackPage callbackPage;
    private ProgressDialog progressDialog;
    private PopupEventScreen popupWindowEvent;
    private PopupPageScreen popupWindowPage;

    private ImageView imageViewAvatar;
    private ImageView imageViewBigAvatar;
    private TextView navUsername;

    View.OnClickListener onAvatarClickListener = view -> {
        startActivity(new Intent(this, SettingActivity.class));
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);

        if (Utils.isThemeDark()) {
            viewPager.setBackgroundColor(getResources().getColor(R.color.colorBlackDark));
            searchImg.setImageResource(R.drawable.ic_search_dark_theme);
            addImg.setImageResource(R.drawable.ic_add_white);
        } else {
            searchImg.setImageResource(R.drawable.ic_search);
            addImg.setImageResource(R.drawable.ic_add_black);
            viewPager.setBackgroundColor(getResources().getColor(android.R.color.white));
        }

        if (Prefs.getBoolean(PREFS_KEY_THEME_CHANGED, false)) {
            Prefs.putBoolean(PREFS_KEY_THEME_CHANGED, false);
//            return;
        }
        setUp();
    }

    private void setUp() {
        setSupportActionBar(findViewById(R.id.toolbar));

        pageFragment = new PageFragment();
        Prefs.putBoolean("EVENT_FRAGMENT", false);
        Prefs.putBoolean("PAGE_FRAGMENT", true);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        imageViewBigAvatar = headerView.findViewById(R.id.logo);
        imageViewBigAvatar.setOnClickListener(onAvatarClickListener);
        imageViewBigAvatar.setColorFilter(getBlackWhiteFilter());

        navUsername = headerView.findViewById(R.id.user_name);
        navUsername.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));

        TextView navEmail = headerView.findViewById(R.id.user_email);
        navEmail.setText(Prefs.getString(PREFS_KEY_EMAIL, ""));

        imageViewAvatar = drawer.findViewById(R.id.avatar);
        imageViewAvatar.setOnClickListener(onAvatarClickListener);
        imageViewAvatar.setColorFilter(getBlackWhiteFilter());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Utils.isEmptyPrefsKey(PREFS_KEY_TOKEN)) {
            presenter.getInfo();
        } else {
            navUsername.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));
            titleUserName.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));

            setGlideImage(this, R.drawable.ic_unknown, imageViewAvatar);
            setGlideImage(this, R.drawable.ic_unknown, imageViewBigAvatar);
        }
    }

    @OnClick(R.id.menu_icon)
    public void onMenuClick() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);

//            Switch themeSwitch = drawer.findViewById(R.id.switch_theme);
//            themeSwitch.setChecked(Prefs.getBoolean(PREFS_KEY_THEME, THEME_LIGHT));
        }
    }

    @OnClick(R.id.search)
    public void search() {
        Prefs.putBoolean("IS_SHOWED", true);
        if (!Prefs.getBoolean("PAGE_FRAGMENT", true)) {
            presenter.getReligion();
        } else {
            showEventScreen();
        }
    }

    @OnClick(R.id.add_plus)
    public void addNewMemoryPage() {
        startActivity(new Intent(this, NewMemoryPageActivity.class));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onReceivedInfo(ResponseUserInfo responseSettings) {
        String userName = responseSettings.getSettings().getName() + " " + responseSettings.getSettings().getSurname();
        Prefs.putString(PREFS_KEY_NAME_USER, userName);
        navUsername.setText(userName.trim());
        titleUserName.setText(userName.trim());

        if (!responseSettings.getSettings().getPicture().isEmpty()) {
            setGlideImage(this, responseSettings.getSettings().getPicture(), imageViewAvatar);
            setGlideImage(this, responseSettings.getSettings().getPicture(), imageViewBigAvatar);
        } else {
            setGlideImage(this, R.drawable.ic_unknown, imageViewAvatar);
            setGlideImage(this, R.drawable.ic_unknown, imageViewBigAvatar);
        }
        Prefs.putString(PREFS_KEY_AVATAR, responseSettings.getSettings().getPicture());
    }

    @Override
    public void onError(Throwable throwable) {
        Utils.showSnack(imageViewAvatar, "Неопределённая ошибка с сервера");
        setGlideImage(this, R.drawable.ic_unknown, imageViewAvatar);
        setGlideImage(this, R.drawable.ic_unknown, imageViewBigAvatar);
    }

    public interface CallbackPage {
        void sendItemsSearch(List<MemoryPageModel> result);
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
        popupWindowPage.setUp(titleUserName);
    }

    private void showPageScreen() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_event_screen, null);
        popupWindowEvent = new PopupEventScreen(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindowEvent.setFocusable(true);
        popupWindowEvent.setCallback(this);
        popupWindowEvent.setUp(titleUserName);
    }

    @Override
    public void onBackPressed() {
        if (popupWindowEvent != null && popupWindowEvent.isShowing()) {
            popupWindowEvent.dismiss();
        } else if (popupWindowPage != null && popupWindowPage.isShowing()) {
            popupWindowPage.dismiss();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentPager adapter = new FragmentPager(getSupportFragmentManager());
        adapter.addFragment(pageFragment, "Памятные страницы");
        adapter.addFragment(new EventFragment(), "События");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                findViewById(R.id.add_plus).setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                findViewById(R.id.search).setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            }
            case R.id.menu_questions: {
                startActivity(new Intent(this, QuestionActivity.class));
                return true;
            }
//            case R.id.menu_theme: {
//                Switch themeSwitch = drawer.findViewById(R.id.switch_theme);
//                themeSwitch.setChecked(!Prefs.getBoolean(PREFS_KEY_THEME, THEME_LIGHT));
//                changeTheme();
//                return true;
//            }
                //TODO
            case R.id.menu_exit: {
                Prefs.clear();
                startActivity(new Intent(this, GridActivity.class));
                finish();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onReceivedReligions(List<String> responseHandBooks) {
        showPageScreen();
    }

    @Override
    public void onSearchedLastNames(List<MemoryPageModel> memoryPageModels) {
        List<MemoryPageModel> ownMemoryPageModels = new ArrayList<>();
        for (MemoryPageModel memoryPage : memoryPageModels) {
            if (memoryPage.getUserId().equals(Prefs.getString(PREFS_KEY_USER_ID, ""))) {
                ownMemoryPageModels.add(memoryPage);
            }
        }
        progressDialog.dismiss();
        callbackPage.sendItemsSearch(ownMemoryPageModels);
    }

    @Override
    public void search(RequestSearchPage requestSearchPage) {
        progressDialog = LoadingPopupUtils.showLoadingDialog(this);
        presenter.searchLastName(requestSearchPage);
    }

    public void setCallback(CallbackPage callback) {
        this.callbackPage = callback;
    }

    public void changeTheme() {
        Prefs.putBoolean(PREFS_KEY_THEME_CHANGED, true);
        Prefs.putBoolean(PREFS_KEY_THEME, !Prefs.getBoolean(PREFS_KEY_THEME, THEME_LIGHT));
        this.recreate();
    }
}
