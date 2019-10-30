package com.remember.app.ui.grid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.ui.adapters.ImageAdapter;
import com.remember.app.ui.auth.AuthActivity;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.menu.events.EventsActivityMenu;
import com.remember.app.ui.menu.notifications.NotificationsActivity;
import com.remember.app.ui.menu.page.PageActivityMenu;
import com.remember.app.ui.menu.question.QuestionActivity;
import com.remember.app.ui.menu.settings.SettingActivity;
import com.remember.app.ui.menu.settings.data.PersonalDataFragmentPresenter;
import com.remember.app.ui.menu.settings.data.PersonalDataFragmentView;
import com.remember.app.ui.utils.PopupPageScreen;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GridActivity extends BaseActivity implements GridView, ImageAdapter.Callback, PersonalDataFragmentView, PopupPageScreen.Callback,NavigationView.OnNavigationItemSelectedListener {

    @InjectPresenter
    GridPresenter presenter;

    @BindView(R.id.image_rv)
    RecyclerView recyclerView;
    @BindView(R.id.search2)
    ImageView search;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.show_all)
    Button showAll;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.imageView5)
    ImageView avatar_user;
    @BindView(R.id.imageView4)
    ImageView button_menu;

    @InjectPresenter
    PersonalDataFragmentPresenter presenterData;

    private RecyclerView.LayoutManager layoutManager;
    private ImageAdapter imageAdapter;
    private int pageNumber = 1;
    private int theme_setting=0;
    private int countSum = 0;
    private PopupPageScreen popupWindowPage;
    private ImageView imageViewBigAvatar;
    private final String TAG="GridActivity";
    private DrawerLayout drawer;

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
            search.setImageResource(R.drawable.ic_search_dark_theme);
        }else {
            search.setImageResource(R.drawable.ic_search);
        }
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        imageAdapter = new ImageAdapter();
        imageAdapter.setCallback(this);
        recyclerView.setAdapter(imageAdapter);
//        setUpLoadMoreListener();
        drawer = findViewById(R.id.drawer_layout_2);
        presenter.getImages(pageNumber);
        showAll.setOnClickListener(v -> {
            showAll.setVisibility(View.GONE);
            pageNumber = 1;
            presenter.getImages(pageNumber);
        });
        button_menu.setOnClickListener(k->{
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }

    public void setBlackWhite(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    @SuppressLint("WrongConstant")
    private void getInfoUser(){
        if (!Prefs.getString("USER_ID", "").equals("")) {
            avatar_user.setVisibility(View.VISIBLE);
            button_menu.setVisibility(View.VISIBLE);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            NavigationView navigationView = findViewById(R.id.nav_view_grid);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = headerView.findViewById(R.id.title_menu);
            imageViewBigAvatar= headerView.findViewById(R.id.logo);
            TextView textView = headerView.findViewById(R.id.textView);
            navUsername.setText(Prefs.getString("NAME_USER", ""));
            textView.setText(Prefs.getString("EMAIL", ""));
            navigationView.setNavigationItemSelectedListener(this);
            if(Prefs.getString("AVATAR", "").equals("")){
                presenterData.getInfo();
            }else {
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
//                setBlackWhite(avatar_user);
//                setBlackWhite(imageViewBigAvatar);
            }

        } else {

            avatar_user.setVisibility(View.GONE);
            button_menu.setVisibility(View.GONE);

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

    @OnClick(R.id.search2)
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
                Log.i(TAG,"pageNumber"+pageNumber+" countSum"+countSum);
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
    protected void onResume() {
        super.onResume();
        if (theme_setting==1){
            this.recreate();
        }
        getInfoUser();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

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
        if (id==R.id.notifications){
            startActivity(new Intent(this, NotificationsActivity.class));
            return  true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        TextView textView = drawer.findViewById(R.id.title_menu);
        textView.setText(Prefs.getString("NAME_USER", ""));
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onReceivedInfo(ResponseSettings responseSettings) {
        if (responseSettings.getPicture() != null) {
            Prefs.putString("AVATAR", "http://помню.рус" + responseSettings.getPicture());
            Prefs.putString("NAME_USER", responseSettings.getName() + " " + responseSettings.getSurname());
            Glide.with(this)
                    .load("http://помню.рус" + responseSettings.getPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(avatar_user);
            Glide.with(this)
                    .load("http://помню.рус" + responseSettings.getPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageViewBigAvatar);

        } else{
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
//        setBlackWhite(avatar_user);
//        setBlackWhite(imageViewBigAvatar);
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


}
