package com.remember.app.ui.cabinet.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends MvpAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupPageScreen.Callback, PopupEventScreen.Callback, MainView {

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.title_name)
    TextView title;

    private Unbinder unbinder;
    private PageFragment pageFragment;
    private CallbackPage callbackPage;
    private ProgressDialog progressDialog;
    private ViewPager viewPager;
    private PopupEventScreen popupWindowEvent;
    private PopupPageScreen popupWindowPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pageFragment = new PageFragment();
        title.setText(Prefs.getString("NAME_USER", ""));

        Prefs.putBoolean("EVENT_FRAGMENT", false);
        Prefs.putBoolean("PAGE_FRAGMENT", true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        ImageView imageView = findViewById(R.id.add_plus);
        imageView.setOnClickListener(v -> {
            startActivity(new Intent(this, NewMemoryPageActivity.class));
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.title_menu);


        TextView textView = headerView.findViewById(R.id.textView);
        navUsername.setText(Prefs.getString("NAME_USER", ""));
        textView.setText(Prefs.getString("EMAIL", ""));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
        popupWindowPage.setUp(title);

    }

    private void showPageScreen() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_event_screen, null);
        popupWindowEvent = new PopupEventScreen(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindowEvent.setFocusable(true);
        popupWindowEvent.setCallback(this);
        popupWindowEvent.setUp(title);
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

    @Override
    protected void onResume() {
        super.onResume();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        ImageView imageViewAvatar = drawer.findViewById(R.id.avatar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView imageViewBigAvatar = headerView.findViewById(R.id.logo);
        if (!Prefs.getString("AVATAR", "").equals("")) {
            Glide.with(this)
                    .load(Prefs.getString("AVATAR", ""))
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageViewAvatar);

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
                    .into(imageViewAvatar);

            Glide.with(this)
                    .load(R.drawable.ic_unknown)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageViewBigAvatar);
        }
        setBlackWhite(imageViewBigAvatar);
        setBlackWhite(imageViewAvatar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        TextView textView = drawer.findViewById(R.id.title_menu);
        textView.setText(Prefs.getString("NAME_USER", ""));
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
