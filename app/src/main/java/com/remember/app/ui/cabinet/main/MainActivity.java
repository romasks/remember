package com.remember.app.ui.cabinet.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
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
import com.remember.app.ui.menu.settings.data.PersonalDataFragmentPresenter;
import com.remember.app.ui.menu.settings.data.PersonalDataFragmentView;
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
        implements NavigationView.OnNavigationItemSelectedListener, PopupPageScreen.Callback, PopupEventScreen.Callback, MainView, PersonalDataFragmentView {

    @InjectPresenter
    MainPresenter presenter;
@BindView(R.id.imageView6)
ImageView button_menu;
    @BindView(R.id.title_name)
    TextView title;
    @BindView(R.id.search2)
    ImageView searchImg;
    @BindView(R.id.add_plus)
    ImageView addImg;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @InjectPresenter
    PersonalDataFragmentPresenter presenterData;
    private Unbinder unbinder;
    private PageFragment pageFragment;
    private CallbackPage callbackPage;
    private ProgressDialog progressDialog;
    private PopupEventScreen popupWindowEvent;
    private PopupPageScreen popupWindowPage;
    private ImageView imageViewAvatar;
    private ImageView imageViewBigAvatar;
     private TextView textView;
     private int theme_setting=0;
     private static String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Prefs.getInt("IS_THEME",0)==2) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppTheme_Dark);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        if (Prefs.getInt("IS_THEME",0)==2){
            viewPager.setBackgroundColor(getResources().getColor(R.color.colorBlacDark));
            searchImg.setImageResource(R.drawable.ic_search_dark_theme);
            addImg.setImageResource(R.drawable.ic_add_white);
        }else {
            searchImg.setImageResource(R.drawable.ic_search);
            addImg.setImageResource(R.drawable.ic_add_black);
            viewPager.setBackgroundColor(getResources().getColor(android.R.color.white));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pageFragment = new PageFragment();
        Prefs.putBoolean("EVENT_FRAGMENT", false);
        Prefs.putBoolean("PAGE_FRAGMENT", true);
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
        button_menu.setOnClickListener(i->{
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }


    @OnClick(R.id.search2)
    public void search() {
        Prefs.putBoolean("IS_SHOWED", true);
        if (!Prefs.getBoolean("PAGE_FRAGMENT", true)) {
            presenter.getReligion();
        } else {
            showEventScreen();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onReceivedInfo(ResponseSettings responseSettings) {
        if (responseSettings.getPicture() != null) {
            Glide.with(this)
                    .load("http://помню.рус" + responseSettings.getPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageViewAvatar);
            Glide.with(this)
                    .load("http://помню.рус" + responseSettings.getPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageViewBigAvatar);
            textView.setText(responseSettings.getName()+" "+responseSettings.getSurname());
            Prefs.putString("AVATAR", "http://помню.рус" + responseSettings.getPicture());
            Prefs.putString("NAME_USER", responseSettings.getName() + " " + responseSettings.getSurname());



        } else{
            Log.i(TAG,"==null");
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
        setBlackWhite(imageViewAvatar);
        setBlackWhite(imageViewBigAvatar);
    }

    @Override
    public void error(Throwable throwable) {

    }

    @Override
    public void onSaved(Object o) {

    }

    @Override
    public void onSavedImage(Object o) {

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
        if (theme_setting==1){
            this.recreate();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
         imageViewAvatar = drawer.findViewById(R.id.avatar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        imageViewBigAvatar = headerView.findViewById(R.id.logo);
        if (!Prefs.getString("AVATAR", "").equals("")) {
            title.setText(Prefs.getString("NAME_USER", ""));
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
            Log.i(TAG,"AVATAR!=null");
            presenterData.getInfo();
        }
        setBlackWhite(imageViewBigAvatar);
        setBlackWhite(imageViewAvatar);
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
            theme_setting=1;
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
         textView = drawer.findViewById(R.id.title_menu);
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
