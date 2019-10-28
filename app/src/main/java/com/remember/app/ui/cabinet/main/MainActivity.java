package com.remember.app.ui.cabinet.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.ResponseSettings;
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
import com.remember.app.ui.utils.MvpAppCompatActivity;
import com.remember.app.ui.utils.PopupEventScreen;
import com.remember.app.ui.utils.PopupPageScreen;

import java.util.List;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class MainActivity extends MvpAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupPageScreen.Callback, PopupEventScreen.Callback, MainView {

    private static String TAG = MainActivity.class.getSimpleName();

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.imageView6)
    ImageView button_menu;
    @BindView(R.id.title_name)
    TextView titleUserName;
    @BindView(R.id.search)
    ImageView searchImg;
    @BindView(R.id.add_plus)
    ImageView addImg;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private Unbinder unbinder;
    private PageFragment pageFragment;
    private CallbackPage callbackPage;
    private ProgressDialog progressDialog;
    private PopupEventScreen popupWindowEvent;
    private PopupPageScreen popupWindowPage;
    private ImageView imageViewAvatar;
    private ImageView imageViewBigAvatar;
    private TextView navUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        searchImg.setImageResource(R.drawable.ic_search);
        addImg.setImageResource(R.drawable.ic_add_black);
        viewPager.setBackgroundColor(getResources().getColor(android.R.color.white));

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

        navUsername = headerView.findViewById(R.id.user_name);
        navUsername.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));

        TextView navEmail = headerView.findViewById(R.id.user_email);
        navEmail.setText(Prefs.getString(PREFS_KEY_EMAIL, ""));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        button_menu.setOnClickListener(i -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        imageViewAvatar = drawer.findViewById(R.id.avatar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        imageViewBigAvatar = headerView.findViewById(R.id.logo);

        String avatarStr = Prefs.getString(PREFS_KEY_AVATAR, "");
        if (!Prefs.getString(PREFS_KEY_AVATAR, "").equals("")) {
            titleUserName.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));

            setGlideImage(this, Prefs.getString(PREFS_KEY_AVATAR, ""), imageViewAvatar);
            setGlideImage(this, Prefs.getString(PREFS_KEY_AVATAR, ""), imageViewBigAvatar);
        } else {
            presenter.getInfo();
        }

        setBlackWhite(imageViewBigAvatar);
        setBlackWhite(imageViewAvatar);

        imageViewBigAvatar.setOnClickListener(view -> {
            startActivity(new Intent(this, SettingActivity.class));
        });
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
    public void onReceivedInfo(ResponseSettings responseSettings) {
        if (responseSettings.getPicture() != null) {
            setGlideImage(this, BASE_SERVICE_URL + responseSettings.getPicture(), imageViewAvatar);
            setGlideImage(this, BASE_SERVICE_URL + responseSettings.getPicture(), imageViewBigAvatar);

            navUsername.setText(responseSettings.getName() + " " + responseSettings.getSurname());
            Prefs.putString(PREFS_KEY_AVATAR, BASE_SERVICE_URL + responseSettings.getPicture());
            Prefs.putString(PREFS_KEY_NAME_USER, responseSettings.getName() + " " + responseSettings.getSurname());

        } else {
            setGlideImage(this, R.drawable.ic_unknown, imageViewAvatar);
            setGlideImage(this, R.drawable.ic_unknown, imageViewBigAvatar);
        }

        setBlackWhite(imageViewAvatar);
        setBlackWhite(imageViewBigAvatar);
    }

    public interface CallbackPage {
        void sendItemsSearch(List<MemoryPageModel> result);
    }

    public void setBlackWhite(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
            return true;
        }
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        TextView userName = drawer.findViewById(R.id.user_name);
        userName.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onReceivedReligions(List<String> responseHandBooks) {
        showPageScreen();
    }

    @Override
    public void onSearchedLastNames(List<MemoryPageModel> memoryPageModels) {
        progressDialog.dismiss();
        callbackPage.sendItemsSearch(memoryPageModels);
    }

    @Override
    public void search(RequestSearchPage requestSearchPage) {
        progressDialog = LoadingPopupUtils.showLoadingDialog(this);
        presenter.searchLastName(requestSearchPage);
    }

    public void setCallback(CallbackPage callback) {
        this.callbackPage = callback;
    }
}
